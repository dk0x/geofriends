
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
        friendsVK: [],
        subsVK: [],
    },
    getters: {},
    mutations: {
        setIsLoading: (state, val) => state.isLoading = val,
        setIsAuth: (state, val) => state.isAuth = val,
        setFriendsVK: (state, peoples) => state.friendsVK = peoples,
        setCities: (state, cities) => state.cities = cities,
    },
    actions: {
        fetchFriendsVK: async (context) => {
            try {
                let response = await axios.get(URL_PATH_API_VK_FRIENDS);
                if (response.status == 200) {
                    context.commit('setFriendsVK', response.data);
                } else {
                    console.log(response);
                }
            } catch (err) {
                console.log(err);
            }
        },
        geocodeCities: async (context, cities) => {
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
        clusterGroup: null,
        layers: [
            {
                id: 0,
                name: 'friendsVK',
                active: false,
                features: [],
            },
        ],
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
        this.initLayers();

        this.setIsLoading(false);
    },
    computed: {
        vkOAuthURI: () => VK_URL_FULL_OAUTH,
        isLoading: () => store.state.isLoading,
        isAuth: () => store.state.isAuth,
        friendsVK: () => store.state.friendsVK,
        cities: () => store.state.cities,
    },
    methods: {
        setIsLoading: (val) => store.commit('setIsLoading', val),
        setIsAuth: (val) => store.commit('setIsAuth', val),
        authByCode: (code) => store.dispatch('authByCode', code),
        forgetMe: () => store.dispatch('forgetMe'),

        initMap: function () {
            this.map = L.map('map');
            this.map.setView([54.9884804, 73.3242361], 12);

            this.tileLayer = L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw', {
                maxZoom: 18,
                attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, <a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
                id: 'mapbox.streets'
            });
            this.tileLayer.addTo(this.map)

            this.clusterGroup = L.markerClusterGroup();
        },
        initLayers: function () { },
    }
})