/*
    Auth header is a helper function that returns an HTTP Authorization header containing the JSON Web Token (JWT)
    of the currently logged in user from local storage. If the user isn't logged in an empty object is returned.
 */
export function authHeader() {
    let user = JSON.parse(localStorage.getItem('user'));

    if (user && user.token) {
        return { 'Authorization': 'Bearer ' + user.token };
    } else {
        return {};
    }
}