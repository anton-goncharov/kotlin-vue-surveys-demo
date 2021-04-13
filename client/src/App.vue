<template>
  <div class="container-fluid" id="app">
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
import Navbar from './components/Navbar.vue'
import Sidebar from './components/Sidebar.vue'
import SurveyList from './components/SurveyList.vue'

export default {
  name: 'App',
  components: {
    Navbar,
    Sidebar,
    SurveyList
  }
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
