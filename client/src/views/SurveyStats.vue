<template>
  <div class="row">
    <div class="col">
    </div>
    <div v-if="this.survey" class="col-lg-6 col-md-8 col-sm-10">
      <form class="container-fluid">
        <!-- Survey Title -->
        <div class="row">
          <div class="col-md-auto">
            <h3>{{ this.survey.title }}</h3>
          </div>
        </div>

        <!-- Cover Image-->
        <div v-if="survey.imageUrl" class="mt-2" style="width: 400px; height: 200px; background-size: cover;" v-bind:style="{ backgroundImage: 'url(\'' + this.$apiUrl + survey.imageUrl + '\')' }">
        </div>

        <!-- Survey Controls -->
        <div aria-label="Toolbar with button groups" class="btn-toolbar my-5" role="toolbar">
          <button class="btn btn-light mr-2" type="button" v-on:click="close()">Close</button>
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

export default {
  name: 'SurveyStats',
  components: {},
  data: function() {
    return {
      surveyImage: null
    }
  },
  props: {
    surveyUuid: String
  },
  created() {
    this.getSurveyByIdApi(this.surveyUuid).then(survey => {
      this.initSurveyQuestions(survey.questions)
    })
  },
  computed: {
    ...mapState({
      survey: state => state.surveys.selected.item,
      surveyQuestions: state => state.surveyQuestions.all.items
    })
  },
  methods: {
    ...mapActions('surveys', {
      getSurveyByIdApi: 'getById'
    }),
    ...mapActions('surveyQuestions', {
      initSurveyQuestions: 'init'
    }),
    ...mapActions('surveyResponses', {
    }),

    // SURVEY
    close() {
      router.back()
    }
  }
}

</script>

<style>
</style>
