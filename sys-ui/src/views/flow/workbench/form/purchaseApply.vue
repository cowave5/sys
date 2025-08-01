<template>
  <el-dialog :title="title" :visible.sync="visible" width="65%" style="max-height: 80vh;">
    <el-form ref="form" :model="form" :rules="rules" label-width="150px" size="mini" style="float: right; width: 65%">
      <el-form-item label="采购清单" prop="content">
        <el-input v-model="form.content" type="textarea" placeholder="" :rows="8" style="width: 80%" :disabled="task.complete"/>
      </el-form-item>
      <el-form-item label="总金额" prop="total">
        <el-input v-model="form.money" placeholder="" style="width: 80%" :disabled="task.complete"/>
      </el-form-item>
      <el-form-item label="申请人" prop="applyUserName">
        <el-input v-model="form.applyUserName" placeholder="" clearable disabled="disabled" style="width: 80%"/>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button v-if="!task.complete" type="primary" @click="handleAgree">重新提交</el-button>
      <el-button v-if="!task.complete" type="danger" @click="handleReject">撤销</el-button>
      <el-button v-if="!task.complete" @click="cancel">取消</el-button>
    </div>
    <div id="ibox-content" class="col-sm-4">
      <div id="vertical-timeline" class="vertical-container dark-timeline">
        <div v-for="(item, index) in taskRecords" :key="index" class="vertical-timeline-block">
          <div class="vertical-timeline-icon lazur-bg"><i class="fa fa-briefcase"></i></div>
          <div v-if="item.taskType === 'userTask'" class="vertical-timeline-content">
            <h4><b>{{ item.taskName }} </b> ({{ item.assigneeName }})</h4>
            <p>- {{ item.comment }}</p>
            <span class="vertical-date">开始: <small>{{ item.startTime }}</small></span><br>
            <span class="vertical-date">结束: <small>{{ item.endTime }}</small></span>
          </div>
          <div v-if="item.taskType === 'endEvent'" class="vertical-timeline-content">
            <h4><b>结束: </b><span class="vertical-date"><small>{{ item.endTime }}</small></span></h4>
          </div>
        </div>
      </div>
    </div>
  </el-dialog>
</template>

<script>
import {taskComplete, taskRecords} from "@/api/flow/task";
import {infoLeave} from "@/api/flow/leave";
import {getDeptCandidatesByCode} from "@/api/system/dept";
import {instanceJump} from "@/api/flow/manage/instance";
import {editPurchase, infoPurchase} from "@/api/flow/purchase";

export default {
  dicts: [],
  data() {
    return {
      visible: false,
      title: '',
      task: {},
      form: {},
      data: {},
      variables: {},
      taskRecords: [],
      // 参数
      manageResult: true
    };
  },
  computed: {
    rules() {
      return {

      };
    }
  },
  methods: {
    show(row) {
      this.title = row.processName + ' | ' + row.taskName;
      this.task = row;
      this.getInfo(row.businessKey);
      this.getRecords(row.taskId);
      this.visible = true;
    },
    getInfo(id) {
      infoPurchase(id).then( response => {
        this.form = response.data;
      });
    },
    getRecords(id) {
      taskRecords(id).then( response => {
        this.taskRecords = response.data;
      });
    },
    /** 取消 */
    cancel() {
      this.visible = false;
    },
    /** 提交 */
    handleAgree(){
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.data.taskId = this.task.taskId;
          this.data.comment = "重新提交";
          this.variables.reapply = true;
          this.variables.money = this.form.money;
          this.data.variables = this.variables;
          editPurchase(this.form).then( resp => {
            taskComplete(this.data).then( response => {
              this.visible = false;
              this.$emit('ok');
              this.$modal.msgSuccess("采购已重新提交");
            });
          });
        }
      });
    },
    /** 撤销 */
    handleReject(){
      this.data.taskId = this.task.taskId;
      this.data.comment = "撤销";
      this.variables.reapply = false;
      this.data.variables = this.variables;
      taskComplete(this.data).then( response => {
        this.visible = false;
        this.$emit('ok');
        this.$modal.msgSuccess("采购已撤销");
      });
    }
  }
};
</script>

<style scoped>
#vertical-timeline {
  position: relative;
  padding: 0;
  margin-bottom: 2em;
}

#vertical-timeline::before {
  content: '';
  position: absolute;
  top: 0;
  left: 18px;
  height: 100%;
  width: 4px;
  background: #f1f1f1;
}

.lazur-bg {
  background-color: #9cb1b2;
  color: #ffffff;
}

.col-sm-4 {
  width: 30%;
  position: relative;
  min-height: 1px;
  padding-right: 15px;
  padding-left: 15px;
}

.vertical-container {
  width: 90%;
  max-width: 1170px;
  margin: 0 auto;
}

.vertical-container::after {
  content: '';
  display: table;
  clear: both;
}

.vertical-timeline-content {
  float: right;
  width: 85%;
  padding: 1em;
  position: relative;
  background: white;
  border-radius: 0.25em;
}

#vertical-timeline.light-timeline:before {
  background: #e7eaec;
}

.dark-timeline .vertical-timeline-content:before {
  border-color: transparent #f5f5f5 transparent transparent;
}

.dark-timeline.center-orientation .vertical-timeline-content:before {
  border-color: transparent transparent transparent #f5f5f5;
}

.dark-timeline .vertical-timeline-block:nth-child(2n) .vertical-timeline-content:before,  .dark-timeline.center-orientation .vertical-timeline-block:nth-child(2n) .vertical-timeline-content:before {
  border-color: transparent #f5f5f5 transparent transparent;
}

.dark-timeline .vertical-timeline-content, .dark-timeline.center-orientation .vertical-timeline-content {
  background: #f5f5f5;
}

.vertical-timeline-block {
  position: relative;
  margin: 2em 0;
  height: 150px;
}

.vertical-timeline-block:after {
  content: "";
  display: table;
  clear: both;
}

.vertical-timeline-block:first-child {
  margin-top: 0;
}

.vertical-timeline-block:last-child {
  margin-bottom: 0;
}

.vertical-timeline-icon {
  position: absolute;
  top: 0;
  left: 0;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  font-size: 16px;
  border: 3px solid #f1f1f1;
  text-align: center;
}

.vertical-timeline-icon i {
  display: block;
  width: 24px;
  height: 24px;
  position: relative;
  left: 50%;
  top: 50%;
  margin-left: -12px;
  margin-top: -9px;
}

.vertical-timeline-content:after {
  content: "";
  display: table;
  clear: both;
}

.vertical-timeline-content h2 {
  font-weight: 400;
  margin-top: 4px;
}

.vertical-timeline-content p {
  margin: 0.5em 0;
  line-height: 1.5;
}

.vertical-timeline-content .vertical-date {
  font-weight: 400;
}

.vertical-date small {
  color: #1ab394;
  font-weight: 400;
}

.vertical-timeline-content::before {
  content: '';
  position: absolute;
  top: 16px;
  right: 100%;
  height: 0;
  width: 0;
  border: 7px solid transparent;
  border-right: 7px solid white;
}
</style>
