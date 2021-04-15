<template>
  <div id="main" class="container-fluid">
    <h1>hi, {{account.user.token}}</h1>
    <div class="row">
      <div class="col-md-12">
        <navbar/>
        <div class="row">
          <div class="col-md-2">
            <sidebar/>
          </div>
          <div class="col-md-8">
            <survey-list/>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Navbar from '@/components/Navbar.vue'
import Sidebar from '@/components/Sidebar.vue'
import SurveyList from '@/components/SurveyList.vue'
import { mapState } from 'vuex'

export default {
  name: 'Main',
  components: {
    Navbar,
    Sidebar,
    SurveyList
  },
  computed: {
    ...mapState({
      account: state => state.account,
    })
  },
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}
</style>
