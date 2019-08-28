
const CONTENT_TYPE_JSON = {
    headers: {
        'Content-Type': 'application/json;charset=UTF-8'
    }
}
const COOKIE_NAME_SESSION_ID = "vkSessionId";
const URL_ORIGIN = location.origin;
const URL_PATH_API = URL_ORIGIN + '/api';
const URL_PATH_API_VK_AUTH = URL_PATH_API + '/vk/auth';
const URL_PATH_API_VK_FORGET_ME = URL_PATH_API + '/vk/forgetMe';
const URL_PATH_API_VK_FRIENDS = URL_PATH_API + '/vk/friends';
const URL_PATH_API_VK_SUBSCRIBERS = URL_PATH_API + '/vk/subscribers';
const URL_PATH_API_GEO_GEOCODE = URL_PATH_API + '/geo/geocode';

const VK_URL_PATH_OAUTH = 'https://oauth.vk.com/authorize';
const VK_APP_ID = 7087056;
const VK_URL_FULL_OAUTH = VK_URL_PATH_OAUTH + '?client_id=' + VK_APP_ID + '&redirect_uri=' + URL_ORIGIN + '&display=page&scope=friends&response_type=code&v=5.101';

function getCookie(name) {
    const matches = document.cookie.match(new RegExp("(?:^|; )" + name.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, '\\$1') + "=([^;]*)"));
    return matches ? decodeURIComponent(matches[1]) : undefined;
}
function getUrlParameter(name) {
    const splitedUrlArray = location.href.split('?');
    const params = new URLSearchParams(splitedUrlArray[1]);
    return params.get(name);
}

const store = new Vuex.Store({
    state: {
        isLoading: true,
        isAuth: false,
        cities: [],
        people: [],
    },
    getters: {
        getCityById: state => id => {
            return state.cities.find(city => city.id == id);
        },
    },
    mutations: {
        setIsLoading: (state, val) => state.isLoading = val,
        setIsAuth: (state, val) => state.isAuth = val,
        setCities: (state, cities) => state.cities = cities,
        setPeople: (state, people) => state.people = people,
    },
    actions: {
        fetchFriendsVK: async (context) => {
            try {
                let response = await axios.get(URL_PATH_API_VK_FRIENDS);
                if (response.status == 200) {
                    context.commit('setPeople', response.data);
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
        authByCode: async (context, code) => {
            try {
                let response = await axios.post(URL_PATH_API_VK_AUTH + '?code=' + code);
                if (response.status == 200) {
                    context.commit('setIsAuth', true);
                    document.location = URL_ORIGIN;
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
        map: null,
        tileLayer: null,
        clusters: null,
    },
    mounted: function () {

        // move this logic to backend, rly...
        let vkOauthCode = getUrlParameter('code');
        let itsVkOAUthRedirect = vkOauthCode != null;
        if (itsVkOAUthRedirect) {
            this.authByCode(vkOauthCode);
            return;
        }

        let userAlreadyHaveSession = getCookie(COOKIE_NAME_SESSION_ID) != undefined;
        this.setIsAuth(userAlreadyHaveSession);

        this.initMap();
        this.fetchFriendsVk();

        this.setIsLoading(false);
    },
    computed: {
        vkOAuthURI: () => VK_URL_FULL_OAUTH,
        isLoading: () => store.state.isLoading,
        isAuth: () => store.state.isAuth,
        cities: () => store.state.cities,
        people: () => store.state.people,
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
            // cities updated event, let update markers
            let markers = [];
            let personInCityCounters = [];
            for (let person of this.people) {
                // get city coords
                if (person.cityId == -1) continue;
                let city = store.getters.getCityById(person.cityId);
                if (!city) continue;

                // icon with photo
                let markerIconWithPhoto = L.icon({
                    iconUrl: person.photoUri,
                    iconSize: [50, 50],
                    popupAnchor: [0, -30]
                });

                // if 2+ person live in same place, we need offset marker
                if (personInCityCounters[person.cityId] === undefined) {
                    personInCityCounters[person.cityId] = 0;
                }
                let num = personInCityCounters[person.cityId]++;
                let baseAngle = Math.PI * (1 + Math.sqrt(5)) * 6;
                let r = Math.sqrt(num) * 0.001;
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
    },
    methods: {
        setIsLoading: (val) => store.commit('setIsLoading', val),
        setIsAuth: (val) => store.commit('setIsAuth', val),
        authByCode: (code) => store.dispatch('authByCode', code),
        forgetMe: () => store.dispatch('forgetMe'),

        fetchFriendsVk: () => store.dispatch('fetchFriendsVK'),

        initMap: function () {
            this.map = L.map('map');
            this.map.setView([54.9884804, 73.3242361], 16);

            this.tileLayer = L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw', {
                maxZoom: 18,
                attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, <a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
                id: 'mapbox.streets'
            });
            this.tileLayer.addTo(this.map)

            this.clusters = L.markerClusterGroup({ maxClusterRadius: 20 });
            this.map.addLayer(this.clusters);
        },
        goTo: function (coords) {
            this.map.setView(coords, 16);
        }
    }
})