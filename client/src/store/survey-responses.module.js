import { surveyResponseService } from '@/service';
import { baseCrud } from './base-crud.module';

const state = {
}

const actions = {
    findBySurveyForCurrentUser({ commit }, surveyUuid) {
        commit('findOneRequest')
        return surveyResponseService.find('findBySurveyForCurrentUser',
            {surveyUuid: surveyUuid, projection: 'surveyResponseBrief'}).then(
            response => {
                commit('findOneSuccess', response.data)
                return response.data
            },
            error => commit('findOneFailure', error)
        )
    }
}

const mutations = {
    findOneRequest(state) {
        state.selected = { loading: true }
    },
    findOneSuccess(state, data) {
        state.selected = { item: data, loading: false }
    },
    findOneFailure(state, error) {
        state.selected = { item: {}, error, loading: false }
    }
}

export const surveyResponses = baseCrud(surveyResponseService, {
    namespaced: true,
    state,
    actions,
    mutations
});