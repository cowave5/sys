<template>
  <div class="app-container">
    <!--  筛选栏  -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="auto">
      <el-form-item label="字典名称" prop="dictName">
        <el-input v-model="queryParams.dictName" placeholder="请输入字典名称" clearable style="width: 240px" @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="字典码" prop="dictCode">
        <el-input v-model="queryParams.dictCode" placeholder="请输入字典码" clearable style="width: 240px" @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{$t('commons.button.search')}}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{$t('commons.button.reset')}}</el-button>
      </el-form-item>
    </el-form>

    <!--  操作栏  -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-sort" size="mini" @click="toggleExpandAll">
                   {{$t('commons.button.expand')}}/{{$t('commons.button.collapse')}}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
                   :disabled="!checkPermit(['sys:dict:create'])">{{$t('commons.button.create')}}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" @click="handleDelete"
                   :disabled="multiple || !checkPermit(['sys:dict:delete'])">{{$t('commons.button.delete')}}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
                   :disabled="!checkPermit(['sys:dict:export'])">{{$t('commons.button.export')}}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-refresh" size="mini" @click="handleRefreshCache"
                   :disabled="!checkPermit(['sys:dict:cache'])">{{$t('commons.button.cache')}}</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!--  列表数据  -->
    <el-table v-if="refreshTable" :data="list" row-key="dictCode" @selection-change="handleSelectionChange"
              :default-expand-all="isExpandAll" :tree-props="{children: 'children', hasChildren: 'hasChildren'}" v-loading="loading">
      <el-table-column  type="selection" width="55" align="center" />
      <el-table-column label="字典名称" align="left">
        <template slot-scope="scope">
          <span v-if="$i18n.locale==='zh' && scope.row.css === null" >{{scope.row.dictLabel}}</span>
          <el-tag v-if="$i18n.locale==='zh' && scope.row.css !== null" :type="scope.row.css">{{scope.row.dictLabel}}</el-tag>
          <span v-if="$i18n.locale==='en' && scope.row.css === null" >{{scope.row.dictEn}}</span>
          <el-tag v-if="$i18n.locale==='en' && scope.row.css !== null" :type="scope.row.css">{{scope.row.dictEn}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="字典码" prop="dictCode" align="center"/>
      <el-table-column :label="$t('dict.label.value')" align="center" prop="dictValue" :show-overflow-tooltip="true"/>
      <el-table-column :label="$t('dict.label.order')" prop="dictOrder" align="center" />
      <el-table-column :label="$t('commons.label.status')" prop="status" align="center" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_is_enable" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column :label="$t('commons.label.readonly')" prop="readOnly" align="center">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.readOnly" :active-value=1 :inactive-value=0 @change="handleReadOnly(scope.row)"
                     :disabled="scope.row.dictCode === 'group' || !checkPermit(['route.common.readonly'])"/>
        </template>
      </el-table-column>
      <el-table-column :label="$t('commons.label.options')" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-plus" @click="handleAdd(scope.row)"
                     v-if="scope.row.groupCode === 'root' || scope.row.groupCode === 'group'">{{$t('commons.button.create')}}</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">{{$t('commons.button.edit')}}</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     :disabled="scope.row.readOnly === 1 || !checkPermit(['sys:dict:delete'])">{{$t('commons.button.delete')}}</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="auto">
        <el-row>
          <el-col :span="24">
            <el-form-item label="上级字典">
              <el-input v-if="$i18n.locale==='zh'" :value="`${form.typeName} | ${form.typeCode}`" :disabled="true"/>
              <el-input v-if="$i18n.locale==='en'" :value="`${form.typeEn} | ${form.typeCode}`" :disabled="true"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="字典名称" prop="dictLabel">
              <el-input v-model="form.dictLabel" :placeholder="$t('dict.placeholder.name')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('dict.label.code')" prop="dictCode">
              <el-input v-model="form.dictCode" :placeholder="$t('dict.placeholder.code')" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('dict.label.english')" prop="dictEn">
              <el-input v-model="form.dictEn" :placeholder="$t('dict.placeholder.english')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('dict.label.value')" prop="dictValue">
              <el-input v-model="form.dictValue" :placeholder="$t('dict.placeholder.value')" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('dict.label.css')" prop="css">
              <el-select v-model="form.css" style="width: 230px">
                <el-option v-if="$i18n.locale==='zh'"   v-for="item in cssOptions" :key="item.value" :value="item.value" :label="item.label"/>
                <el-option v-if="$i18n.locale==='en'"   v-for="item in cssOptions" :key="item.value" :value="item.value" :label="item.labelEn"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('dict.label.order')" prop="dictOrder">
              <el-input-number v-model="form.dictOrder" controls-position="right" :min="0" style="width: 230px"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('commons.label.status')" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio v-if="$i18n.locale==='zh'" v-for="dict in dict.type.sys_is_enable" :key="dict.value" :label="dict.value">{{dict.label}}</el-radio>
                <el-radio v-if="$i18n.locale==='en'" v-for="dict in dict.type.sys_is_enable" :key="dict.value" :label="dict.value">{{dict.labelEn}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('dict.label.default')" prop="isDefault">
              <el-radio-group v-model="form.isDefault">
                <el-radio v-if="$i18n.locale==='zh'" v-for="dict in dict.type.sys_yes_no" :key="dict.value" :label="dict.value">{{dict.label}}</el-radio>
                <el-radio v-if="$i18n.locale==='en'" v-for="dict in dict.type.sys_yes_no" :key="dict.value" :label="dict.value">{{dict.labelEn}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item prop="valueParser">
          <span slot="label">
            <el-tooltip :content="$t('config.text.parser')" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
            {{$t('config.label.parser')}}
          </span>
          <el-input v-model="form.valueParser" :placeholder="$t('config.placeholder.parser')" />
        </el-form-item>
        <el-form-item prop="valueParam">
          <span slot="label">
            <el-tooltip :content="$t('config.text.param')" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
            {{ $t('config.label.param') }}
          </span>
          <el-input v-model="form.valueParam" :placeholder="$t('config.placeholder.param')"/>
        </el-form-item>
        <el-form-item :label="$t('commons.label.remark')" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="..."></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm"
                   :disabled="form.readOnly === 1 || !checkPermit(['sys:dict:edit'])">{{$t('commons.button.confirm')}}</el-button>
        <el-button @click="cancel">{{$t('commons.button.cancel')}}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {checkPermit} from "@/utils/permission";
import {
  addDict,
  delDict,
  getDictInfo,
  getDictList,
  refreshDict,
  updateDict,
  updateReadonly
} from '@/api/system/dict'

export default {
  name: "Dict",
  dicts: ['sys_is_enable', 'sys_yes_no'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 重新渲染表格状态
      refreshTable: true,
      // 是否展开
      isExpandAll: false,
      // 总条数
      total: 0,
      // 字典表格数据
      list: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 字典标签样式
      cssOptions: [
        {value: null, label: "默认", labelEn: "default"},
        {value: "primary", label: "主要", labelEn: "primary"},
        {value: "success", label: "成功", labelEn: "success"},
        {value: "info", label: "信息", labelEn: "info"},
        {value: "warning", label: "警告", labelEn: "warning"},
        {value: "danger", label: "危险", labelEn: "danger"},
      ],
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        dictName: undefined,
        dictCode: undefined
      },
      // 表单参数
      form: {},
    };
  },
  created() {
    this.getList();
  },
  computed: {
    rules() {
      return {
        groupCode: [
          { required: true, message: this.$t('dict.rules.groupcode'), trigger: "blur" }
        ],
        typeCode: [
          { required: true, message: this.$t('dict.rules.typecode'), trigger: "blur" }
        ],
        dictLabel: [
          { required: true, message: this.$t('dict.rules.name'), trigger: "blur" }
        ],
        dictEn: [
          { required: true, message: this.$t('dict.rules.english'), trigger: "blur" }
        ],
        dictCode: [
          { required: true, message: this.$t('dict.rules.code'), trigger: "blur" }
        ],
        dictValue: [
          { required: true, message: this.$t('dict.rules.value'), trigger: "blur" }
        ],
      };
    }
  },
  methods: {
    checkPermit,
    /** 表单重置 */
    reset() {
      this.form = {
        id: undefined,
        groupCode: undefined,
        typeCode: undefined,
        status: 1,
        isDefault: 0,
        dictOrder: 1,
        valueParser: undefined,
        valueParam: undefined,
        remark: undefined
      };
      this.resetForm("form");
    },
    /** 多选框 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 展开/折叠 */
    toggleExpandAll() {
      this.refreshTable = false;
      this.isExpandAll = !this.isExpandAll;
      this.$nextTick(() => {
        this.refreshTable = true;
      });
    },
    /** 搜索 */
    handleQuery() {
      this.getList();
    },
    /** 重置 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 列表 */
    getList() {
      this.loading = true;
      getDictList(this.queryParams).then(response => {
          this.list = this.handleTree(response.data, "dictCode", "typeCode");
          this.loading = false;
        }
      );
    },
    /** 新增 */
    handleAdd(row) {
      this.reset();
      this.form.typeCode = row.dictCode ? row.dictCode : "group";
      this.form.typeName = row.dictLabel ? row.dictLabel : "分组字典";
      this.form.typeEn = row.dictEn ? row.dictEn : "Group dict";
      this.open = true;
      this.title = this.$t('dict.dialog.new');
    },
    /** 修改 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getDictInfo(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = this.$t('dict.dialog.edit');
      });
    },
    /** 删除 */
    handleDelete(row) {
      const dictIds = row.id || this.ids;
      const msg = row.id
          ? this.$t('dict.confirm.delete', { arg1: row.dictCode })
          : this.$t('dict.confirm.delete_select');
      this.$modal.confirm(msg).then(function() {
        return delDict(dictIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t('commons.msg.success.delete'));
      }).catch(() => {});
    },
    /** 类型只读修改 */
    handleReadOnly(row) {
      let text = row.readOnly === 1
          ? this.$t('dict.confirm.readonly_set', { arg1: row.dictCode })
          : this.$t('dict.confirm.readonly_cancel', { arg1: row.dictCode });
      this.$modal.confirm(text).then(function() {
        return updateReadonly(row.id, row.readOnly);
      }).then(() => {
        this.$modal.msgSuccess(this.$t('commons.msg.success.edit'));
      }).catch(function() {
        row.readOnly = row.readOnly === 0 ? 1 : 0;
      });
    },
    /** 导出 */
    handleExport() {
      this.download('/admin/api/v1/dict/export', {}, this.$t('dict.text.data') + `_${new Date().getTime()}.xlsx`)
    },
    /** 刷新缓存 */
    handleRefreshCache() {
      refreshDict().then(() => {
        this.$modal.msgSuccess(this.$t('commons.msg.success.refresh'));
      });
    },
    /** 取消 */
    cancel() {
      this.open = false;
      this.reset();
    },
    /** 提交 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id !== undefined) {
            updateDict(this.form).then(() => {
              this.$modal.msgSuccess(this.$t('commons.msg.success.edit'));
              this.open = false;
              this.getList();
            });
          } else {
            addDict(this.form).then(() => {
              this.$modal.msgSuccess(this.$t('commons.msg.success.create'));
              this.open = false;
              this.getList();
            });
          }
        }
      });
    }
  }
};
</script>
