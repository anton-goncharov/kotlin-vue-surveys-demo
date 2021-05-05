import { surveyService } from '@/service';
import { baseCrud } from './base-crud.module';

const state = {
};

const actions = {
    newSurvey({ commit }, callback) {
        surveyService.create({ title: "New Survey" })
            .then(
                response => {
                    commit('newSurveySuccess', response)
                    callback()
                },
                error => commit('newSurveyFailure', error)
            );
    },
    updatePartially({ commit }, data) {
        commit('updateRequest')
        surveyService.patch(data).then(
            // eslint-disable-next-line no-unused-vars
            response => { commit('updateSuccess'), data },
            error => commit('updateFailure', error)
        )
    },
    deleteQuestionById({ commit }, questionUuid) {
        surveyService.deleteQuestionById(questionUuid)
            .then(
                // eslint-disable-next-line no-unused-vars
                response => commit('deleteQuestionSuccess', questionUuid),
                error => commit('deleteQuestionFailure', error)
            );
    }
}

const mutations = {
    newSurveySuccess(state, created) {
        state.selected = {}
        state.selected.item = created.data
    },
    newSurveyFailure() {
        // TODO redirect back to main page with some error alert
    },
    deleteQuestionSuccess(state, questionUuid) {
        // remove question from survey's questions array
        const index = state.selected.item.questions.findIndex(q => q.uuid === questionUuid)
        state.selected.item.questions.splice(index, 1)
    },
    deleteQuestionFailure() {
    }
}

export const surveys = baseCrud(surveyService, {
    namespaced: true,
    state,
    actions,
    mutations
});