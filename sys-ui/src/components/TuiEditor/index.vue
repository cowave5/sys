<template>
  <div :id="id" />
</template>

<script>
// deps for editor
import Editor from '@toast-ui/editor'
import "@toast-ui/editor/dist/i18n/zh-cn";
import '@toast-ui/editor/dist/toastui-editor.css' // Editor's Style
import defaultOptions from './default-options'

export default {
  name: 'TuiEditor',
  props: {
    value: {
      type: String,
      default: ''
    },
    id: {
      type: String,
      required: false,
      default() {
        return 'tui-editor-' + +new Date() + ((Math.random() * 1000).toFixed(0) + '')
      }
    },
    options: {
      type: Object,
      default() {
        return defaultOptions
      }
    },
    mode: {
      type: String,
      default: 'markdown'
    },
    height: {
      type: String,
      required: false,
      default: '300px'
    },
    language: {
      type: String,
      required: false,
      default: 'zh-CN' // https://github.com/nhnent/tui.editor/tree/master/src/js/langs
    }
  },
  data() {
    return {
      editor: null
    }
  },
  computed: {
    editorOptions() {
      const options = Object.assign({}, defaultOptions, this.options)
      options.initialEditType = this.mode
      options.height = this.height
      options.language = this.language
      return options
    }
  },
  watch: {
    value(newValue, preValue) {
      if (newValue !== preValue && newValue !== this.editor.getMarkdown()) {
        this.editor.setMarkdown(newValue)
      }
    },
    language(val) {
      this.destroyEditor()
      this.initEditor()
    },
    height(newValue) {
      this.editor.height(newValue)
    },
    mode(newValue) {
      this.editor.changeMode(newValue)
    }
  },
  mounted() {
    this.initEditor()
  },
  destroyed() {
    this.destroyEditor()
  },
  methods: {
    initEditor() {
      this.editor = new Editor({
        el: document.getElementById(this.id),
        ...this.editorOptions
      })
      if (this.value) {
        this.editor.setMarkdown(this.value)
      }
      this.editor.on('change', () => {
        this.$emit('input', this.editor.getMarkdown())
      })
    },
    destroyEditor() {
      if (!this.editor) return
      this.editor.off('change')
      this.editor.destroy()
    },
    setValue(value) {
      this.editor.setMarkdown(value)
    },
    getValue() {
      return this.editor.getMarkdown()
    },
    setHtml(value) {
      this.editor.setHtml(value)
    },
    getHtml() {
      return this.editor.getHtml()
    }
  }
}
</script>
