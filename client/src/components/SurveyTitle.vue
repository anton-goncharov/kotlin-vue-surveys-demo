<template>
  <!-- Survey Title -->
  <div class="row">
    <div v-if="!isEditingTitle" class="col-md-auto">
      <h3>{{ cachedSurvey.title }} <span v-if="isSubmitted" class="badge badge-success">Completed</span></h3>
    </div>
    <div v-else class="col-md-auto btn-toolbar">
      <input id="inlineFormInputGroup" v-model="cachedSurvey.title" class="form-control col-8" placeholder="Enter survey title here" type="text">
      <div class="input-group-append col-4">
        <div class="btn-group">
          <button class="btn btn-outline-primary" type="button" v-on:click="updateTitle()" value="text">Save</button>
          <button class="btn btn-outline-secondary" type="button" v-on:click="resetCachedSurvey()" value="text">Cancel</button>
        </div>
      </div>
    </div>
    <div class="col-12" v-show="isCoordinator()">
      <button class="btn btn-link pl-0" type="button" v-if="!isEditingTitle" v-on:click="isEditingTitle = true">Rename</button>
    </div>
  </div>
</template>

<script>

import rolesMixin from "@/components/mixins/rolesMixin";

export default {
  name: 'SurveyTitle',
  mixins: [rolesMixin],
  components: {},
  data: function() {
    return {
      isEditingTitle: false
    }
  },
  props: {
    isSubmitted: Boolean,
    cachedSurvey: Object
  },
  created() {
  },
  computed: {
  },
  methods: {
    updateTitle() {
      this.isEditingTitle = false;
      this.$emit('save', {title: this.cachedSurvey.title});
    },
    resetCachedSurvey() {
      this.isEditingTitle = false;
      this.$emit('reset');
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>