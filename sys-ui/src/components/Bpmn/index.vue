<template>
  <div id="app">
    <div class="container">
      <!-- 创建一个canvas画布 npmn-js是通过canvas实现绘图的，并设置ref让vue获取到element -->
      <div class="bpmn-canvas" ref="canvas"></div>
      <!-- 工具栏显示的地方 -->
      <div class="bpmn-js-properties-panel" id="js-properties-panel"></div>


      <!-- 把操作按钮写在这里面 -->
      <div class="action">
        <el-tooltip effect="light" content="导入Bpmn" placement="top">
          <el-upload action class="upload-demo" :before-upload="openBpmn">
            <el-button icon="el-icon-folder-opened"></el-button>
          </el-upload>
        </el-tooltip>
        <el-tooltip effect="light" content="新建流程" placement="top">
          <el-button class="new" icon="el-icon-circle-plus" @click="newDiagram"></el-button>
        </el-tooltip>
        <el-tooltip effect="light" content="下载Bpmn" placement="top">
          <el-button icon="el-icon-download" @click="downloadBpmn"></el-button>
        </el-tooltip>
        <el-tooltip effect="light" content="下载Svg" placement="top">
          <el-button icon="el-icon-picture" @click="downloadSvg"></el-button>
        </el-tooltip>

        <el-button-group style="margin-left: 10px;">
          <el-button @click="handlerUndo">撤销</el-button>
          <el-button @click="handlerRedo">恢复</el-button>
        </el-button-group>
        <el-button-group style="margin-left: 10px;">
          <el-button @click="handlerZoom(0.1)">放大</el-button>
          <el-button @click="handlerZoom(-0.1)">缩小</el-button>
          <el-button @click="handlerZoom(0)">还原</el-button>
        </el-button-group>
        <el-button-group style="margin-left: 10px;">
          <el-button @click="perviewXML">xml预览</el-button>
          <el-button @click="perviewSVG">svg预览</el-button>
        </el-button-group>

        <!-- 预览弹出 -->
        <el-dialog title="XML预览" append-to-body width="80%" :visible.sync="perviewXMLShow">
          <div style="max-height: 65vh;overflow: auto;">
            <pre v-highlight="perviewXMLStr"><code></code></pre>
          </div>
        </el-dialog>
        <el-dialog title="SVG预览" append-to-body width="80%" :visible.sync="perviewSVGShow">
          <div style="text-align: center;" v-html="perviewSVGData" />
        </el-dialog>

        <a hidden ref="downloadLink"></a>
      </div>
    </div>
  </div>
</template>

<script>
// bpmn 相关依赖
import 'bpmn-js/dist/assets/diagram-js.css'
import 'bpmn-js/dist/assets/bpmn-font/css/bpmn.css'
import 'bpmn-js/dist/assets/bpmn-font/css/bpmn-codes.css'
import 'bpmn-js/dist/assets/bpmn-font/css/bpmn-embedded.css'

// 左边工具栏以及编辑节点的样式
import 'bpmn-js-properties-panel/dist/assets/bpmn-js-properties-panel.css'


import BpmnModeler from "bpmn-js/lib/Modeler";
// 左边工具栏以及节点
import propertiesProviderModule from "bpmn-js-properties-panel/lib/provider/camunda";
// 右边的工具栏
import propertiesPanelModule from "bpmn-js-properties-panel";
import camundaModdleDescriptor from "camunda-bpmn-moddle/resources/camunda";

// BPMN国际化
import customTranslate from './customTranslate/customTranslate';
// 自定义汉化模块
var customTranslateModule = {
    translate: ['value', customTranslate]
};

// XML预览代码高亮
import Highlight from "@/components/highlight/highlight";
import vue from "vue"

export default {
  name: "Bpmn",
  props: {
    /* 编辑器的内容 */
    value: {
      type: String,
      default: "",
    },
    /* 只读 */
    readOnly: {
      type: Boolean,
      default: false,
    },
  },
  components: {

  },
  data() {
    return {
      bpmnModeler: null,
      scale: 1,
      canvas: null,
      bpmnTemplate: `
          <?xml version="1.0" encoding="UTF-8"?>
          <definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI" xmlns:omgdi="http://www.omg.org/spec/DD/20100524/DI" xmlns:omgdc="http://www.omg.org/spec/DD/20100524/DC" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" id="sid-38422fae-e03e-43a3-bef4-bd33b32041b2" targetNamespace="http://bpmn.io/bpmn" exporter="bpmn-js (https://demo.bpmn.io)" exporterVersion="5.1.2">
          <process id="Process_1" isExecutable="false">
              <startEvent id="StartEvent_1y45yut" name="开始">
              <outgoing>SequenceFlow_0h21x7r</outgoing>
              </startEvent>
              <task id="Task_1hcentk">
              <incoming>SequenceFlow_0h21x7r</incoming>
              </task>
              <sequenceFlow id="SequenceFlow_0h21x7r" sourceRef="StartEvent_1y45yut" targetRef="Task_1hcentk" />
          </process>
          <bpmndi:BPMNDiagram id="BpmnDiagram_1">
              <bpmndi:BPMNPlane id="BpmnPlane_1" bpmnElement="Process_1">
              <bpmndi:BPMNShape id="StartEvent_1y45yut_di" bpmnElement="StartEvent_1y45yut">
                  <omgdc:Bounds x="152" y="102" width="36" height="36" />
                  <bpmndi:BPMNLabel>
                  <omgdc:Bounds x="160" y="145" width="22" height="14" />
                  </bpmndi:BPMNLabel>
              </bpmndi:BPMNShape>
              <bpmndi:BPMNShape id="Task_1hcentk_di" bpmnElement="Task_1hcentk">
                  <omgdc:Bounds x="240" y="80" width="100" height="80" />
              </bpmndi:BPMNShape>
              <bpmndi:BPMNEdge id="SequenceFlow_0h21x7r_di" bpmnElement="SequenceFlow_0h21x7r">
                  <omgdi:waypoint x="188" y="120" />
                  <omgdi:waypoint x="240" y="120" />
              </bpmndi:BPMNEdge>
              </bpmndi:BPMNPlane>
          </bpmndi:BPMNDiagram>
          </definitions>
      `,
      perviewXMLStr: null,
      perviewSVGData: null,
      perviewXMLShow: false,
      perviewSVGShow: false,
    };
  },
  methods: {
    newDiagram() {
      this.createNewDiagram(this.bpmnTemplate);
    },

    handlerRedo() {
        this.bpmnModeler.get("commandStack").redo();
    },
    handlerUndo() {
        this.bpmnModeler.get("commandStack").undo();
    },
    handlerZoom(radio) {
        const newScale = !radio ? 1.0 : this.scale + radio;
        this.bpmnModeler.get("canvas").zoom(newScale);
        this.scale = newScale;
    },

    async downloadBpmn() {
      try {
        const { xml } = await this.bpmnModeler.saveXML({ format: true });
        // 获取文件名
        const name = `${this.getFilename(xml)}.bpmn`;
        // 将文件名以及数据交给下载方法
        this.download({ name: name, data: xml });
      } catch (error) {
        this.toast.error('下载Bpmn失败，请重试')
      }
    },
    async downloadSvg() {
      try {
        const { xml } = await this.bpmnModeler.saveXML({ format: true });
        // 获取文件名
        const name = `${this.getFilename(xml)}.svg`;

        // 从建模器画布中提取svg图形标签
        let context = "";
        const djsGroupAll = this.$refs.canvas.querySelectorAll(".djs-group");
        for (let item of djsGroupAll) {
          context += item.innerHTML;
        }
        // 获取svg的基本数据，长宽高
        const viewport = this.$refs.canvas
          .querySelector(".viewport")
          .getBBox();

        // 将标签和数据拼接成一个完整正常的svg图形
        const svg = `
          <svg
            xmlns="http://www.w3.org/2000/svg"
            xmlns:xlink="http://www.w3.org/1999/xlink"
            width="${viewport.width}"
            height="${viewport.height}"
            viewBox="${viewport.x} ${viewport.y} ${viewport.width} ${viewport.height}"
            version="1.1"
            >
            ${context}
          </svg>
        `;
        // 将文件名以及数据交给下载方法
        this.download({ name: name, data: svg });
      } catch (error) {
        this.toast.error('下载Svg失败，请重试')
      }
    },

    openBpmn(file) {
      const reader = new FileReader();
      // 读取File对象中的文本信息，编码格式为UTF-8
      reader.readAsText(file, "utf-8");
      reader.onload = () => {
        //读取完毕后将文本信息导入到Bpmn建模器
        this.createNewDiagram(reader.result);
      };
      return false;
    },

    getFilename(xml) {
      let start = xml.indexOf("process");
      let filename = xml.substr(start, xml.indexOf(">"));
      filename = filename.substr(filename.indexOf("id") + 4);
      filename = filename.substr(0, filename.indexOf('"'));
      return filename;
    },

    download({ name = "diagram.bpmn", data }) {
      // 这里就获取到了之前设置的隐藏链接
      const downloadLink = this.$refs.downloadLink;
      // 把输就转换为URI，下载要用到的
      const encodedData = encodeURIComponent(data);

      if (data) {
        // 将数据给到链接
        downloadLink.href =
          "data:application/bpmn20-xml;charset=UTF-8," + encodedData;
        // 设置文件名
        downloadLink.download = name;
        // 触发点击事件开始下载
        downloadLink.click();
      }
    },
    // XML预览
    async perviewXML() {
      try {
        const { xml } = await this.bpmnModeler.saveXML({ format: true });
        this.perviewXMLStr = xml
        this.perviewXMLShow = true
      } catch (error) {
        this.toast.error('预览失败，请重试')
      }
    },
    // SVG预览
    async perviewSVG() {
      try {
        const { svg } = await this.bpmnModeler.saveSVG();
        this.perviewSVGData = svg
        this.perviewSVGShow = true
      } catch (error) {
        this.toast.error('预览失败，请重试')
      }
    },

    async init() {
      // 获取画布 element
      this.canvas = this.$refs.canvas;

      // 创建Bpmn对象
      this.bpmnModeler = new BpmnModeler({
        // 设置bpmn的绘图容器为上门获取的画布 element
        container: this.canvas,

        // 加入工具栏支持
        propertiesPanel: {
          parent: "#js-properties-panel"
        },
        additionalModules: [propertiesProviderModule, propertiesPanelModule, customTranslateModule],
        moddleExtensions: {
          camunda: camundaModdleDescriptor
        }
      });

      await this.createNewDiagram(this.value?this.value:this.bpmnTemplate);
      // 监听画布点击事件，存储变化值
      var that = this
      this.$refs.canvas.addEventListener("click", async function(){
        try {
          const { xml } = await that.bpmnModeler.saveXML({ format: true });
          that.$emit("input",xml)
        } catch (error) {
          that.$modal.msgError('实时保存失败')
        }
      })
    },
    async createNewDiagram(bpmn) {
      // 将字符串转换成图显示出来;
      try {
      	const result = await this.bpmnModeler.importXML(bpmn);
      } catch (err) {
      	this.$modal.msgError("打开模型出错,请确认该模型符合Bpmn2.0规范");
      }
    },
  },
  mounted() {
    this.init();
    vue.use(Highlight);
  }
};
</script>

<style lang="scss" >

.bpmn-canvas {
  width: 100%;
  height: 100vh;
  background: url('data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNDAiIGhlaWdodD0iNDAiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+PGRlZnM+PHBhdHRlcm4gaWQ9ImEiIHdpZHRoPSI0MCIgaGVpZ2h0PSI0MCIgcGF0dGVyblVuaXRzPSJ1c2VyU3BhY2VPblVzZSI+PHBhdGggZD0iTTAgMTBoNDBNMTAgMHY0ME0wIDIwaDQwTTIwIDB2NDBNMCAzMGg0ME0zMCAwdjQwIiBmaWxsPSJub25lIiBzdHJva2U9IiNlMGUwZTAiIG9wYWNpdHk9Ii4yIi8+PHBhdGggZD0iTTQwIDBIMHY0MCIgZmlsbD0ibm9uZSIgc3Ryb2tlPSIjZTBlMGUwIi8+PC9wYXR0ZXJuPjwvZGVmcz48cmVjdCB3aWR0aD0iMTAwJSIgaGVpZ2h0PSIxMDAlIiBmaWxsPSJ1cmwoI2EpIi8+PC9zdmc+')
    repeat !important;
}

.action {
  position: fixed;
  bottom: 10px;
  left: 10px;
  display: flex;
}
.upload-demo {
  margin-right: 10px;
}
// 右菜单样式(注释隐藏菜单)
// .bpmn-js-properties-panel {
//   position: absolute;
//   top: 0;
//   right: 0px;
//   width: 300px;
//   max-height: 500px;
//   overflow-y: auto;
//   border: 1px solid rgba(0,0,0,0.1);
// }

.djs-palette {
  width: 100px;
}


</style>
