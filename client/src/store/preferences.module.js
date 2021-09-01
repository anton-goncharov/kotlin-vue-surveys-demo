import { baseCrud } from './base-crud.module';
import {preferencesService} from "@/service/preferences.service";

const state = {
    all: {
        items: {}
    }
};

const actions = {
    save({ commit }, prefs) {
        return preferencesService.save(prefs)
            .then(
                response => commit('savePreferencesSuccess', response),
                error => commit('savePreferencesFailure', error)
            );
    }
}

const mutations = {
    savePreferencesSuccess(state, data) {
        state.data = data;
    },
    savePreferencesFailure(state) {
        state.hasError = true;
    }
}

export const preferences = baseCrud(preferencesService, {
    namespaced: true,
    state,
    actions,
    mutations
});