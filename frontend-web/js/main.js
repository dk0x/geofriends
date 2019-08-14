const HOST = location.protocol + '//' + location.host;
const API_URL = HOST + '/api';

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

function showMessage(msg, timeout) {
    console.log(msg);
    // TODO: realize popup with timeout
}

async function authByCode(code) {
    try {
        await fetch(API_URL + '/vk/auth?' + 'code=' + code, { method: 'POST' });
    } catch (err) {
        showMessage(err, 5);
    }
}

async function fetchFriends() {
    try {
        const response = await fetch(API_URL + '/vk/friends');
        return await response.json();
    } catch (err) {
        showMessage(err);
    }
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
    try {
        const response = await fetch(API_URL + '/geo/geocode', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(cities),
        });
        return await response.json();
    } catch (err) {
        showMessage(err);
    }
}

function getCityById(cities, id) {
    for (let i = 0; i < cities.length; i++) {
        const city = cities[i];
        if (city.id === id)
            return city;
    }
}

function showFriendsOnMap(friends, cities) {
    let signIn = document.getElementById("sign-in").remove();
    let map = L.map('map');
    map.setView([54.9884804, 73.3242361], 13);
    L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw', {
        maxZoom: 18,
        attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, ' +
            '<a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
            'Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
        id: 'mapbox.streets'
    }).addTo(map);

    friends.forEach(friend => {
        if (friend.cityId != -1) {
            const city = getCityById(cities, friend.cityId);
            L.marker([city.latitude, city.longitude])
                .addTo(map)
                .bindPopup('<p>' + friend.firstName + ' ' + friend.lastName + '</p>' +
                    '<img style="width: 50px;height: 50px;" src="' + friend.photoUri + '">')
                .openPopup();
        }
    });
}

async function main() {
    const vkOauthCode = getUrlParameter('code');
    if (vkOauthCode != null) {
        await authByCode(vkOauthCode);
        document.location = HOST;
    } else {
        if (getCookie("vkSessionId") != undefined) {
            const friends = await fetchFriends();
            const cities = extractCitiesFromFriends(friends);
            const geocodedCities = await geocodeCities(cities);
            showFriendsOnMap(friends, geocodedCities);
        }
    }
}
main();

