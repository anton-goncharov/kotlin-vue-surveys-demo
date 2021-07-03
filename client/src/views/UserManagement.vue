<template>
  <div class="row">
    <div class="col"></div>
    <div class="col-lg-6 col-md-8 col-sm-10">
      <h2>Users</h2>

      <table class="table mt-4">
        <thead>
        <tr>
          <th scope="col">#</th>
          <th scope="col">Name</th>
          <th scope="col">Email</th>
          <th scope="col">Role</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(user, index) in users" :key="user.uuid">
          <th scope="row">{{ index + 1 }}</th>
          <td>{{ user.name }}</td>
          <td><a :href="'mailto:' + user.email">{{ user.email }}</a></td>
          <td>
            <span v-if="user.role === 'COORDINATOR'" class="badge badge-primary">Coordinator</span>
            <span v-if="user.role === 'RESPONDENT'" class="badge badge-light">Respondent</span>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
    <div class="col"></div>
  </div>
</template>

<script>

import {mapActions, mapState} from "vuex";

export default {
  name: "UserManagement",
  created() {
    this.getAllUsers();
  },
  computed: {
    ...mapState({
      users: state => state.users.all.items
    })
  },
  methods: {
    ...mapActions('users', {
      getAllUsers: 'getAll'
    })
  }
};

</script>

<style>
</style>
