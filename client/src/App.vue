<template>
  <div class="container-fluid" id="app">
    <div v-if="alert.message" :class="`alert ${alert.type}`">{{alert.message}}</div>
    <div id="main" class="container-fluid">
      <div class="row">
        <div class="col-md-12">
          <navbar/>
          <router-view></router-view>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState, mapActions } from 'vuex'
import Navbar from "@/components/Navbar";

export default {
  name: 'App',
  components: {
    Navbar
  },
  computed: {
    ...mapState({
      alert: state => state.alert
    })
  },
  methods: {
    ...mapActions({
      clearAlert: 'alert/clear'
    })
  },
  watch: {
    $route() {
      // clear alert on location change
      this.clearAlert();
    }
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
