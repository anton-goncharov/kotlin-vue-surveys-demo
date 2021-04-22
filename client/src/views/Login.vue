<template>
  <div class="container">
    <div class="row justify-content-center">
      <div class="col-lg-6 col-md-8 col-xs-10 mt-5">
        <h2 class="w-100">Login</h2>
        <form @submit.prevent="handleSubmit">
          <div class="form-group">
            <label for="username">Username</label>
            <input id="username" v-model="username" :class="{ 'is-invalid': submitted && !username }" class="form-control" type="text" />
            <div v-show="submitted && !username" class="invalid-feedback">Username is required</div>
          </div>
          <div class="form-group">
            <label for="password">Password</label>
            <input id="password" v-model="password" :class="{ 'is-invalid': submitted && !password }" class="form-control" type="password" />
            <div v-show="submitted && !password" class="invalid-feedback">Password is required</div>
          </div>
          <div class="form-group">
            <button :disabled="status.loggingIn" class="btn btn-primary">Login</button>
            <img v-show="status.loggingIn" src="data:image/gif;base64,R0lGODlhEAAQAPIAAP///wAAAMLCwkJCQgAAAGJiYoKCgpKSkiH/C05FVFNDQVBFMi4wAwEAAAAh/hpDcmVhdGVkIHdpdGggYWpheGxvYWQuaW5mbwAh+QQJCgAAACwAAAAAEAAQAAADMwi63P4wyklrE2MIOggZnAdOmGYJRbExwroUmcG2LmDEwnHQLVsYOd2mBzkYDAdKa+dIAAAh+QQJCgAAACwAAAAAEAAQAAADNAi63P5OjCEgG4QMu7DmikRxQlFUYDEZIGBMRVsaqHwctXXf7WEYB4Ag1xjihkMZsiUkKhIAIfkECQoAAAAsAAAAABAAEAAAAzYIujIjK8pByJDMlFYvBoVjHA70GU7xSUJhmKtwHPAKzLO9HMaoKwJZ7Rf8AYPDDzKpZBqfvwQAIfkECQoAAAAsAAAAABAAEAAAAzMIumIlK8oyhpHsnFZfhYumCYUhDAQxRIdhHBGqRoKw0R8DYlJd8z0fMDgsGo/IpHI5TAAAIfkECQoAAAAsAAAAABAAEAAAAzIIunInK0rnZBTwGPNMgQwmdsNgXGJUlIWEuR5oWUIpz8pAEAMe6TwfwyYsGo/IpFKSAAAh+QQJCgAAACwAAAAAEAAQAAADMwi6IMKQORfjdOe82p4wGccc4CEuQradylesojEMBgsUc2G7sDX3lQGBMLAJibufbSlKAAAh+QQJCgAAACwAAAAAEAAQAAADMgi63P7wCRHZnFVdmgHu2nFwlWCI3WGc3TSWhUFGxTAUkGCbtgENBMJAEJsxgMLWzpEAACH5BAkKAAAALAAAAAAQABAAAAMyCLrc/jDKSatlQtScKdceCAjDII7HcQ4EMTCpyrCuUBjCYRgHVtqlAiB1YhiCnlsRkAAAOwAAAAAAAAAAAA==" />
            <router-link class="btn btn-link" to="/register">Register</router-link>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>

import { mapState, mapActions } from 'vuex'

export default {
  data () {
    return {
      username: '',
      password: '',
      submitted: false
    }
  },
  computed: {
    ...mapState('account', ['status'])
  },
  created () {
    // reset login status
    this.logout();
  },
  methods: {
    ...mapActions('account', ['login', 'logout']),
    handleSubmit () {
      this.submitted = true;
      const { username, password } = this;
      if (username && password) {
        this.login({ username, password })
      }
    }
  }
};


</script>

<style>
</style>
