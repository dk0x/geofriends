
const CONTENT_TYPE_JSON = {
    headers: {
        'Content-Type': 'application/json;charset=UTF-8'
    }
}
const COOKIE_NAME_SESSION_ID = "sessionId";
const URL_ORIGIN = location.origin;
const URL_PATH_API = URL_ORIGIN + '/api';
const URL_PATH_API_VK_AUTH = URL_PATH_API + '/vk/auth';
const URL_PATH_API_VK_FORGET_ME = URL_PATH_API + '/vk/forgetMe';
const URL_PATH_API_VK_FRIENDS = URL_PATH_API + '/vk/friends';
const URL_PATH_API_VK_FOLLOWERS = URL_PATH_API + '/vk/followers';
const URL_PATH_API_GEO_GEOCODE = URL_PATH_API + '/geo/geocode';

const VK_URL_PATH_OAUTH = 'https://oauth.vk.com/authorize';
const VK_APP_ID = 7087056;
const VK_URL_HREF_OAUTH = VK_URL_PATH_OAUTH + '?client_id=' + VK_APP_ID + '&redirect_uri=' + URL_PATH_API_VK_AUTH + '&display=page&scope=friends&response_type=code&v=5.101';

function getCookie(name) {
    const matches = document.cookie.match(new RegExp("(?:^|; )" + name.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, '\\$1') + "=([^;]*)"));
    return matches ? decodeURIComponent(matches[1]) : undefined;
}

const store = new Vuex.Store({
    state: {
        isLoading: true,
        isAuth: false,
        displayFriendsVK: true,
        displayFollowersVK: true,
        peopleFilter: "",
        cities: [],
        people: [],
    },
    getters: {
        getCityById: state => id => {
            return state.cities.find(city => city.id == id);
        },
        getPersonById: state => id => {
            return state.people.find(person => person.id == id);
        },
    },
    mutations: {
        setIsLoading: (state, val) => state.isLoading = val,
        setIsAuth: (state, val) => state.isAuth = val,
        setCities: (state, cities) => state.cities = cities,
        setPeople: (state, people) => state.people = people,
        addPerson: (state, person) => {
            if (person.firstName == "DELETED") return;
            if (person.cityId == -1) return;
            let existPerson = state.people.find(p => p.id == person.id);
            if (!existPerson) {
                state.people.push(person);
            }
        },
    },
    actions: {
        fetchFriendsVK: async (context) => {
            try {
                let response = await axios.get(URL_PATH_API_VK_FRIENDS);
                if (response.status == 200) {
                    let persons = response.data;
                    persons.forEach(person => {
                        person.source = "vk";
                        person.type = "friend";
                        context.commit('addPerson', person);
                    });
                } else {
                    console.log(response);
                }
            } catch (err) {
                console.log(err);
            }
        },
        fetchFollowersVK: async (context) => {
            try {
                let response = await axios.get(URL_PATH_API_VK_FOLLOWERS);
                if (response.status == 200) {
                    let persons = response.data;
                    persons.forEach(person => {
                        person.source = "vk";
                        person.type = "follower";
                        context.commit('addPerson', person);
                    });
                } else {
                    console.log(response);
                }
            } catch (err) {
                console.log(err);
            }
        },
        updateCitiesCoords: async (context, cities) => {
            try {
                let json = JSON.stringify(cities);
                let response = await axios.post(URL_PATH_API_GEO_GEOCODE, json, CONTENT_TYPE_JSON);
                if (response.status == 200) {
                    context.commit('setCities', response.data);
                } else {
                    console.log(response);
                }
            } catch (err) {
                console.log(err);
            }
        },
        forgetMe: async (context) => {
            context.commit('setIsLoading', true);
            try {
                let response = await axios.delete(URL_PATH_API_VK_FORGET_ME);
                if (response.status == 200) {
                    context.commit('setIsAuth', false);
                } else {
                    console.log(response);
                }
            } catch (err) {
                console.log(err);
            }
            context.commit('setIsLoading', false);
        }
    }
})

var app = new Vue({
    el: '#app',
    store,
    data: {
        defaultZoom: 12,
        map: null,
        tileLayer: null,
        clusters: null,
    },
    mounted: function () {
        let userAlreadyHaveSession = getCookie(COOKIE_NAME_SESSION_ID) != undefined;
        this.setIsAuth(userAlreadyHaveSession);

        this.initMap();
        this.fetchFriendsVk();
        this.fetchFollowersVK();

        this.setIsLoading(false);
    },
    computed: {
        vkOAuthURI: () => VK_URL_HREF_OAUTH,
        isLoading: () => store.state.isLoading,
        isAuth: () => store.state.isAuth,
        cities: () => store.state.cities,
        people: () => store.state.people,
        filteredPeople: () => {
            let displayFriendsVK = store.state.displayFriendsVK;
            let displayFollowersVK = store.state.displayFollowersVK;
            let peopleFilter = store.state.peopleFilter;
            let filtered = store.state.people.filter(person => {
                if (peopleFilter && peopleFilter != "") {
                    let filterWords = peopleFilter.toLowerCase().split(' ');
                    for (let word of filterWords) {
                        let name = person.lastName + ' ' + person.firstName;
                        name = name.toLowerCase();
                        if (!name.includes(word)) {
                            return false;
                        }
                    }
                }
                if (person.source === "vk") {
                    if (person.type === "friend") {
                        return displayFriendsVK;
                    }
                    if (person.type === "follower") {
                        return displayFollowersVK;
                    }
                }
                return true;
            });
            let sorted = filtered.sort((a, b) => {
                let aName = a.lastName + ' ' + a.firstName;
                let bName = b.lastName + ' ' + b.firstName;
                return aName.toLowerCase() > bName.toLowerCase() ? 1 : -1;
            });
            return sorted;
        },
    },
    watch: {
        people: function () {
            // people updated event, lets update cities (extract from people)
            let citiesMap = this.people.reduce((map, person) => {
                if (person.cityId == -1) return map;
                map[person.cityId] = {
                    id: person.cityId,
                    name: person.city,
                    country: person.country,
                    latitude: 0,
                    longitude: 0
                };
                return map;
            }, {});
            let cities = Object.values(citiesMap);
            store.dispatch('updateCitiesCoords', cities);
        },
        cities: function () {
            // cities updated event, lets update markers
            this.updateMarkers();
        },
        filteredPeople: function () {
            // filteredPeople updated event, lets update markers
            this.updateMarkers();
        },
    },
    methods: {
        setIsLoading: (val) => store.commit('setIsLoading', val),
        setIsAuth: (val) => store.commit('setIsAuth', val),
        forgetMe: () => store.dispatch('forgetMe'),
        goToVKAuth: () => location.href = VK_URL_HREF_OAUTH,
        fetchFriendsVk: () => store.dispatch('fetchFriendsVK'),
        fetchFollowersVK: () => store.dispatch('fetchFollowersVK'),

        initMap: function () {
            this.map = L.map('map');
            this.map.setView([54.9884804, 73.3242361], this.defaultZoom);

            this.tileLayer = L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw', {
                maxZoom: 14,
                attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, <a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
                id: 'mapbox.streets'
            });
            this.tileLayer.addTo(this.map)

            this.clusters = L.markerClusterGroup({ maxClusterRadius: 20 });
            this.map.addLayer(this.clusters);
        },

        updateMarkers: function () {
            let markers = [];
            let personInCityCounters = [];
            for (let person of this.filteredPeople) {
                // get city coords
                if (person.cityId == -1) continue;
                let city = store.getters.getCityById(person.cityId);
                if (!city) continue;

                // icon with photo
                let size = [50, 50];
                if (person.type === "follower") {
                    size = [45, 45];
                }
                let markerIconWithPhoto = L.icon({
                    iconUrl: person.photoUri,
                    className: "leaflet-marker-icon-" + person.source + "-" + person.type,
                    iconSize: size,
                    popupAnchor: [0, -30]
                });

                // if 2+ person live in same place, we need offset marker
                if (personInCityCounters[person.cityId] === undefined) {
                    personInCityCounters[person.cityId] = 0;
                }
                let num = personInCityCounters[person.cityId]++;
                let baseAngle = Math.PI * (1 + Math.sqrt(5)) * 6;
                let r = Math.sqrt(num) * 0.01;
                let theta = num * baseAngle;
                let x = r * Math.cos(theta) * 0.65;
                let y = r * Math.sin(theta);

                let marker = L.marker([city.latitude + x, city.longitude + y], { icon: markerIconWithPhoto });
                marker.bindPopup(person.firstName + ' ' + person.lastName);

                markers.push(marker);
            }

            // clear old and add new markers
            this.clusters.clearLayers();
            for (let marker of markers) {
                this.clusters.addLayer(marker);
            }
        },

        goToCityId: function (id) {
            let city = store.getters.getCityById(id);
            this.goToCoords([city.latitude, city.longitude]);
        },
        goToCoords: function (coords) {
            this.map.setView(coords, this.defaultZoom);
        },

    }
})