<template>
  <nav class="navbar navbar-expand-lg navbar-light bg-white px-0 justify-content-between">
    <a class="navbar-brand" href="/"><h3><b>🔶 Surveys Demo App</b></h3></a>

    <button class="navbar-toggler" data-target="#bs-navbar-collapse-1" data-toggle="collapse" type="button">
      <span class="navbar-toggler-icon"></span>
    </button>


    <div id="bs-navbar-collapse-1" class="collapse navbar-collapse mt-3">
      <ul class="navbar-nav ml-md-auto">
        <form class="form-inline">
          <button class="btn btn-primary mb-4" type="button" v-on:click="newSurvey()">
            Create Survey
          </button>
        </form>

        <li v-if="account.user" class="nav-item dropdown">
          <a id="navbarDropdownMenuLink2" class="nav-link dropdown-toggle" data-toggle="dropdown" href="">{{
              account.user.name
            }}</a>
          <div class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdownMenuLink2">
            <a class="dropdown-item text-primary" href="/profile">Profile</a>
            <a class="dropdown-item text-primary" href="/preferences">Preferences</a>
            <a class="dropdown-item text-primary" href="/user-management">User Management</a>
            <div class="dropdown-divider">
            </div> <a class="dropdown-item text-primary" href="/logout" v-on:click="logout()">Logout</a>
          </div>
        </li>
      </ul>
    </div>
  </nav>
</template>

<script>

import {mapActions, mapState} from "vuex";
import {router} from "@/router";

export default {
  name: 'Navbar',
  props: {
  },
  computed: {
    ...mapState({
      account: state => state.account,
    })
  },
  methods: {
    ...mapActions('account', ['logout']),

    newSurvey: () => {
      router.push('/surveys/new');
    },
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.navbar {
  margin-bottom: 20px;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
