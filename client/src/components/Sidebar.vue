<template>
  <div class="container-fluid">
    <div class="row">
      <h5 class="w-100">Time Period</h5>
      <ul class="nav nav-pills">
        <li class="nav-item pr-3">
          <a class="nav-link btn btn-secondary" href="#">Today <span class="badge badge-light">2</span></a>
        </li>
        <li class="nav-item pr-3">
          <a class="nav-link" href="#">Last Week <span class="badge badge-secondary">16</span></a>
        </li>
        <li class="nav-item pr-3">
          <a class="nav-link" href="#">All Time <span class="badge badge-secondary">57</span></a>
        </li>
      </ul>
    </div>
    <div class="row my-4">
      <h5 class="w-100">Status</h5>
      <ul class="nav nav-pills">
        <li class="nav-item pr-3">
          <a class="nav-link btn btn-secondary" href="#">All <span class="badge badge-light">42</span></a>
        </li>
        <li class="nav-item pr-3 color-secondary">
          <a class="nav-link" href="#">Completed <span class="badge badge-secondary">42</span></a>
        </li>
        <li class="nav-item pr-3 color-secondary">
          <a class="nav-link" href="#">Incomplete <span class="badge badge-secondary">16</span></a>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Sidebar',
  props: {
    msg: String
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>
