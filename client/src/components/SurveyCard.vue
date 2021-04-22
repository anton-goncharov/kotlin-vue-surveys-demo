<template>
  <a :href="`/surveys/${uuid}`" class="nounderline">
    <div class="survey-card bg-dark">
      <div class="h-75 p-3">
        <h4 class="text-light">{{ title }}</h4>
      </div>
      <div class="h-25">
        <div class="col-md-12">
          <a :href="`/surveys/${uuid}/stats`" class="badge badge-warning align-text-bottom">Stats</a>
        </div>
      </div>
    </div>
  </a>
</template>

<script>
export default {
  name: 'SurveyCard',
  props: {
    title: String,
    uuid: String
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

.nounderline {
  text-decoration: none !important
}
</style>