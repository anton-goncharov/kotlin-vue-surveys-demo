export function baseCrud(service, additions) {

    const crudState = {
        all: {},
        selected: {}
    }

    const crudActions = {
        getAll({ commit }, page) {
            commit('getAllRequest')
            return service.getAll(page).then(
                    response => {
                        commit('getAllSuccess', response.data)
                        return response.data
                    },
                    error => commit('getAllFailure', error)
                )
        },
        getById({ commit }, id) {
            commit('getByIdRequest')
            return service.getById(id).then(
                    response => {
                        commit('getByIdSuccess', response.data)
                        return response.data
                    },
                    error => commit('getByIdFailure', error)
                )
        },
        create({ commit }, data) {
            commit('createRequest')
            return service.create(data).then(
                response => {
                    commit('createSuccess', response.data)
                    return response.data
                },
                error => commit('createFailure', error)
            )
        },
        update({ commit }, data) {
            commit('updateRequest')
            service.update(data).then(
                // eslint-disable-next-line no-unused-vars
                () => { commit('updateSuccess', data) },
                error => commit('updateFailure', error)
            )
        },
        deleteById({ commit }, uuid) {
            commit('deleteRequest')
            return service.deleteById(uuid).then(
                    // eslint-disable-next-line no-unused-vars
                    response => commit('deleteSuccess', uuid),
                    error => commit('deleteFailure', error)
                )
        },
        flush({ commit }) {
            return commit('flush')
        }
     }

    const crudMutations = {
        // --------- get mutations
        getAllRequest(state) {
            state.all = { loading: true }
        },
        getAllSuccess(state, items) {
            // The 'items' can be an object having '_embedded' prop with page number, num of pages etc.
            // It's important to save all this data, but at the same time to unify access
            // to the nested array.
            const embedded = items._embedded
            if (embedded) {
                state.all = {
                    items: embedded[Object.keys(embedded)[0]],
                    page: items.page
                }
            } else {
                state.all = { items: items }
            }
        },
        getAllFailure(state, error) {
            state.all = { error }
        },
        getByIdRequest(state) {
            state.selected = { loading: true }
        },
        getByIdSuccess(state, item) {
            state.selected = { item: item }
        },
        getByIdFailure(state, error) {
            state.selected = { error }
        },
        // --------- create mutations
        createRequest(state) {
            state.selected = { creating: true }
        },
        createSuccess(state, data) {
            state.selected = { item: data, creating: false }
            if (state.all && state.all.items) {
                state.all.items.push(data)
            }
        },
        createFailure(state, error) {
            state.selected = { error, creating: false }
        },
        // --------- update mutations
        updateRequest(state) {
            state.selected.updating = true
        },
        updateSuccess(state, data) {
            state.selected.updating = false
            state.selected.item = Object.assign(state.selected.item, data)
            if (state.all && state.all.items) {
                const index = state.all.items.findIndex(q => q.uuid === data.uuid)
                state.all.items[index] = Object.assign(state.all.items[index], data)
            }
        },
        updateFailure(state, error) {
            state.selected = { error, updating: false }
        },
        // --------- delete mutations
        deleteRequest(state) {
            state.selected = { deleting: true }
        },
        deleteSuccess(state, uuid) {
            state.selected = { deleting: false }
            if (state.all && state.all.items) {
                const index = state.all.items.findIndex(q => q.uuid === uuid)
                state.all.items.splice(index, 1)
            }
        },
        deleteFailure(state, error) {
            state.selected = { error, deleting: false }
        },
        flush(state) {
            state.all = {}
            state.selected= {}
        }
    }

    return {
        namespaced: true,
        state: {...crudState, ...additions.state},
        actions: {...crudActions, ...additions.actions},
        mutations: {...crudMutations, ...additions.mutations}
    }
}