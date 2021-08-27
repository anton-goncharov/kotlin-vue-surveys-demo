<template>
  <button class="nav-link btn"
          v-bind:class="[isSelected(filterValue) ? 'btn-secondary text-light' : 'btn-link']"
          v-on:click="onClick(filterValue)">
    {{ displayLabel }}
    <span class="badge" v-bind:class="[isSelected(filterValue) ? 'badge-light' : 'badge-secondary']">{{ this.count }}</span>
  </button>
</template>

<script>

import {mapActions} from "vuex";

export default {
  name: 'SurveyFilter',
  data: function() {
    return {
      count: ''
    }
  },
  props: {
    isSelected: Function,
    filterKey: String,
    filterValue: String,
    displayLabel: String,
    onClick: Function
  },
  created() {
    const params = {}
    params[this.filterKey] = this.filterValue
    this.countByParams(params).then(
        (result) => this.count = result
    )
  },
  computed: {
  },
  methods: {
    ...mapActions('surveys', {
      countByParams: 'count'
    })
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>