import { surveyService } from '@/service';
import { baseCrud } from './base-crud.module';
import {imageService} from "../service";

const state = {
    byTag: [],
    all: {}
};

const actions = {
    getAllByTag({ commit }, params, page) {
        console.log("searchParams", params); // TODO delete before commit
        // commit('getAllByTag')
        surveyService.getAll({tag: params.tag, ...params.searchParams}, page).then(
            response =>
                commit('getAllByTagSuccess', {
                    tag: params.tag,
                    pageable: response.data,
                }),
            error => commit('getAllFailure', error)
        )
    },
    getAllUntagged({ commit }, page) {
        // commit('getAllUntagged')
        surveyService.getAll({tag: 'untagged'}, page).then(
            response => commit('getAllUntaggedSuccess', response.data),
            error => commit('getAllFailure', error)
        )
    },
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
    updateTags({ commit }, data) {
        surveyService.updateTags(data.surveyUuid, data.tags)
            .then(
                // eslint-disable-next-line no-unused-vars
                () => { commit('updateSuccess', data) },
                error => commit('updateFailure', error)
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
    getAllByTagSuccess(state, data) {
        if (data.pageable) {
            state.byTag.push({
                tag: data.tag,
                items: data.pageable
            })
        }
    },
    getAllUntaggedSuccess(state, items) {
        const embedded = items._embedded
        state.untagged = {
            items: embedded[Object.keys(embedded)[0]],
            page: items.page
        }
    },
    newSurveySuccess(state, created) {
        state.selected = {}
        state.selected.item = created.data
        state.all.items.push(created.data)
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