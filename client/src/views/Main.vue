<template>
  <div class="row">
    <div class="col-md-2">
      <sidebar v-on:filter-surveys="onFilterUpdate"/>
    </div>
    <div class="col-md-8">
      <survey-list />
    </div>
  </div>
</template>

<script>
import Sidebar from '@/components/Sidebar.vue'
import SurveyList from '@/components/SurveyList.vue'
import {mapActions, mapState} from 'vuex'

export default {
  name: 'Main',
  data: function() {
    return {
      surveysQuery: {}
    }
  },
  components: {
    Sidebar,
    SurveyList
  },
  computed: {
    ...mapState({
      account: state => state.account
    })
  },
  methods: {
    ...mapActions('surveys', {
      setSearchQuery: 'setSearchQuery',
    }),
    onFilterUpdate(condition) {
      this.surveysQuery = { ...this.surveysQuery, ...condition }
      this.setSearchQuery(this.surveysQuery)
    }
  }
}
</script>

<style>
</style>
