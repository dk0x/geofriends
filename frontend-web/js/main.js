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
        const json = await response.json();
        console.log(json);
    } catch (err) {
        showMessage(err);
    }
}

async function main() {
    const vkOauthCode = getUrlParameter('code');
    if (vkOauthCode != null) {
        await authByCode(vkOauthCode);
        // document.location = HOST;
    } else {
        const cookie = getCookie("vkSessionId");
        if (cookie != undefined) {
            await fetchFriends();
        }
    }
}
main();

//                     let signIn = document.getElementById("sign-in").remove();
//                     let map = L.map('map');
//                     if (data.length > 0) {
//                         map.setView([54.9884804, 73.3242361], 13);
//                         L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw', {
//                             maxZoom: 18,
//                             attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, ' +
//                                 '<a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
//                                 'Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
//                             id: 'mapbox.streets'
//                         }).addTo(map);
//                         data.forEach(el => {
//                             L.marker([el.latitude, el.longitude])
//                                 .addTo(map)
//                                 .bindPopup('<p>' + el.firstName + ' ' + el.lastName + '</p>' +
//                                     '<img style="width: 50px;height: 50px;" src="' + el.photoUri + '">')
//                                 .openPopup();
//                         });

//                     }
//                 })
