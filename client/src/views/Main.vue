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
import { mapState } from 'vuex'

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
    onFilterUpdate(condition) {
      console.log("conditions", condition);
      this.surveysQuery = Object.assign(this.surveysQuery, condition)
      console.log("this.surveysQuery", this.surveysQuery);
    }
  }
}
</script>

<style>
/*#app {*/
/*  font-family: Avenir, Helvetica, Arial, sans-serif;*/
/*  -webkit-font-smoothing: antialiased;*/
/*  -moz-osx-font-smoothing: grayscale;*/
/*  color: #2c3e50;*/
/*}*/
</style>
