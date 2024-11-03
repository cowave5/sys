<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>

export default  {
  name: 'App',
  metaInfo() {
    return {
      title: this.$store.state.settings.dynamicTitle && this.$store.state.settings.title,
      titleTemplate: title => {
        return title ? this.$t(title) + "/" + this.$t(`route.title`) : this.$t(`route.title`)
      }
    }
  },
  created() {
    this.$store.dispatch('OpenSocket');
  },
  beforeDestroy() {
    this.$store.dispatch('CloseSocket');
  },
}
</script>
