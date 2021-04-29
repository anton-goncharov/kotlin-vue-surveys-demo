<template>
  <button class="nav-link btn"
          v-bind:class="[isSelected(filterValue) ? 'btn-secondary text-light' : 'btn-link']"
          v-on:click="onClick(filterValue)">
    {{ displayLabel }}
    <span class="badge" v-bind:class="[isSelected(filterValue) ? 'badge-light' : 'badge-secondary']">{{ this.count }}</span>
  </button>
</template>

<script>

export default {
  name: 'SurveyFilter',
  data: function() {
    return {
      count: 42
    }
  },
  props: {
    isSelected: Function,
    filterValue: String,
    displayLabel: String,
    onClick: Function
  },
  created() {
  },
  computed: {
  },
  methods: {
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>