<template>
  <div>
    <router-view :key="$route.fullPath"></router-view>
  </div>
</template>

<script>
  import Field from './components/Field.vue';

  export default {
    name: "App",
    components: {Field}
  }

</script>

<style scoped>

</style>
