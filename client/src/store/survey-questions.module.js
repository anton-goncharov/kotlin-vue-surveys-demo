import { surveyQuestionService } from '@/service';
import { baseCrud } from './base-crud.module';

const state = {
};

const actions = {
    init({ commit }, list) {
        commit('populateFromList', list)
    }
}

const mutations = {
    populateFromList(state, list) {
        state.all = {items: list}
    }
}

export const surveyQuestions = baseCrud(surveyQuestionService, {
    namespaced: true,
    state,
    actions,
    mutations
});