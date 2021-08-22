import {authHeader, handleResponse} from '@/helper';

export const tagService = {
    getAll,
    create
};

function getAll() {
    const requestOptions = {
        method: 'GET',
        headers: authHeader()
    };

    return fetch(`${process.env.VUE_APP_BACKEND_API_URL}/tags`, requestOptions).then(handleResponse);
}

function create(tag) {
    const requestOptions = {
        method: 'POST',
        headers: { ...authHeader(), 'Content-Type': 'application/json' },
        body: JSON.stringify(tag)
    };

    return fetch(`${process.env.VUE_APP_BACKEND_API_URL}/tags`, requestOptions).then(handleResponse);
}