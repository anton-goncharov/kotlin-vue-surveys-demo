<template>
  <div class="container-fluid">
    <div class="row">
      <h5 class="w-100">Time Period</h5>
      <ul class="nav nav-pills">
        <li class="nav-item pr-3">
          <survey-filter :display-label="'Today'" :filter-value="'today'" :is-selected="isTimeSelected" :on-click="setTimeQuery" />
        </li>
        <li class="nav-item pr-3">
          <survey-filter :display-label="'Last Week'" :filter-value="'week'" :is-selected="isTimeSelected" :on-click="setTimeQuery" />
        </li>
        <li class="nav-item pr-3">
          <survey-filter :display-label="'All Time'" :filter-value="'all'" :is-selected="isTimeSelected" :on-click="setTimeQuery" />
        </li>
      </ul>
    </div>
    <div class="row my-4">
      <h5 class="w-100">Status</h5>
      <ul class="nav nav-pills">
        <li class="nav-item pr-3">
          <survey-filter :display-label="'Completed'" :filter-value="'completed'" :is-selected="isStatusSelected" :on-click="setStatusQuery" />
        </li>
        <li class="nav-item pr-3">
          <survey-filter :display-label="'Incomplete'" :filter-value="'incomplete'" :is-selected="isStatusSelected" :on-click="setStatusQuery" />
        </li>
        <li class="nav-item pr-3">
          <survey-filter :display-label="'All'" :filter-value="'all'" :is-selected="isStatusSelected" :on-click="setStatusQuery" />
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import SurveyFilter from "@/components/SurveyFilter";

export default {
  name: 'Sidebar',
  components: {SurveyFilter},
  props: {
    timeQuery: String,
    statusQuery: String
  },
  data: function() {
    return {
      timeSelector: 'all',
      statusSelector: 'all'
    }
  },
  created() {
    this.timeSelector = this.timeQuery || this.timeSelector
    this.statusSelector = this.timeQuery || this.statusSelector
  },
  methods: {
    isTimeSelected(filterValue) {
      return this.timeSelector === filterValue
    },
    isStatusSelected(filterValue) {
      return this.statusSelector === filterValue
    },
    setTimeQuery(condition) {
      this.$emit(`filter-surveys`, {time: condition})
      this.timeSelector = condition
    },
    setStatusQuery(condition) {
      this.$emit(`filter-surveys`, {status: condition})
      this.statusSelector = condition
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>
