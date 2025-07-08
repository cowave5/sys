<template>
  <div class="app-container">
    <!--  筛选栏  -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="数据模块" prop="opModule">
        <el-select v-model="queryParams.scopeModule" style="width: 100%">
          <el-option v-for="item in moduleOptions" :key="item.dictCode" :value="item.dictCode" :label="$t(item.dictName)"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!--  操作栏  -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
                   :disabled="!checkPermit(['sys:post:create'])">
          {{$t('commons.button.create')}}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" @click="handleUpdate"
                   :disabled="single || !checkPermit(['sys:post:edit'])">
          {{$t('commons.button.edit')}}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" @click="handleDelete"
                   :disabled="multiple || !checkPermit(['monitor:log:delete'])">
          {{$t('commons.button.delete')}}
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!--  列表数据  -->
    <el-table ref="tables" v-loading="loading" :data="list" @selection-change="handleSelectionChange" :header-cell-style="{'text-align':'center'}">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" type="index" align="center" width="55">
        <template slot-scope="scope">
          <span>{{(queryParams.page - 1) * queryParams.pageSize + scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column label="数据模块" align="center" prop="opModule">
        <template slot-scope="{row: {scopeModule}}">
          <template v-for="module in moduleOptions">
            <span v-if="scopeModule === module.dictCode">{{ $t(module.dictName) }}</span>
          </template>
        </template>
      </el-table-column>
      <el-table-column label="权限名称" align="center" prop="scopeName"/>
      <el-table-column label="状态" prop="userStatus" align="center" width="60">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.scopeStatus" @change="handleStatusChange(scope.row)"
                     :active-value=1 :inactive-value=0 :disabled="!checkPermit(['sys:user:status'])"/>
        </template>
      </el-table-column>
      <el-table-column :label="$t('commons.label.createTime')" align="center" prop="createTime">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('commons.label.updateTime')" align="center" prop="updateTime">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('commons.label.options')" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">
            {{$t('commons.button.edit')}}
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     :disabled="!checkPermit(['sys:post:delete'])">
            {{$t('commons.button.delete')}}
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-edit">
            编辑权限
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.page" :limit.sync="queryParams.pageSize" @pagination="getList"/>

    <!-- 添加或修改 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="权限名称" prop="scopeName">
          <el-input v-model="form.scopeName" />
        </el-form-item>
        <el-form-item label="数据模块" prop="scopeModule">
          <el-select v-model="form.scopeModule" style="width: 100%">
            <el-option v-for="item in moduleOptions" :key="item.dictCode" :value="item.dictCode" :label="$t(item.dictName)"/>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm" :disabled="!checkPermit(['sys:config:edit'])">
          {{$t('commons.button.confirm')}}
        </el-button>
        <el-button @click="cancel">{{$t('commons.button.cancel')}}</el-button>
      </div>
    </el-dialog>

    <component ref="infoRef" :is="infoView" @ok="handleQuery" />
  </div>
</template>

<script>
import {checkPermit} from "@/utils/permission";
import {getDictByGroup} from "@/api/system/dict";
import {addScope, deleteScope, editScope, intoScope, listScope, updateScopeStatus} from "@/api/system/scope";
export default {
  name: "Scope",
  dicts: [],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 表格数据
      list: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 日期范围
      dateRange: [],
      // 查询参数
      form: {
        scopeName: undefined,
        scopeModule: undefined,
      },
      queryParams: {
        page: 1,
        pageSize: 10,
        scopeModule: undefined,
      },
      moduleOptions: [],
      typeOptions: [],
      viewKey: '',
      infoView: null
    };
  },
  created() {
    this.getOptions();
    this.getList();
  },
  computed: {
    rules() {
      return {
        scopeName: [
          { required: true, message: "权限名称不能为空", trigger: "blur" }
        ],
        scopeModule: [
          { required: true, message: "权限模块不能为空", trigger: "blur" }
        ],
      };
    }
  },
  methods: {
    checkPermit,
    getOptions() {
      getDictByGroup('op_module').then(response => {
        this.moduleOptions = response.data;
      });
    },
    /** 查询列表 */
    getList() {
      this.loading = true;
      listScope(this.queryParams).then(response => {
            this.list = response.data.list;
            this.total = response.data.total;
            this.loading = false;
          }
      );
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.page = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.scopeId)
      this.multiple = !selection.length
    },
    /** 删除 */
    handleDelete(row) {
      const scopeIds = row.scopeId || this.ids;
      this.$modal.confirm('确认删除所选权限？').then(function() {
        return deleteScope(scopeIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 用户状态修改 */
    handleStatusChange(row) {
      let text = row.scopeStatus === 1
          ? "确认启用权限 '" + row.scopeName + "'?"
          : "确认停用权限 '" + row.scopeName + "'?";
      this.$modal.confirm(text).then(function() {
        return updateScopeStatus(row.scopeId, row.scopeStatus);
      }).then(() => {
        this.$modal.msgSuccess(this.$t('commons.msg.success.edit'));
      }).catch(function() {
        row.userStatus = row.userStatus === 0 ? 1 : 0;
      });
    },
    /** 新增 */
    handleAdd() {
      this.form = {
        scopeName: undefined,
        scopeModule: undefined,
      };
      this.title = "新增数据权限";
      this.open = true;
    },
    /** 修改 */
    handleUpdate(row) {
      const scopeId = row.scopeId || this.ids;
      this.title = "修改数据权限";
      intoScope(scopeId).then(response => {
        this.form = response.data;
        this.open = true;
      });
    },
    /** 取消 */
    cancel() {
      this.open = false;
    },
    /** 提交 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.scopeId !== undefined) {
            editScope(this.form).then(() => {
              this.$modal.msgSuccess(this.$t('commons.msg.success.edit'));
              this.open = false;
              this.getList();
            });
          } else {
            addScope(this.form).then(() => {
              this.$modal.msgSuccess(this.$t('commons.msg.success.create'));
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 详情 */
    handleView(row) {
      let types = this.moduleOptions.find(item => item.key === row.opModule).children;
      if (types !== undefined) {
        let viewName = types.find(item => item.key === row.opType).value;
        if (viewName === undefined || viewName === null) {
          viewName = "log_view_" + row.opType;
        }
        this.infoView = this.getDialog(viewName)
        this.$nextTick(() => {
          this.$refs.infoRef.show(row)
        })
      }
    },
    getDialog(viewName) {
      return require(`@/views/monitor/operlog/detail/${viewName}.vue`).default;
    },
  }
};
</script>

