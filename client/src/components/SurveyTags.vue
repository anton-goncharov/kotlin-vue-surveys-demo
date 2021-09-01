<template>
  <div class="col-12 m-0 p-0 my-4">
    <h6>Tags</h6>
    <vue-tags-input
        v-model="inputTag"
        :tags="inputTags"
        :autocomplete-items="autocompleteItems"
        :add-only-from-autocomplete="false"
        @tags-changed="update"
        @before-adding-tag="beforeAdd"
    />

    <!-- modal window to create new tag -->
    <div v-if="showModal" class="surveys-modal">
      <transition name="modal">
        <div class="modal-mask">
          <div class="modal-wrapper">
            <div class="modal-dialog" role="document">
              <div class="modal-content">
                <div class="modal-header">
                  <h5 class="modal-title">New Tag</h5>
                </div>
                <div class="modal-body">
                  <p>Create new tag '{{ inputTag }}'?</p>
                </div>
                <div class="modal-footer">
                  <button type="button" class="btn btn-secondary" @click="resetTag()">Close</button>
                  <button type="button" class="btn btn-primary" @click="addNewTag()">Confirm</button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </transition>
    </div>
  </div>
</template>

<script>

import VueTagsInput from "@johmun/vue-tags-input";
import {mapActions, mapState} from "vuex";
import slugify from "slugify";

export default {
  name: 'SurveyTags',
  components: {VueTagsInput},
  data: function() {
    return {
      tagToAdd: null,
      showModal: false,
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
    if (this.surveyTags) {
      this.inputTags = this.surveyTags.map( tag => { return {
          text: tag.name,
          style: tagStyle(tag.name)
        }
      })
    }
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
      getAllTags: 'getAll',
      createTag: 'create'
    }),
    update(newTags) {
      this.inputTags = newTags.map(newTag => { return {
          text: newTag.text,
          style: tagStyle(newTag.text)
        }
      });
      const tagUuids = newTags.map(newTag => this.tags.items.find(tag => tag.name === newTag.text).uuid);
      this.$emit('saveTags', tagUuids);
    },
    beforeAdd(tagObject) {
      console.log("before Add", tagObject); // TODO delete before commit
      if (!this.tags.items.find(tag => tag.name === tagObject.tag.text)) {
        this.showModal = true;
        this.tagToAdd = tagObject;
      } else {
        tagObject.addTag()
      }
    },
    resetTag() {
      this.showModal = false;
      this.inputTag = '';
    },
    addNewTag() {
      this.createTag({
        name: this.tagToAdd.tag.text,
        shortName: slugify(this.tagToAdd.tag.text, {replacement: '-', lower: true, strict: true})
      }).then(
          () => { // success
            this.tagToAdd.addTag();
            this.resetTag();
          },
          () => { // error
            console.error("Failed to create a tag");
            this.resetTag();
          }
      )
    }
  }
}

function tagStyle(name) {
  var hash = 0;
  for (var i = 0; i < name.length; i++) {
    hash = name.charCodeAt(i) + ((hash << 5) - hash);
  }
  const c = (hash & 0x00FFFFFF)
      .toString(16)
      .toUpperCase();
  const color = "00000".substring(0, 6 - c.length) + c;
  return 'background-color: #' + color
}

</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.surveys-modal {
  position: absolute;
}
</style>