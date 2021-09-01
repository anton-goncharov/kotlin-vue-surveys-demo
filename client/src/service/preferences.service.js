import {authHeader, handleResponse} from '@/helper';

export const preferencesService = {
    getAll,
    save
};

function getAll() {
    const requestOptions = {
        method: 'GET',
        headers: authHeader()
    };

    return fetch(`${process.env.VUE_APP_BACKEND_API_URL}/preferences`, requestOptions).then(handleResponse);
}

function save(preferences) {
    const requestOptions = {
        method: 'POST',
        headers: { ...authHeader(), 'Content-Type': 'application/json' },
        body: JSON.stringify(preferences)
    };

    return fetch(`${process.env.VUE_APP_BACKEND_API_URL}/preferences`, requestOptions).then(handleResponse);
}