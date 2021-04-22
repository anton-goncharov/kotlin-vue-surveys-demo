<template>
  <div v-if="surveys.items">
    <div class="page-header">
      <h1>Surveys: All</h1>
    </div>

    <!-- Pagination -->
    <nav>
      <ul v-if="surveys.items.page.totalPages > 1" class="pagination">
        <!-- disable '<<' only if it's first page -->
        <li class="page-item" v-bind:class="{disabled: surveys.items.page.number === 0}">
          <a :href="'/?page=' + (surveys.items.page.number - 1)" class="page-link">
            <span aria-hidden="true">&laquo;</span>
            <span class="sr-only">Previous</span>
          </a>
        </li>
        <li v-for="n in surveys.items.page.totalPages" v-bind:key="'page'+ n" class="page-item"
            v-bind:class="{ active: (surveys.items.page.number === n-1) }">
          <a :href="'/?page=' + (n - 1)" class="page-link">{{ n }}</a>
        </li>
        <!-- disable '>>' only if current page number is max -->
        <li class="page-item" v-bind:class="{disabled: surveys.items.page.number === (surveys.items.page.totalPages - 1)}">
          <a :href="'/?page=' + (surveys.items.page.number + 1)" class="page-link">
            <span aria-hidden="true">&raquo;</span>
            <span class="sr-only">Next</span>
          </a>
        </li>
        <!-- TODO handle when there's a lot of pages (limit them in UI) -->
      </ul>
    </nav>

    <!-- Survey Card -->
    <div class="row survey-card-list my-5">
      <div v-for="survey in surveys.items._embedded.surveys" :key="survey.uuid" class="col-md-3 my-2">
        <survey-card :title="survey.title" :uuid="survey.uuid" v-on:click="openSurvey(survey.uuid)"/>
      </div>
    </div>

    <button class="btn btn-primary" type="button" v-on:click="newSurvey()">
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

export default {
  name: 'SurveyList',
  components: {
    SurveyCard
  },
  created() {
    this.getAllSurveys(this.$route.query.page);
  },
  computed: {
    ...mapState({
      surveys: state => state.surveys.all
    })
  },
  methods: {
    ...mapActions('surveys', {
      getAllSurveys: 'getAll'
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