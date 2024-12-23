// Import the http module to make HTTP requests. From this point, you can use `http` methods to make HTTP requests.
import http from 'k6/http';
import { faker } from '@faker-js/faker';

// Import the sleep function to introduce delays. From this point, you can use the `sleep` function to introduce delays in your test script.
import { check, sleep } from 'k6';

export const options = {
    iterations: 10,
}

const createBike = () => {
    const payload = JSON.stringify({
       	"name": `Bike ${faker.number.int()}`,
       	"description": faker.location.city()
    });

    const params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    const port = faker.helpers.arrayElement(['8080', '8081']);

    const res = http.post(`http://localhost:${port}/api/v1/bikes`, payload, params);

    check(res, { "Bike created": (res) => res.status === 202 });
}

const createUser = () => {
    const payload = JSON.stringify({
        "name": faker.person.fullName(),
        "email": faker.internet.email(),
        "document": faker.number.int({ min: 10000 })
    });

    const params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    const port = faker.helpers.arrayElement(['8080', '8081']);

    const res = http.post(`http://localhost:${port}/api/v1/users`, payload, params);

    check(res, { "Use Created": (res) => res.status === 202 });
}

const createRent = () => {
    const params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    const port = faker.helpers.arrayElement(['8080', '8081']);

    const userId = faker.helpers.arrayElement(http.get(`http://localhost:${port}/api/v1/users`, null, params).json()).id;

    const bikeId = faker.helpers.arrayElement(http.get(`http://localhost:${port}/api/v1/bikes`, null, params).json()).id;

    const res = http.post(`http://localhost:${port}/api/v1/users/${userId}/bikes/${bikeId}/rent`, null, params);

    check(res, { "Rent created": (res) => res.status === 202 });
}

export default function() {
    createBike();

    createUser();

    createRent();

    sleep(1);
}