<template>
  <div>
    <div class="page-header">
      <h1>
        Surveys: <small>Today</small>
      </h1>
    </div>
    <nav>
      <ul class="pagination">
        <li class="page-item">
          <a class="page-link" href="#">Previous</a>
        </li>
        <li class="page-item">
          <a class="page-link" href="#">1</a>
        </li>
        <li class="page-item">
          <a class="page-link" href="#">2</a>
        </li>
        <li class="page-item">
          <a class="page-link" href="#">3</a>
        </li>
        <li class="page-item">
          <a class="page-link" href="#">4</a>
        </li>
        <li class="page-item">
          <a class="page-link" href="#">5</a>
        </li>
        <li class="page-item">
          <a class="page-link" href="#">Next</a>
        </li>
      </ul>
    </nav>
    <div class="row survey-card-list">
      <div class="col-md-3">
        <survey-card name="One"/>
      </div>
      <div class="col-md-3">
        <survey-card name="Two"/>
      </div>
      <div class="col-md-3">
        <survey-card name="Three"/>
      </div>
      <div class="col-md-3">
        <survey-card name="Four"/>
      </div>
    </div>
    <button type="button" class="btn btn-success">
      Create Survey
    </button>
  </div>
</template>

<script>
import SurveyCard from "@/components/SurveyCard";

export default {
  name: 'SurveyList',
  components: {SurveyCard},
  props: {
    msg: String
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.page-header {
  margin-bottom: 30px;
}
.survey-card-list {
  margin-top: 30px;
  margin-bottom: 30px;
}
</style>