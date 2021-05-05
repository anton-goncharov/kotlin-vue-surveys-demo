import {authHeader, handleResponse} from '@/helper';

export const surveyResponseService = {
    find,
    getById,
    create,
    update,
    deleteById
}
function find(searchName, searchParams) {
    const requestOptions = {
        method: 'GET',
        headers: authHeader()
    };

    return fetch(`${process.env.VUE_APP_BACKEND_API_URL}/survey-responses/search/${searchName}?` +
        new URLSearchParams(searchParams), requestOptions).then(handleResponse);
}

function getById(id) {
    const requestOptions = {
        method: 'GET',
        headers: authHeader()
    };

    return fetch(`${process.env.VUE_APP_BACKEND_API_URL}/survey-responses/${id}`, requestOptions).then(handleResponse);
}

function create(surveyResponse) {
    const requestOptions = {
        method: 'POST',
        headers: { ...authHeader(), 'Content-Type': 'application/json' },
        body: JSON.stringify(surveyResponse)
    };

    return fetch(`${process.env.VUE_APP_BACKEND_API_URL}/survey-responses`, requestOptions).then(handleResponse);
}

function update(surveyResponse) {
    const requestOptions = {
        method: 'PUT',
        headers: { ...authHeader(), 'Content-Type': 'application/json' },
        body: JSON.stringify(surveyResponse)
    };

    return fetch(`${process.env.VUE_APP_BACKEND_API_URL}/survey-responses/${surveyResponse.uuid}`, requestOptions).then(handleResponse);
}

function deleteById(uuid) {
    const requestOptions = {
        method: 'DELETE',
        headers: authHeader()
    };

    return fetch(`${process.env.VUE_APP_BACKEND_API_URL}/survey-responses/${uuid}`, requestOptions).then(handleResponse);
}