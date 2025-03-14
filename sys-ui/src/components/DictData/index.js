import Vue from 'vue'
import DataDict from '@/utils/dict'
import { getDictByType } from '@/api/system/dict'

function install() {
  Vue.use(DataDict, {
    metas: {
      '*': {
        labelField: 'dictLabel',
        valueField: 'dictValue',
        request(dictMeta) {
          return getDictByType(dictMeta.type).then(res => res.data)
        },
      },
    },
  })
}

export default {
  install,
}