import { authHeader } from '@/helper';
import { handleResponse } from '@/helper';

export const userService = {
    login,
    logout,
    register,
    getById
};

function login(email, password) {
    const requestOptions = {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password })
    };

    return fetch(`${process.env.VUE_APP_BACKEND_API_URL}/public/login`, requestOptions)
        .then(handleResponse)
        .then(payload => {
            // login successful if there's a jwt token in the response
            if (payload && payload.data) {
                // store user details and jwt token in local storage to keep user logged in between page refreshes
                localStorage.setItem('user', JSON.stringify(payload.data));
                const token = payload.headers.get("Authorization");
                localStorage.setItem('authToken', token);
            }
            return payload.data;
        });
}

function logout() {
    // remove user from local storage to log user out
    localStorage.removeItem('user');
    localStorage.removeItem('authToken');
}

function register(user) {
    const requestOptions = {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(user)
    };

    return fetch(`${process.env.VUE_APP_BACKEND_API_URL}/public/register`, requestOptions).then(handleResponse);
}

function getById(id) {
    const requestOptions = {
        method: 'GET',
        headers: authHeader()
    };

    return fetch(`${process.env.VUE_APP_BACKEND_API_URL}/users/${id}`, requestOptions).then(handleResponse);
}
