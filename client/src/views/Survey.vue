<template>
  <div class="row">
    <div class="col">
    </div>
    <div v-if="this.survey" class="col-lg-6 col-md-8 col-sm-10">

      <!-- Survey Title -->
      <div class="row">
        <div class="col-md-auto">
          <h3>{{ this.survey.title }}</h3>
        </div>
        <div class="col ml-1">
          <button class="btn btn-link" type="button">Rename</button>
          <button class="btn btn-link" type="button">Change Cover Image</button>
        </div>
      </div>

      <!-- Cover Image-->
      <div class="mt-2" style="width: 400px; height: 200px; background-image: url('https://images.unsplash.com/photo-1593642634315-48f5414c3ad9?ixid=MnwxMjA3fDF8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=2250&q=80'); background-size: cover">
      </div>

      <!-- Question -->
      <!-- TODO a control to reorder questions -->
      <div v-for="question in this.survey.questions" v-bind:key="question.uuid" class="survey-question-block mt-5">
        <div class="row">
          <div class="col-md-8">
            <h5>{{ question.text }}</h5>
          </div>
          <div class="col-md-4">
            <button class="btn btn-link" type="button">Edit</button>
            <button class="btn btn-link text-danger" type="button" v-on:click="deleteSurveyQuestionApi(question.uuid)">Delete</button>
          </div>
        </div>
        <div v-for="choice in question.choices" v-bind:key="choice.uuid" class="form-check">
          <!-- Multiselect Question (checkboxes) -->
          <input v-if="question.multiselect === true" :id="choice.uuid" class="form-check-input" type="checkbox" value="">
          <!-- Otherwise: Select-One Question (radio buttons) -->
          <input v-else :id="choice.uuid" :name="question.uuid" class="form-check-input" type="radio" value="">
          <label :for="choice.uuid" class="form-check-label">
            {{ choice.text }}
          </label>
        </div>
      </div>

      <div class="row">
        <button class="btn btn-link mt-4" type="button">Add Question</button>
      </div>

      <div aria-label="Toolbar with button groups" class="btn-toolbar my-5" role="toolbar">
        <button class="btn btn-primary mr-2" type="button" v-on:click="submitSurveyResponse()">Submit</button>
        <button class="btn btn-light mr-2" type="button" v-on:click="close()">Close</button>
        <button class="btn btn-outline-danger" type="button" v-on:click="deleteSurvey()">Delete Survey</button>
      </div>
    </div>
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

export default {
  name: 'Survey',
  props: {
    surveyUuid: String
  },
  created() {
    if (this.surveyUuid) {
      this.getSurveyByIdApi(this.surveyUuid)
    } else {
      this.newSurvey()
    }
  },
  computed: {
    ...mapState({
      account: state => state.account,
      survey: state => state.surveys.selected.item
    })
  },
  methods: {
    ...mapActions('surveys', {
      newSurveyApi: 'newSurvey',
      getSurveyByIdApi: 'getById',
      createSurveyApi: 'create',
      updateSurveyApi: 'update',
      deleteSurveyApi: 'delete',
      deleteSurveyQuestionApi: 'deleteQuestionById'
    }),
    submitSurveyResponse() {
      router.back();
    },
    deleteSurvey() {
      router.back();
    },
    close() {
      router.back(); // TODO or is it better to router.push('/') ?
    },
    newSurvey() {
      this.newSurveyApi(() => router.replace("/surveys/" + this.survey.uuid))

    }
  }
}
</script>

<style>
</style>
