<template>
  <div class="row">
    <div class="col">
    </div>
    <div v-if="this.survey && this.formData" class="col-lg-6 col-md-8 col-sm-10">
      <form class="container-fluid">
        <!-- Survey Title -->
        <div class="row">
          <div v-if="!isEditingTitle" class="col-md-auto">
            <h3>{{ this.survey.title }} <span v-if="isSurveySubmitted()" class="badge badge-success">Completed</span></h3>
          </div>
          <div v-else class="col-md-auto btn-toolbar">
            <input id="inlineFormInputGroup" v-model="cachedSurvey.title" class="form-control col-8" placeholder="Enter survey title here" type="text">
            <div class="input-group-append col-4">
              <div class="btn-group">
                <button class="btn btn-outline-primary" type="button" v-on:click="isEditingTitle = false; updateTitle()" value="text">Save</button>
                <button class="btn btn-outline-secondary" type="button" v-on:click="isEditingTitle = false; flushSurveyCache()" value="text">Cancel</button>
              </div>
            </div>
          </div>
          <div class="col-12">
            <button class="btn btn-link pl-0" type="button" v-if="!isEditingTitle" v-on:click="isEditingTitle = true">Rename</button>
            <button class="btn btn-link" type="button">Change Cover Image</button>
          </div>
        </div>

        <!-- Cover Image-->
        <div class="mt-2" style="width: 400px; height: 200px; background-image: url('https://images.unsplash.com/photo-1593642634315-48f5414c3ad9?ixid=MnwxMjA3fDF8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=2250&q=80'); background-size: cover">
        </div>

        <!-- Question -->
          <!-- TODO a control to reorder questions -->
          <div v-for="(question,qIndex) in this.surveyQuestions" v-bind:key="question.uuid" class="survey-question-block mt-5">
            <div v-if="!editing[question.uuid]">
              <div class="row">
                <div class="col-md-8">
                  <h5>{{ question.text }}</h5>
                </div>
                <div class="col-md-4">
                  <button class="btn btn-link" type="button" v-on:click="$set(editing, question.uuid, true)">Edit</button>
                  <button class="btn btn-link text-danger" type="button" v-on:click="deleteQuestion(question.uuid)">Delete</button>
                </div>
              </div>
              <div v-for="(choice,cIndex) in question.choices" v-bind:key="choice.uuid" class="form-check">
                <!-- Multiselect Question (checkboxes) -->
                <input v-if="question.multiselect === true"
                       :id="choice.uuid"
                       :name="question.uuid"
                       v-model="formData[qIndex].choices[cIndex].val"
                       :disabled="isSurveySubmitted()"
                       class="form-check-input" type="checkbox" value="true">
                <!-- Otherwise: Select-One Question (radio buttons) -->
                <input v-else
                       :id="choice.uuid"
                       :name="question.uuid"
                       v-model="formData[qIndex].choices[cIndex].val"
                       :disabled="isSurveySubmitted()"
                       class="form-check-input" type="radio" value="true">
                <label :for="choice.uuid" class="form-check-label">
                  {{ choice.text }}
                </label>
              </div>
            </div>
            <survey-question-edit v-else
                                  :question-supplier="() => question"
                                  @cancelled="editing[question.uuid] = false"
                                  @saved="saveQuestion" />
          </div>

        <!-- Add Question Form -->
        <div class="row">
          <button v-if="!isAddingNewQuestion"
                  class="btn btn-link mt-4" type="button"
                  v-on:click="isAddingNewQuestion = true">
            Add Question
          </button>
          <survey-question-edit v-else
                                :question-supplier="() => {return {choices: []}}"
                                @cancelled="isAddingNewQuestion = false"
                                @saved="saveQuestion" />
        </div>

        <!-- Survey Controls -->
        <div aria-label="Toolbar with button groups" class="btn-toolbar my-5" role="toolbar">
          <input class="btn btn-primary mr-2" value="Submit" type="submit"
                 @click.prevent="submitResponse(true)"
                 :disabled="isSurveySubmitted()">
          <input class="btn btn-warning mr-2" value="Save Draft" type="submit"
                 @click.prevent="submitResponse(false)"
                 :disabled="isSurveySubmitted()">
          <button class="btn btn-light mr-2" type="button" v-on:click="close()">Close</button>
          <button class="btn btn-outline-danger" type="button" v-on:click="deleteSurvey()">Delete Survey</button>
        </div>
      </form>
    </div>

    <!-- Loading Spinner   -->
    <div v-else class="col-lg-6 col-md-8 col-sm-10">
      <img src="data:image/gif;base64,R0lGODlhEAAQAPIAAP///wAAAMLCwkJCQgAAAGJiYoKCgpKSkiH/C05FVFNDQVBFMi4wAwEAAAAh/hpDcmVhdGVkIHdpdGggYWpheGxvYWQuaW5mbwAh+QQJCgAAACwAAAAAEAAQAAADMwi63P4wyklrE2MIOggZnAdOmGYJRbExwroUmcG2LmDEwnHQLVsYOd2mBzkYDAdKa+dIAAAh+QQJCgAAACwAAAAAEAAQAAADNAi63P5OjCEgG4QMu7DmikRxQlFUYDEZIGBMRVsaqHwctXXf7WEYB4Ag1xjihkMZsiUkKhIAIfkECQoAAAAsAAAAABAAEAAAAzYIujIjK8pByJDMlFYvBoVjHA70GU7xSUJhmKtwHPAKzLO9HMaoKwJZ7Rf8AYPDDzKpZBqfvwQAIfkECQoAAAAsAAAAABAAEAAAAzMIumIlK8oyhpHsnFZfhYumCYUhDAQxRIdhHBGqRoKw0R8DYlJd8z0fMDgsGo/IpHI5TAAAIfkECQoAAAAsAAAAABAAEAAAAzIIunInK0rnZBTwGPNMgQwmdsNgXGJUlIWEuR5oWUIpz8pAEAMe6TwfwyYsGo/IpFKSAAAh+QQJCgAAACwAAAAAEAAQAAADMwi6IMKQORfjdOe82p4wGccc4CEuQradylesojEMBgsUc2G7sDX3lQGBMLAJibufbSlKAAAh+QQJCgAAACwAAAAAEAAQAAADMgi63P7wCRHZnFVdmgHu2nFwlWCI3WGc3TSWhUFGxTAUkGCbtgENBMJAEJsxgMLWzpEAACH5BAkKAAAALAAAAAAQABAAAAMyCLrc/jDKSatlQtScKdceCAjDII7HcQ4EMTCpyrCuUBjCYRgHVtqlAiB1YhiCnlsRkAAAOwAAAAAAAAAAAA==" />
    </div>

    <div class="col">
    </div>
  </div>
</template>

<script>
import { mapState, mapActions } from 'vuex'
import {router} from "@/router";
import SurveyQuestionEdit from "@/components/SurveyQuestionEdit";

export default {
  name: 'Survey',
  components: {SurveyQuestionEdit},
  data: function() {
    return {
      isAddingNewQuestion: false,
      isEditingTitle: false,
      editing: {},
      cachedSurvey: null,
      formData: []
    }
  },
  props: {
    surveyUuid: String
  },
  created() {
    if (this.surveyUuid) {
      this.formData = this.surveyQuestions // default value
      const apiCalls = [
        this.getSurveyByIdApi(this.surveyUuid),
        this.findBySurveyForCurrentUserApi(this.surveyUuid)
      ];
      Promise.all(apiCalls).then(responses => {
        const survey = responses[0]
        this.initSurveyQuestions(survey.questions)
        // cache survey for draft form editing (user cancels and we should flush the model to initial state)
        this.cachedSurvey = Object.assign({}, survey);
      })
    } else {
      this.newSurvey()
      this.initSurveyQuestions([])
    }
  },
  computed: {
    ...mapState({
      account: state => state.account,
      survey: state => state.surveys.selected.item,
      surveyQuestions: state => state.surveyQuestions.all.items,
      surveyResponse: state => state.surveyResponses.selected.item
    })
  },
  watch: {
    surveyQuestions: function(val) {
      this.formData = this.mapResponseToForm(val, this.surveyResponse)
    }
  },
  methods: {
    ...mapActions('surveys', {
      newSurveyApi: 'newSurvey',
      getSurveyByIdApi: 'getById',
      createSurveyApi: 'create',
      updateSurveyApi: 'update',
      deleteSurveyApi: 'deleteById',
    }),
    ...mapActions('surveyQuestions', {
      initSurveyQuestions: 'init',
      createSurveyQuestionApi: 'create',
      updateSurveyQuestionApi: 'update',
      deleteSurveyQuestionApi: 'deleteById',
      flushSurveyQuestions: 'flush'
    }),
    ...mapActions('surveyResponses', {
      findBySurveyForCurrentUserApi: 'findBySurveyForCurrentUser',
      createSurveyResponseApi: 'create',
      updateSurveyResponseApi: 'update'
    }),
    updateTitle() {
      this.updateSurveyApi({ uuid: this.cachedSurvey.uuid, title: this.cachedSurvey.title })
    },
    deleteSurvey() {
      Promise.all([
        this.deleteSurveyApi(this.survey.uuid),
        this.flushSurveyQuestions()
      ]).then(
          () => router.push('/')
      )
    },
    flushSurveyCache() {
      this.cachedSurvey = Object.assign({}, this.survey);
    },
    close() {
      this.flushSurveyQuestions().then(
          () => router.back()
      )
    },
    newSurvey() {
      this.newSurveyApi(() => router.replace("/surveys/" + this.survey.uuid))
    },

    // QUESTIONS
    deleteQuestion(uuid) {
      this.deleteSurveyQuestionApi(uuid)
    },
    saveQuestion(question) {
      question.survey = this.survey._links.self.href
      if (question.uuid) {
        this.updateSurveyQuestionApi(question)
        this.editing[question.uuid] = false
      } else {
        question.pos = this.surveyQuestions?.length || 0
        this.createSurveyQuestionApi(question)
        this.isAddingNewQuestion = false
      }
    },

    // SURVEY RESPONSE
    mapResponseToForm(questions, response) {
      if (questions && response && response.choiceResponses) {
        // for each choice response in survey response
        for (const choiceResponse of response.choiceResponses) {
          // find question
          questions.filter(q => choiceResponse.question.uuid === q.uuid)
              .forEach(q => q.choices.filter(c => choiceResponse.choice.uuid === c.uuid)
                  .forEach(c => c.val = true)
              )
        }
      }
      return questions
    },
    submitResponse(isFinal) {
      // TODO validate input, all answers should be responded
      // got thru formData.questions
      // for each question create { question: [link] }
      // for each choice having "val=true", clone question and add {choice: uuid}
      const request = { submitted: isFinal, survey: this.survey._links.self.href, choiceResponses: [] }
      for (const question of this.formData) {
        for (const choice of question.choices) {
          if (choice.val) {
            const choiceResponse = {
              question: question._links.self.href,
              choice: { uuid: choice.uuid } }
            request.choiceResponses.push(choiceResponse)
          }
        }
      }
      this.createSurveyResponseApi(request);
      router.push('/');
    },
    isSurveySubmitted() {
      return this.surveyResponse && this.surveyResponse.submitted
    }
  }
}

</script>

<style>
</style>
