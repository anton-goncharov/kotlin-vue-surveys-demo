import {authHeader, handleResponse} from '@/helper';

export const surveyQuestionService = {
    getById,
    create,
    update,
    deleteById
};

function getById(id) {
    const requestOptions = {
        method: 'GET',
        headers: authHeader()
    };

    return fetch(`${process.env.VUE_APP_BACKEND_API_URL}/questions/${id}`, requestOptions).then(handleResponse);
}

function create(question) {
    console.log("create question", question); // TODO delete before commit
    const requestOptions = {
        method: 'POST',
        headers: { ...authHeader(), 'Content-Type': 'application/json' },
        body: JSON.stringify(question)
    };

    return fetch(`${process.env.VUE_APP_BACKEND_API_URL}/questions`, requestOptions).then(handleResponse);
}

function update(question) {
    console.log("update question", question); // TODO delete before commit
    const requestOptions = {
        method: 'POST',
        headers: { ...authHeader(), 'Content-Type': 'application/json' },
        body: JSON.stringify(question)
    };

    return fetch(`${process.env.VUE_APP_BACKEND_API_URL}/questions/${question.uuid}`, requestOptions).then(handleResponse);
}

function deleteById(uuid) {
    console.log("delete question", uuid); // TODO delete before commit
    const requestOptions = {
        method: 'DELETE',
        headers: authHeader()
    };

    return fetch(`${process.env.VUE_APP_BACKEND_API_URL}/questions/${uuid}`, requestOptions).then(handleResponse);
}