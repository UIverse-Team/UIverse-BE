import http from 'k6/http';
import {check, sleep} from 'k6';

export const options = {
    vus: 1000,
    duration: '1m',
};

function buildQueryString(params) {
    const parts = [];

    for (const key in params) {
        if (Array.isArray(params[key])) {
            params[key].forEach(value => {
                parts.push(encodeURIComponent(key) + '=' + encodeURIComponent(value));
            });
        } else {
            parts.push(encodeURIComponent(key) + '=' + encodeURIComponent(params[key]));
        }
    }

    return parts.join('&');
}

export default function () {
    const url = 'http://host.docker.internal:8080/products';

    const params = {
        page: 0,
        size: 12,
        sort: 'wish',
        keyword: '티셔츠',
        categoryId: 50000000,
        priceRanges: [0, 25000, 50000, 100000],
        ratings: [1, 2, 3, 4, 5]
    };

    const queryString = buildQueryString(params);

    const res = http.get(`${url}?${queryString}`);

    check(res, {
        "is status 200": (r) => r.status === 200
    });

    sleep(1);
}