<template>
  <div class="row">
    <div class="col"></div>
    <div class="col-lg-6 col-md-8 col-sm-10">
      <h2>Preferences</h2>
      <div class="mt-4">
        <h4>Show Surveys</h4>
        <div v-for="tag in tags.items" :key="tag.uuid" class="form-check mt-2">
          <input class="form-check-input" type="checkbox" value="" :id="'tag-' + tag.uuid">
          <label class="form-check-label" :for="'tag-' + tag.uuid">
            {{ tag.name }}
          </label>
        </div>
        <div class="form-check mt-3">
          <input class="form-check-input" type="checkbox" value="" id="untagged">
          <label class="form-check-label" for="untagged">
            Display untagged
          </label>
        </div>
      </div>

    </div>
    <div class="col"></div>
  </div>
</template>

<script>

import {mapActions, mapState} from "vuex";
import moment from "moment";

export default {
  name: "Preferences",
  created() {
    this.getAllTags()
  },
  computed: {
    ...mapState({
      account: state => state.account,
      tags: state => state.tags.all
    })
  },
  methods: {
    ...mapActions('tags', {
      getAllTags: 'getAll',
    }),
    formatDate(date) {
      return moment(date).format('LL')
    }
  }
}

</script>

<style>
</style>
