export function baseCrud(service, additions) {

    const crudState = {
        all: {},
        selected: {}
    };

    const crudActions = {
        getAll({ commit }, page) {
            commit('getAllRequest');
            service.getAll(page).then(
                    response => commit('getAllSuccess', response.data),
                    error => commit('getAllFailure', error)
                );
        },
        getById({ commit }, id) {
            commit('getByIdRequest');
            return service.getById(id).then(
                    response => {
                        commit('getByIdSuccess', response.data)
                        return response.data
                    },
                    error => commit('getByIdFailure', error)
                );
        },
        create({ commit }, data) {
            commit('createRequest')
            service.create(data).then(
                response => { commit('createSuccess', response.data) },
                error => commit('createFailure', error)
            );
        },
        update({ commit }, data) {
            commit('updateRequest')
            service.update(data).then(
                // eslint-disable-next-line no-unused-vars
                response => { commit('updateSuccess'), data },
                error => commit('updateFailure', error)
            );
        },
        deleteById({ commit }, uuid) {
            commit('deleteRequest')
            service.deleteById(uuid).then(
                    // eslint-disable-next-line no-unused-vars
                    response => { commit('deleteSuccess', uuid) },
                    error => commit('deleteFailure', error)
                );
        }
    }

    const crudMutations = {
        // --------- get mutations
        getAllRequest(state) {
            state.all = { loading: true };
        },
        getAllSuccess(state, items) {
            state.all = { items: items };
        },
        getAllFailure(state, error) {
            state.all = { error };
        },
        getByIdRequest(state) {
            state.selected = { loading: true };
        },
        getByIdSuccess(state, item) {
            state.selected = { item: item };
        },
        getByIdFailure(state, error) {
            state.selected = { error };
        },
        // --------- create mutations
        createRequest(state) {
            state.selected = { creating: true };
        },
        createSuccess(state, data) {
            state.selected = { item: data, creating: false };
            state.all.items.push(data)
        },
        createFailure(state, error) {
            state.selected = { error, creating: false };
        },
        // --------- update mutations
        updateRequest(state) {
            state.selected = { updating: true };
        },
        updateSuccess(state, data) {
            state.selected = { updating: false };
            const index = state.all.items.findIndex(q => q.uuid === data.uuid)
            state.all.items[index] = data
            // TODO update in ALL
        },
        updateFailure(state, error) {
            state.selected = { error, updating: false };
        },
        // --------- delete mutations
        deleteRequest(state) {
            state.selected = { deleting: true };
        },
        deleteSuccess(state, uuid) {
            state.selected = { deleting: false };
            const index = state.all.items.findIndex(q => q.uuid === uuid)
            state.all.items.splice(index, 1)
        },
        deleteFailure(state, error) {
            state.selected = { error, deleting: false };
        }
    }

    return {
        namespaced: true,
        state: {...crudState, ...additions.state},
        actions: {...crudActions, ...additions.actions},
        mutations: {...crudMutations, ...additions.mutations}
    }
}