<template>
  <ul class="nav nav-pills">
    <li class="nav-item">
      <a class="nav-link btn btn-primary" href="#">All <span class="badge badge-light">42</span></a>
    </li>
    <li class="nav-item">
      <a class="nav-link" href="#">Today <span class="badge badge-secondary">16</span></a>
    </li>
  </ul>
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
