import { surveyService } from '@/service';
import { baseCrud } from './base-crud.module';
import {imageService} from "../service";

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
    setSurveyImage({ commit }, file, contentType) {
        return imageService.upload(file, contentType)
            .then(
                response => {
                    commit('surveyImageUploadSuccess', response)
                    return response
                },
                error => commit('surveyImageUploadFailure', error)
            );
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
    },
    surveyImageUploadSuccess(state, imageUrl) {
        state.selected.item.imageUrl = imageUrl
    },
    surveyImageUploadFailure() {
    }
}

export const surveys = baseCrud(surveyService, {
    namespaced: true,
    state,
    actions,
    mutations
});