export function baseCrud(service, additions) {

    const crudState = {
        all: {},
        selected: {}
    };

    const crudActions = {
        getAll({ commit }, page) {
            commit('getAllRequest');

            service.getAll(page)
                .then(
                    response => commit('getAllSuccess', response.data),
                    error => commit('getAllFailure', error)
                );
        },
        getById({ commit }, id) {
            commit('getByIdRequest');

            service.getById(id)
                .then(
                    response => commit('getByIdSuccess', response.data),
                    error => commit('getByIdFailure', error)
                );
        }
        // create({ commit }, object) {
        //     commit('createRequest');
        //     console.log("create", object); // TODO delete before commit
        // },
        // update({ commit }, object) {
        //     commit('updateRequest');
        //     console.log("update", object); // TODO delete before commit
        // },
        // delete({ commit }, id) {
        //     commit('deleteRequest');
        //     console.log("delete", id); // TODO delete before commit
        // }
    }

    const crudMutations = {
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
        }
    }

    return {
        namespaced: true,
        state: {...crudState, ...additions.state},
        actions: {...crudActions, ...additions.actions},
        mutations: {...crudMutations, ...additions.mutations}
    }
}