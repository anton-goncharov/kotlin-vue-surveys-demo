<template>
  <div class="row">
    <div class="col-md-2">
      <sidebar/>
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
