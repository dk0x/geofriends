const COOKIE_NAME_VK_SESSION_ID = "vkSessionId";
const HOST = location.protocol + '//' + location.host;
const API_URL = HOST + '/api';


function showLoading() {
    document.getElementById("loadingIndicator").style.display = '';
}
function hideLoading() {
    document.getElementById("loadingIndicator").style.display = "none";
}

function showSignInButton() {
    let host = location.host;
    if (host.split(':').length === 1)
        host = host + ':80';
    let vkOauthUri = 'https://oauth.vk.com/authorize' +
        '?client_id=7087056&display=page&scope=friends&response_type=code&v=5.101' +
        '&redirect_uri=' + location.protocol + '//' + host;
    document.getElementById('signInButton').setAttribute('href', vkOauthUri);
    document.getElementById('signInWrapper').style.display = '';
}
function hideSignInButton() {
    document.getElementById('signInWrapper').style.display = 'none';
}



function getCookie(name) {
    const matches = document.cookie.match(new RegExp(
        "(?:^|; )" + name.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, '\\$1') + "=([^;]*)"
    ));
    return matches ? decodeURIComponent(matches[1]) : undefined;
}

function getUrlParameter(name) {
    const splitedUrlArray = location.href.split('?');
    const params = new URLSearchParams(splitedUrlArray[1]);
    return params.get(name);
}

async function authByCode(code) {
    let response = await fetch(API_URL + '/vk/auth?code=' + code, {
        method: 'POST'
    });
    if (response.status != 200)
        throw await response.json();
}

async function fetchFriends() {
    let response = await fetch(API_URL + '/vk/friends');
    if (response.status != 200)
        throw await response.json();
    return await response.json();
}

function extractCitiesFromFriends(friends) {
    const citiesMap = friends.reduce((map, friend) => {
        if (friend.cityId != -1) {
            map[friend.cityId] = {
                id: friend.cityId,
                name: friend.city,
                country: friend.country,
                latitude: 0,
                longitude: 0
            };
        }
        return map;
    }, {});
    return Object.values(citiesMap);
}

async function geocodeCities(cities) {
    let response = await fetch(API_URL + '/geo/geocode', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(cities),
    });
    if (response.status != 200)
        throw await response.json();
    return await response.json();
}

function getCityById(cities, id) {
    for (let i = 0; i < cities.length; i++) {
        const city = cities[i];
        if (city.id === id)
            return city;
    }
}


let cityIdSamePlaceCounters = [];
function getOffsetForCoordByCityId(id) {
    if (cityIdSamePlaceCounters[id] === undefined) {
        cityIdSamePlaceCounters[id] = 0;
    }
    cityIdSamePlaceCounters[id]++;

    let counter = cityIdSamePlaceCounters[id];
    let radius = counter * 0.001;
    let placesInCircle = 360 / 10 - 1;
    let placeInCircle = counter;
    let deg = placeInCircle * 10;
    let rad = deg * (Math.PI / 180);

    let x = Math.sin(rad) * radius;
    let y = Math.cos(rad) * radius;

    return { x, y };
}

function showFriendsOnMap(friends, cities) {
    let map = L.map('map');
    map.setView([54.9884804, 73.3242361], 13);
    L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw', {
        maxZoom: 18,
        attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, <a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
        id: 'mapbox.streets'
    }).addTo(map);

    let clusterGroup = L.markerClusterGroup();

    friends.forEach(friend => {
        if (friend.cityId != -1) {
            let city = getCityById(cities, friend.cityId);
            let offsets = getOffsetForCoordByCityId(city.id);

            let markerIconWithPhoto = L.icon({
                iconUrl: friend.photoUri,
                iconSize: [50, 50],
                popupAnchor: [0, -20]
            });

            let marker = L.marker(
                [city.latitude + offsets.y * 0.65, city.longitude + offsets.x],
                { icon: markerIconWithPhoto });
            marker.bindPopup(friend.firstName + ' ' + friend.lastName);
            // marker.addTo(map);
            clusterGroup.addLayer(marker);
        }
    });
    map.addLayer(clusterGroup);
}

function clearSession() {
    document.cookie = COOKIE_NAME_VK_SESSION_ID + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT;";
}

async function forgetMe() {
    showLoading();
    let response = await fetch(API_URL + '/vk/forgetMe', {
        method: 'DELETE'
    });
    if (response.status != 200)
        throw await response.json();
    clearSession();
    location.reload();
}

async function main() {
    try {
        hideLoading();
        hideSignInButton();

        let vkOauthCode = getUrlParameter('code');
        let itsVkRedirect = vkOauthCode != null;
        if (itsVkRedirect) {
            showLoading();
            await authByCode(vkOauthCode);
            hideLoading();
            document.location = HOST;
            return;
        }

        let userAlreadyHaveSession = getCookie(COOKIE_NAME_VK_SESSION_ID) != undefined;
        if (userAlreadyHaveSession) {
            showLoading();
            let friends = await fetchFriends();
            let cities = extractCitiesFromFriends(friends);
            let geocodedCities = await geocodeCities(cities);
            showFriendsOnMap(friends, geocodedCities);
            hideLoading();
            return;
        }

        // otherwise show sign in button
        showSignInButton();
    } catch (err) {
        switch (err.errorCode) {
            case 'VK_AUTH_EXPIRED', 'SESSION_EXPIRED': {
                clearSession();
                location.reload();
            } break;
            case 'NOT_PROPERLY_HANDLED_EXCEPTION_CATCHED': {
                // display msg like 'Please send to support this error token'
                console.log(err.description);
            } break;
            default: {
                console.log(err);
            }
        }
    }
}
main();

