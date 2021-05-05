import Vue from 'vue';
import Vuex from 'vuex';

import { alert } from './alert.module';
import { account } from './account.module';
import { users } from './users.module';
import { surveys } from './surveys.module';
import { surveyQuestions } from './survey-questions.module';
import { surveyResponses } from './survey-responses.module';

Vue.use(Vuex);

export const store = new Vuex.Store({
    modules: {
        alert,
        account,
        surveys,
        surveyQuestions,
        surveyResponses,
        users
    }
});