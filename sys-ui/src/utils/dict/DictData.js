/**
 * @classdesc 字典数据
 * @property {String} label 标签
 * @property {*} value 标签
 * @property {Object} raw 原始数据
 */
export default class DictData {
  constructor(label, labelEn, value, css, raw) {
    this.label = label
    this.labelEn = labelEn
    this.value = value
    this.css = css
    this.raw = raw
  }
}
