<template>
  <div class="row">
    <div class="col"></div>
    <div class="col-lg-6 col-md-8 col-sm-10">
      <h2>Preferences</h2>
      <div v-if="preferences && tags" class="mt-4">
        <h4 class="my-3">Tags to display on the main page</h4>
        <draggable v-model="tagItems"
                   group="tagPreferences"
                   ghost-class="drag-handle-dragging"
                   @start="onDragStart"
                   @end="onDragEnd">
          <div v-for="tag in tagItems"
               :key="tag.uuid"
               class="form-check p-0 mt-2">
            <div class="d-inline pr-4">
              <svg class="drag-handle" xmlns="http://www.w3.org/2000/svg" height="15" viewBox="0 0 24 24" width="15"><path d="M0 0h24v24H0V0z" fill="none"/><path d="M11 18c0 1.1-.9 2-2 2s-2-.9-2-2 .9-2 2-2 2 .9 2 2zm-2-8c-1.1 0-2 .9-2 2s.9 2 2 2 2-.9 2-2-.9-2-2-2zm0-6c-1.1 0-2 .9-2 2s.9 2 2 2 2-.9 2-2-.9-2-2-2zm6 4c1.1 0 2-.9 2-2s-.9-2-2-2-2 .9-2 2 .9 2 2 2zm0 2c-1.1 0-2 .9-2 2s.9 2 2 2 2-.9 2-2-.9-2-2-2zm0 6c-1.1 0-2 .9-2 2s.9 2 2 2 2-.9 2-2-.9-2-2-2z" fill="#c4c4c4"/></svg>
            </div>
            <input class="form-check-input" type="checkbox" v-model="selectedTags[tag.shortName]" value="" :id="'tag-' + tag.uuid">
            <label class="form-check-label" :for="'tag-' + tag.uuid">
              {{ tag.name }}
            </label>
          </div>
        </draggable>
        <div class="form-check mt-3">
          <input class="form-check-input" v-model="preferences.displayUntagged" type="checkbox" value="" id="untagged">
          <label class="form-check-label" for="untagged">
            Display untagged as "Other"
          </label>
        </div>
      </div>

      <div aria-label="button group" class="btn-toolbar mt-4" role="toolbar">
        <button class="btn btn-primary" type="button" v-on:click="savePreferences()">Save Preferences</button>
      </div>

    </div>
    <div class="col"></div>
  </div>
</template>

<script>

import {mapActions, mapState} from "vuex";
import draggable from 'vuedraggable'
import {router} from "@/router";

export default {
  name: "Preferences",
  components: {draggable},
  data: function() {
    return {
      displayUntagged: false,
      tagItems: []
    }
  },
  created() {
    this.getAllTags()
    this.getPreferences()
  },
  computed: {
    ...mapState({
      account: state => state.account,
      tags: function(state) {
        this.tagItems = state.tags.all.items.slice()
        const layout = state.preferences.all.items.mainPageLayout
        function findOrMax(shortName) {
          const pos = layout.findIndex(t => t.shortName === shortName)
          return pos === -1 ? layout.length : pos
        }
        this.tagItems.sort((t1,t2) => findOrMax(t1.shortName) - findOrMax(t2.shortName))
        return state.tags.all
      },
      preferences: state => state.preferences.all.items,
      selectedTags: state => Object.fromEntries(state.preferences.all.items.mainPageLayout.map((tag) => [tag.shortName, true]))
    })
  },
  methods: {
    ...mapActions('tags', {
      getAllTags: 'getAll',
    }),
    ...mapActions('preferences', {
      getPreferences: 'getAll',
      updatePreferences: 'save'
    }),
    onDragStart() {
    },
    onDragEnd() {
    },
    savePreferences() {
      let selectedTags = this.selectedTags
      this.preferences.mainPageLayout = []
      for (let tag of this.tagItems) {
        if (selectedTags[tag.shortName]) {
          this.preferences.mainPageLayout.push(tag.shortName);
        }
      }
      this.updatePreferences(this.preferences).then(
          // redirect to main page
          () => router.push('/'),
          (error) => console.error("failed to save preferences", error)
      )
    }

  }
}

</script>

<style>
.drag-handle {
  margin-bottom: -2px;
  margin-left: -2px;
  cursor: grab;
}

.drag-handle-dragging {
  cursor: grabbing;
}
</style>
