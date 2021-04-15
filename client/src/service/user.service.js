import { authHeader } from '@/helper';

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
        .then(token => {
            // login successful if there's a jwt token in the response
            const user = {}
            console.log(token)
            if (token) {
                user.token = token
                // store user details and jwt token in local storage to keep user logged in between page refreshes
                localStorage.setItem('user', JSON.stringify(user));
            }

            return user;
        });
}

function logout() {
    // remove user from local storage to log user out
    localStorage.removeItem('user');
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

function handleResponse(response) {
    return response.text().then(text => {
        const data = response.headers.get("Authorization")
        console.log("header", data)
        console.log("response", response)
        // const data = text && JSON.parse(text);
        if (!response.ok) {
            if (response.status === 401) {
                // auto logout if 401 response returned from api
                logout();
                location.reload(true);
            }
            // const error = (data && data.message) || response.statusText;
            return Promise.reject(text);
        }

        return data;
    });
}
