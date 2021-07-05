import { userService } from '@/service';

export function handleResponse(response) {
    return response.text().then(text => {
        const data = text && JSON.parse(text);
        if (!response.ok) {
            if (response.status === 401) {
                // auto logout if 401 response returned from api
                userService.logout();
                location.reload(true);
            }
            // const error = (data && data.message) || response.statusText;
            return Promise.reject(text);
        }
        // console.log("data", data); // crude debug
        return {
            headers: response.headers,
            data: data
        };
    });
}
