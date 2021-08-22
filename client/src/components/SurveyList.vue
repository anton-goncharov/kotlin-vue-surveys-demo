<template>
  <div v-if="surveysByTag">
    <div class="page-header">
      <h1>Surveys<!--: All--></h1>
    </div>

    <!-- Pagination -->
<!--    <nav>-->
<!--      <ul v-if="surveys.page.totalPages > 1" class="pagination">-->
<!--        &lt;!&ndash; disable '<<' only if it's first page &ndash;&gt;-->
<!--        <li class="page-item" v-bind:class="{disabled: surveys.page.number === 0}">-->
<!--          <a :href="'/?page=' + (surveys.page.number - 1)" class="page-link">-->
<!--            <span aria-hidden="true">&laquo;</span>-->
<!--            <span class="sr-only">Previous</span>-->
<!--          </a>-->
<!--        </li>-->
<!--        <li v-for="n in surveys.page.totalPages" v-bind:key="'page'+ n" class="page-item"-->
<!--            v-bind:class="{ active: (surveys.page.number === n-1) }">-->
<!--          <a :href="'/?page=' + (n - 1)" class="page-link">{{ n }}</a>-->
<!--        </li>-->
<!--        &lt;!&ndash; disable '>>' only if current page number is max &ndash;&gt;-->
<!--        <li class="page-item" v-bind:class="{disabled: surveys.page.number === (surveys.page.totalPages - 1)}">-->
<!--          <a :href="'/?page=' + (surveys.page.number + 1)" class="page-link">-->
<!--            <span aria-hidden="true">&raquo;</span>-->
<!--            <span class="sr-only">Next</span>-->
<!--          </a>-->
<!--        </li>-->
<!--        &lt;!&ndash; TODO handle when there's a lot of pages (limit them in UI) &ndash;&gt;-->
<!--      </ul>-->
<!--    </nav>-->

    <!-- List by tag -->
    <div>
      <!-- for each tag -->
      <div v-for="tag in tags.items" :key="tag.uuid">
        <h2>{{tag.name}}</h2>
        <!-- list survey cards -->
        <div v-if="surveysByTag[tag.shortName].items._embedded" class="row survey-card-list mb-5">
          <div v-for="survey in surveysByTag[tag.shortName].items._embedded.surveys" :key="survey.uuid" class="col-md-3 my-2">
            <survey-card :title="survey.title" :uuid="survey.uuid" :image-url="survey.imageUrl" v-on:click="openSurvey(survey.uuid)"/>
          </div>
        </div>
        <h5 v-else class="text-secondary mt-3 mb-5">No items</h5>
      </div>
    </div>

    <!-- List untagged -->
    <div v-if="surveysByTag['none'].items && surveysByTag['none'].items._embedded">
      <h2>Other</h2>
      <!-- list survey Card -->
      <div class="row survey-card-list mb-5">
        <div v-for="survey in surveysByTag['none'].items._embedded.surveys" :key="survey.uuid" class="col-md-3 my-2">
          <survey-card :title="survey.title" :uuid="survey.uuid" :image-url="survey.imageUrl" v-on:click="openSurvey(survey.uuid)"/>
        </div>
      </div>
    </div>

    <button class="btn btn-primary mb-4" type="button" v-on:click="newSurvey()">
      Create Survey
    </button>
  </div>
  <div v-else>
    <img src="data:image/gif;base64,R0lGODlhEAAQAPIAAP///wAAAMLCwkJCQgAAAGJiYoKCgpKSkiH/C05FVFNDQVBFMi4wAwEAAAAh/hpDcmVhdGVkIHdpdGggYWpheGxvYWQuaW5mbwAh+QQJCgAAACwAAAAAEAAQAAADMwi63P4wyklrE2MIOggZnAdOmGYJRbExwroUmcG2LmDEwnHQLVsYOd2mBzkYDAdKa+dIAAAh+QQJCgAAACwAAAAAEAAQAAADNAi63P5OjCEgG4QMu7DmikRxQlFUYDEZIGBMRVsaqHwctXXf7WEYB4Ag1xjihkMZsiUkKhIAIfkECQoAAAAsAAAAABAAEAAAAzYIujIjK8pByJDMlFYvBoVjHA70GU7xSUJhmKtwHPAKzLO9HMaoKwJZ7Rf8AYPDDzKpZBqfvwQAIfkECQoAAAAsAAAAABAAEAAAAzMIumIlK8oyhpHsnFZfhYumCYUhDAQxRIdhHBGqRoKw0R8DYlJd8z0fMDgsGo/IpHI5TAAAIfkECQoAAAAsAAAAABAAEAAAAzIIunInK0rnZBTwGPNMgQwmdsNgXGJUlIWEuR5oWUIpz8pAEAMe6TwfwyYsGo/IpFKSAAAh+QQJCgAAACwAAAAAEAAQAAADMwi6IMKQORfjdOe82p4wGccc4CEuQradylesojEMBgsUc2G7sDX3lQGBMLAJibufbSlKAAAh+QQJCgAAACwAAAAAEAAQAAADMgi63P7wCRHZnFVdmgHu2nFwlWCI3WGc3TSWhUFGxTAUkGCbtgENBMJAEJsxgMLWzpEAACH5BAkKAAAALAAAAAAQABAAAAMyCLrc/jDKSatlQtScKdceCAjDII7HcQ4EMTCpyrCuUBjCYRgHVtqlAiB1YhiCnlsRkAAAOwAAAAAAAAAAAA==" />
  </div>
</template>

<script>
import {mapActions, mapState} from 'vuex'
import SurveyCard from "@/components/SurveyCard";
import {router} from "@/router";
import rolesMixin from "@/components/mixins/rolesMixin";

export default {
  name: 'SurveyList',
  mixins: [rolesMixin],
  components: {
    SurveyCard
  },
  created() {

    for (let tag of ['new-noteworthy', 'education', 'none']) {
      this.getAllSurveysByTag(tag);
    }
    // this.getAllSurveys();

    this.getAllTags()
  },
  computed: {
    ...mapState({
      surveys: state => state.surveys.all,
      surveysByTag: state => state.surveys.byTag,
      tags: state => state.tags.all
    })
  },
  methods: {
    ...mapActions('surveys', {
      getAllSurveys: 'getAll',
      getAllSurveysByTag: 'getAllByTag',
      getAllSurveysUntagged: 'getAllUntagged'
    }),
    ...mapActions('tags', {
      getAllTags: 'getAll'
    }),
    newSurvey: () => {
      router.push('/surveys/new');
    },
    openSurvey: (uuid) => {
      router.push('/surveys/' + uuid);
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.page-header {
  margin-bottom: 30px;
}

</style>