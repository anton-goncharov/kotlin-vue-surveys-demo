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
                <button class="btn btn-outline-secondary" type="button" v-on:click="isEditingTitle = false; flushCachedSurvey()" value="text">Cancel</button>
              </div>
            </div>
          </div>
          <div class="col-12" v-show="isCoordinator()">
            <button class="btn btn-link pl-0" type="button" v-if="!isEditingTitle" v-on:click="isEditingTitle = true">Rename</button>
          </div>
        </div>

        <!-- Cover Image-->
          <!-- image controls -->
        <div v-show="isCoordinator()">
          <button v-if="!isEditingImage" class="btn btn-link pl-0" type="button" v-on:click="isEditingImage = true">Change Cover Image</button>
          <button v-if="isEditingImage && hasImage" class="btn btn-link pl-0" type="button" v-on:click="saveSurveyImage">Save Cover Image</button>
          <button v-if="isEditingImage" class="btn btn-link text-secondary pl-0" type="button" v-on:click="isEditingImage = false; hasImage = false;">Cancel Image Editing</button>
        </div>
          <!-- image itself -->
        <div v-if="!isEditingImage && survey.imageUrl" class="mt-2" style="width: 400px; height: 200px; background-size: cover;" v-bind:style="{ backgroundImage: 'url(\'' + this.$apiUrl + survey.imageUrl + '\')' }">
        </div>
        <image-uploader v-if="isEditingImage"
              :quality="0.9"
              :autoRotate=true
              outputFormat="file"
              :preview=true
              :className="['', { 'fileinput--loaded' : hasImage }]"
              :capture="false"
              accept="video/*,image/*"
              doNotResize="['gif', 'svg']"
              @input="setImage"
          ></image-uploader>


        <!-- Question -->
          <!-- TODO add a control to reorder questions -->
          <div v-for="(question,qIndex) in this.surveyQuestions" v-bind:key="question.uuid" class="survey-question-block mt-5">
            <div v-if="!editing[question.uuid]">
              <div class="row">
                <div class="col-md-8">
                  <h5>{{ question.text }}</h5>
                </div>
                <div class="col-md-4" v-show="isCoordinator()">
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
        <div class="row" v-show="isCoordinator()">
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
        <div aria-label="button group" class="btn-toolbar mt-5" role="toolbar">
          <input class="btn btn-primary mr-2" value="Submit" type="submit"
                 @click.prevent="submitResponse(true)"
                 :disabled="isSurveySubmitted()">
          <input class="btn btn-warning mr-2" value="Save Draft" type="submit"
                 @click.prevent="submitResponse(false)"
                 :disabled="isSurveySubmitted()">
          <button class="btn btn-light mr-2" type="button" v-on:click="close()">Close</button>
        </div>
        <div aria-label="button group" class="btn-toolbar mt-4" role="toolbar">
          <button class="btn btn-outline-danger" type="button" v-show="isCoordinator()" v-on:click="deleteSurvey()">Delete Survey</button>
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
import ImageUploader from 'vue-image-upload-resize'
import rolesMixin from "@/components/mixins/rolesMixin";

export default {
  name: 'Survey',
  components: {SurveyQuestionEdit, ImageUploader},
  mixins: [rolesMixin],
  data: function() {
    return {
      isAddingNewQuestion: false,
      isEditingTitle: false,
      isEditingImage: false,
      hasImage: false,
      surveyImage: null,
      editing: {},
      cachedSurvey: null, // TODO make all changes to cached copy
      formData: []
    }
  },
  props: {
    surveyUuid: String
  },
  created() {
    if (this.surveyUuid) {
      // if existing survey is opened
      this.formData = this.surveyQuestions // default value
      const apiCalls = [
        this.getSurveyByIdApi(this.surveyUuid),
        // load existing survey response if already submitted
        this.findBySurveyForCurrentUserApi(this.surveyUuid)
      ];
      Promise.all(apiCalls).then(responses => {
        const survey = responses[0]
        this.initSurveyQuestions(survey.questions)
        // cache survey for draft form editing (user cancels and we should flush the model to initial state)
        this.cachedSurvey = Object.assign({}, survey);
      })
    } else {
      // if new survey is created
      this.initNewSurvey()
      this.initSurveyQuestions([])
    }
  },
  computed: {
    ...mapState({
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
      setSurveyImageApi: 'setSurveyImage'
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

    // SURVEY
    updateTitle() {
      this.updateSurveyApi({ ...this.cachedSurvey, title: this.cachedSurvey.title })
    },
    deleteSurvey() {
      Promise.all([
        this.deleteSurveyApi(this.survey.uuid),
        this.flushSurveyQuestions()
      ]).then(
          () => router.push('/')
      )
    },
    flushCachedSurvey() {
      this.cachedSurvey = Object.assign({}, this.survey);
    },
    close() {
      this.flushSurveyQuestions().then(
          () => router.back()
      )
    },
    initNewSurvey() {
      this.newSurveyApi(() => {
        router.replace("/surveys/" + this.survey.uuid)
        this.flushCachedSurvey()
      })
    },

    // SURVEY IMAGE
    setImage(file) {
      this.hasImage = true
      this.surveyImage = file
    },
    saveSurveyImage() {
      this.setSurveyImageApi(this.surveyImage).then(
          (location) => {
            this.updateSurveyApi({
              uuid: this.cachedSurvey.uuid,
              title: this.cachedSurvey.title, // TODO avoid setting it here
              imageUrl: location
            })
            this.isEditingImage = false
          }
      )
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
          questions.filter(q => choiceResponse.question.id === q.uuid)
              .forEach(q => q.choices.filter(c => choiceResponse.choice.id === c.uuid)
                  .forEach(c => c.val = true)
              )
        }
      }
      return questions
    },
    submitResponse(isFinal) {
      // TODO validate input, all arbitrary questions should be responded
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
      this.createSurveyResponseApi(request).then(
          // go to the main page
          () => {router.push('/')}
      );
    },
    isSurveySubmitted() {
      return this.surveyResponse && this.surveyResponse.submitted
    }
  }
}

</script>

<style>
.input-hidden {
  opacity: 0;
}
.img-preview {
  width: 200px;
  display: block;
  margin-bottom: 10px;
}
</style>
