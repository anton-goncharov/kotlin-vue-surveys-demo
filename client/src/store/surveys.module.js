import { surveyService } from '@/service';
import { imageService } from '@/service';
import { baseCrud } from './base-crud.module';

const state = {
    byTag: [],
    all: {},
    searchQuery: {}
};

const actions = {
    setSearchQuery({ commit }, query) {
        commit('setSearchQuery', query)
    },
    // eslint-disable-next-line no-unused-vars
    count({ commit }, params) {
        return surveyService.count(params).then(
            response => response.headers.get('X-Total-Count') || '',
            () => {} // error, whatever, it's just count
        )
    },
    getAllByTag({ commit, state }, params, page) {
        surveyService.getAll({tag: params.tag, ...state.searchQuery}, page).then(
            response =>
                commit('getAllByTagSuccess', {
                    tag: params.tag,
                    order: params.order,
                    pageable: response.data,
                }),
            error => commit('getAllFailure', error)
        )
    },
    newSurvey({ commit }) {
        return surveyService.create({ title: "New Survey" })
            .then(
                response => commit('newSurveySuccess', response),
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
    setSearchQuery(state, query) {
        state.searchQuery = query
    },
    getAllByTagSuccess(state, data) {
        // remove prev data
        const toRemove = state.byTag.indexOf(state.byTag.find(bt => bt.tag === data.tag));
        if (toRemove > -1) {
            state.byTag.splice(toRemove, 1);
        }
        // push new array
        if (data.pageable) {
            state.byTag.push({
                tag: data.tag,
                order: data.order,
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
        if (state.all && state.all.items) {
            state.all.items.push(created.data)
        }
        if (state.byTag) {
            const untagged = state.byTag.find(bt => bt.tag === 'none')
            if (untagged) {
                untagged.items._embedded.surveys.push(created.data)
            }
        }
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