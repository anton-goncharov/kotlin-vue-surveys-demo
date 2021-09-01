<template>
  <div v-if="tags.items">
    <div class="page-header">
      <h1>Surveys<!--: All--></h1>
<!--      <span>{{ searchQuery }}</span>-->
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
      <div v-for="data of surveysByTag" :key="data.tag">
        <h2>{{ tags.items.find(t => (t.shortName === data.tag)).name }}</h2>
        <!-- list survey cards -->
        <div v-if="data.items._embedded" class="row survey-card-list mb-5">
          <div v-for="survey in data.items._embedded.surveys" :key="survey.uuid" class="col-md-3 my-2">
            <survey-card :title="survey.title" :uuid="survey.uuid" :image-url="survey.imageUrl" v-on:click="openSurvey(survey.uuid)"/>
          </div>
        </div>
        <h5 v-if="!data.items._embedded || data.items._embedded.surveys.length === 0" class="text-secondary mt-3 mb-5">No items</h5>
      </div>
    </div>

    <!-- List untagged -->
    <div v-if="surveysUntagged && surveysUntagged.items._embedded">
      <h2>Other</h2>
      <!-- list survey Card -->
      <div class="row survey-card-list mb-5">
        <div v-for="survey in surveysUntagged.items._embedded.surveys" :key="survey.uuid" class="col-md-3 my-2">
          <survey-card :title="survey.title" :uuid="survey.uuid" :image-url="survey.imageUrl" v-on:click="openSurvey(survey.uuid)"/>
        </div>
      </div>
    </div>

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
  data: function() {
    return {
      tagsToShow: [],  // TODO get it from user preferences
      surveysSearchParams: {}
    }
  },
  created() {
    this.getAllTags();
    this.getUserPreferences().then(
        () => {
          this.tagsToShow = this.preferences.mainPageLayout.map(tag => tag.shortName)
          if (this.preferences.displayUntagged) {
            this.tagsToShow.push('none')
          }
          this.listSurveys()
        }
    )
  },
  watch: {
    searchQuery: function() {
      this.listSurveys();
    }
  },
  computed: {
    ...mapState({
      surveys: state => state.surveys.all,
      surveysByTag: state => state.surveys.byTag.filter(item => item.tag !== 'none')
                                                .sort((a,b) => a.order - b.order),
      surveysUntagged: state => state.surveys.byTag.find(item => item.tag === 'none'),
      searchQuery: state => state.surveys.searchQuery,
      tags: state => state.tags.all,
      preferences: state => state.preferences.all.items
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
    ...mapActions('preferences', {
      getUserPreferences: 'getAll'
    }),
    listSurveys: function() {
      // TODO list according to user preferences
      for (let i = 0; i < this.tagsToShow.length; i++){
        let tag = this.tagsToShow[i];
        this.getAllSurveysByTag({
          tag: tag,
          order: i
        });
      }
    },
    openSurvey: (uuid) => {
      router.push('/surveys/' + uuid);
    }
  },

}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.page-header {
  margin-bottom: 30px;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity .5s
}

.fade-enter,
.fade-leave-to {
  opacity: 0
}

</style>