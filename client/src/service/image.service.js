import { authHeader } from '@/helper';
import { handleResponse } from '@/helper';

export const imageService = {
    upload
};

function upload(file) {
    const formData = new FormData();
    formData.append("file", file, file.name)

    const requestOptions = {
        method: 'POST',
        headers: authHeader(),
        body: formData
    };

    return fetch(`${process.env.VUE_APP_BACKEND_API_URL}/images`, requestOptions)
        .then(handleResponse)
        .then(payload => {
            if (payload) {
                // get uploaded image location (relative URL w/o host)
                return payload.headers.get("Location");
            }
        });
}
