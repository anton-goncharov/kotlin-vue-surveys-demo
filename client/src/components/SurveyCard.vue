<template>
  <transition name="fade">
  <a :href="`/surveys/${uuid}`" class="nounderline">
    <div class="survey-card" :class="{ 'bg-dark' : !imageUrl }" :style="{ backgroundImage: imageUrl ? 'url(' + this.$apiUrl + imageUrl + ')' : 'none' }">
      <div class="h-75 p-3">
        <h4 class="text-light">{{ title }}</h4>
      </div>
      <div class="h-25">
        <div class="col-md-12">
          <a v-show="isCoordinator()" :href="`/surveys/${uuid}/stats`" class="badge badge-warning align-text-bottom">Stats</a>
        </div>
      </div>
    </div>
  </a>
  </transition>
</template>

<script>
import rolesMixin from "@/components/mixins/rolesMixin";

export default {
  name: 'SurveyCard',
  mixins: [rolesMixin],
  props: {
    title: String,
    imageUrl: String,
    uuid: String,
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
  background-color: rgba(0, 0, 0, 0.3);
  background-size: cover;
  background-blend-mode: overlay;

}

.nounderline {
  text-decoration: none !important
}
</style>