<template>
  <el-dropdown trigger="click" @command="handleLanguage">
    <el-tooltip content="国际化" placement="top">
      <div>
        <svg-icon icon-class="language"  />
      </div>
    </el-tooltip>
    <el-dropdown-menu slot="dropdown">
      <el-dropdown-item v-for="item of sizeOptions" :key="item.value" :disabled="language===item.value" :command="item.value">
        {{ item.label }}
      </el-dropdown-item>
    </el-dropdown-menu>
  </el-dropdown>
</template>

<script>
export default {
  data() {
    return {
      sizeOptions: [
        { label: '中文', value: 'zh' },
        { label: 'English', value: 'en' }
      ]
    }
  },
  created() {
    const historyLanguage = localStorage.getItem('language');
    if (historyLanguage) {
      this.$i18n.locale = historyLanguage;
    }
  },
  computed: {
    language() {
      return this.$i18n.locale
    }
  },
  methods: {
    handleLanguage(language) {
      localStorage.setItem('language', language);
      this.$i18n.locale = language;
    }
  }
}
</script>
