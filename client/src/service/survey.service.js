import {authHeader, handleResponse} from '@/helper';
import {uiConstants} from "@/ui.constants";

export const surveyService = {
    getAll,
    count,
    getById,
    create,
    update,
    updateTags,
    deleteById
};

function getAll(params, page) {
    const requestOptions = {
        method: 'GET',
        headers: authHeader()
    };

    if (params) {
        return fetch(`${process.env.VUE_APP_BACKEND_API_URL}/surveys/search?` + new URLSearchParams({
            ...params,
            size: uiConstants.ELEMENTS_ON_PAGE,
            page: page || 0
        }), requestOptions).then(handleResponse);
    } else {
        return fetch(`${process.env.VUE_APP_BACKEND_API_URL}/surveys?` + new URLSearchParams({
            size: uiConstants.ELEMENTS_ON_PAGE,
            page: page || 0
        }), requestOptions).then(handleResponse);
    }
}

function count(params, page) {
    const requestOptions = {
        method: 'GET',
        headers: authHeader()
    };
    return fetch(`${process.env.VUE_APP_BACKEND_API_URL}/surveys/count?` + new URLSearchParams({
        ...params,
        size: uiConstants.ELEMENTS_ON_PAGE,
        page: page || 0
    }), requestOptions).then(handleResponse);
}

function getById(id) {
    const requestOptions = {
        method: 'GET',
        headers: authHeader()
    };

    return fetch(`${process.env.VUE_APP_BACKEND_API_URL}/surveys/${id}?` + new URLSearchParams({
        projection: "surveyWithQuestions"
    }), requestOptions).then(handleResponse);
}

function create(survey) {
    const requestOptions = {
        method: 'POST',
        headers: { ...authHeader(), 'Content-Type': 'application/json' },
        body: JSON.stringify(survey)
    };

    return fetch(`${process.env.VUE_APP_BACKEND_API_URL}/surveys`, requestOptions).then(handleResponse);
}

function update(survey) {
    const requestOptions = {
        method: 'PUT',
        headers: { ...authHeader(), 'Content-Type': 'application/json' },
        body: JSON.stringify(survey)
    };

    return fetch(`${process.env.VUE_APP_BACKEND_API_URL}/surveys/${survey.uuid}`, requestOptions).then(handleResponse);
}

function updateTags(surveyUuid, tags) {
    const requestOptions = {
        method: 'POST',
        headers: { ...authHeader(), 'Content-Type': 'application/json' },
        body: JSON.stringify(tags)
    };

    return fetch(`${process.env.VUE_APP_BACKEND_API_URL}/surveys/${surveyUuid}/tags`, requestOptions).then(handleResponse);
}

function deleteById(id) {
    const requestOptions = {
        method: 'DELETE',
        headers: authHeader()
    };

    return fetch(`${process.env.VUE_APP_BACKEND_API_URL}/surveys/${id}`, requestOptions).then(handleResponse);
}