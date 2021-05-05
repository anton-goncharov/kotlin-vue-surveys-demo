<template>
  <div class="col-12 my-4 py-4 border-left" style="border-color: #dee2e6">
    <!-- question title -->
    <div class="col-12">
      <input v-model="question.text" class="form-control form-control-lg" placeholder="Enter new question here" type="text">
    </div>

    <!-- multiselect -->
    <div class="col-12 mt-3">
      <div class="form-check">
        <input id="defaultCheck1"
               v-model="question.multiselect"
               :disabled="question.choices.length < 2"
               class="form-check-input"
               type="checkbox">
        <label class="form-check-label" for="defaultCheck1">
          User can select multiple choices
        </label>
      </div>
    </div>

    <!-- choices -->
    <div v-for="(choice,index) in this.question.choices" v-bind:key="`choice-${index}`" class="col-12 mt-3">
      <label class="sr-only" for="inlineFormInputGroup">Username</label>
      <div class="input-group mb-2">
        <div class="input-group-prepend">
          <div class="input-group-text">{{ index+1 }}</div>
        </div>
        <input id="inlineFormInputGroup" v-model="choice.text" class="form-control" placeholder="Enter choice text here" type="text">
        <div class="input-group-append">
          <div class="input-group-text bg-white border border-danger" style="z-index: 3">
            <button aria-label="Close" class="close" type="button" v-on:click="deleteQuestionChoice(index)">
              <span aria-hidden="true" class="text-danger">&times;</span>
            </button>
          </div>
        </div>
      </div>
    </div><i class="bi bi-trash"></i>

    <div class="col-12 mt-3">
      <button class="btn btn-link btn-sm" type="button" v-on:click="addQuestionChoice">Add Choice</button>
    </div>
    <div class="col-12 mt-2">
      <button class="btn btn-outline-primary btn-sm mr-2" type="button" v-on:click="$emit('saved', question)">Save Question</button>
      <button class="btn btn-link btn-sm text-danger" type="button" v-on:click="$emit('cancelled', question.uuid)">Cancel</button>
    </div>
  </div>
</template>

<script>

export default {
  name: 'SurveyQuestionEdit',
  data: function() {
    return {
      question: {}
    }
  },
  props: {
    questionSupplier: Function
  },
  created() {
    this.question = this.questionSupplier()
  },
  computed: {
  },
  methods: {
    addQuestionChoice() {
      this.question.choices.push({});
    },
    deleteQuestionChoice(index) {
      this.question.choices.splice(index,1);
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>