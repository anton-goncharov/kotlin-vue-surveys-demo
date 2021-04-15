<template>
  <a href="#">
    <div class="survey-card">
      <span class="badge badge-default">{{ name }}</span>
    </div>
  </a>
</template>

<script>
export default {
  name: 'SurveyCard',
  props: {
    name: String
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.survey-card {
  width: 100%;
  height: 150px;

  border: 1px solid #dee2e6;
  border-radius: 7px;
}
</style>