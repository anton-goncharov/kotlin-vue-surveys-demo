import {authHeader, handleResponse} from '@/helper';
import {uiConstants} from "@/ui.constants";

export const surveyService = {
    getAll,
    getById,
    create,
    deleteQuestionById
};

function getAll(page) {
    const requestOptions = {
        method: 'GET',
        headers: authHeader()
    };

    return fetch(`${process.env.VUE_APP_BACKEND_API_URL}/surveys?` + new URLSearchParams({
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
    console.log("survey", survey); // TODO delete before commit
    const requestOptions = {
        method: 'POST',
        headers: { ...authHeader(), 'Content-Type': 'application/json' },
        body: JSON.stringify(survey)
    };

    return fetch(`${process.env.VUE_APP_BACKEND_API_URL}/surveys`, requestOptions).then(handleResponse);
}

function deleteQuestionById(id) {
    const requestOptions = {
        method: 'DELETE',
        headers: authHeader()
    };

    return fetch(`${process.env.VUE_APP_BACKEND_API_URL}/questions/${id}`, requestOptions).then(handleResponse);
}