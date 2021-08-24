<template>
  <div class="col-12 m-0 p-0 my-4">
    <h6>Tags</h6>
    <vue-tags-input
        v-model="inputTag"
        :tags="inputTags"
        :autocomplete-items="autocompleteItems"
        :add-only-from-autocomplete="true"
        @tags-changed="update"
    />
  </div>
</template>

<script>

import VueTagsInput from "@johmun/vue-tags-input";
import {mapActions, mapState} from "vuex";

export default {
  name: 'SurveyTags',
  components: {VueTagsInput},
  data: function() {
    return {
      inputTag: '',
      inputTags: []
    }
  },
  props: {
    surveyTags: Array
  },
  created() {
    this.getAllTags()
    // populate list of existing tags for this survey
    this.inputTags = this.surveyTags.map( tag => tag.name )
  },
  computed: {
    ...mapState({
      tags: state => state.tags.all,
      // populate autocomplete items from all loaded tags
      autocompleteItems: state => state.tags.all.items ? state.tags.all.items.map(item => {
        return { text: item.name };
      }) : []
    })
  },
  methods: {
    ...mapActions('tags', {
      getAllTags: 'getAll'
    }),
    update(newTags) {
      // TODO fix adding tags to a new survey
      this.inputTags = newTags;
      const tagUuids = newTags.map(tagName => this.tags.items.find(tag => tag.name === tagName.text).uuid);
      this.$emit('saveTags', tagUuids);
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>