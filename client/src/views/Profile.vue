<template>
  <div class="row user-profile">
    <div class="col"></div>
    <div class="col-lg-6 col-md-8 col-sm-10">
      <h2>Your Profile</h2>

      <div class="mt-4">
        <div class="form-group">
          <label for="inputName">Name</label>
          <input type="text" class="form-control" id="inputName" :value="account.user.name">
        </div>
        <div class="form-group">
          <label for="inputEmail">Email</label>
          <input type="text" class="form-control" id="inputEmail" :value="account.user.email">
        </div>
        <div class="form-group">
          <label>Role</label>
          <h5 class="m-0"><span v-if="account.user.role === 'COORDINATOR'" class="badge badge-primary">Coordinator</span></h5>
          <h5 class="m-0"><span v-if="account.user.role === 'RESPONDENT'" class="badge badge-light">Respondent</span></h5>
        </div>
        <div class="form-group">
          <label>Created At</label>
          <p>{{ formatDate(account.user.createdDate) }}</p>
        </div>
<!--        <h4>Name: {{ account }}</h4>-->
<!--        <h4>Name: {{ account.user.name }}</h4>-->
<!--        <h4>Email: {{ account.user.email }}</h4>-->
      </div>
    </div>
    <div class="col"></div>
  </div>
</template>

<script>

import {mapState} from "vuex";
import moment from "moment";

export default {
  name: "Profile",
  computed: {
    ...mapState({
      account: state => state.account,
    })
  },
  methods: {
    formatDate(date) {
      return moment(date).format('LL')
    }
  }
}

</script>

<style>
  .user-profile label {
    font-weight: bold;
  }
</style>
