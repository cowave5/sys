<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item :label="$t('role.label.name')" prop="roleName">
        <el-input v-model="queryParams.roleName" :placeholder="$t('role.placeholder.name')" clearable style="width: 240px" @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item :label="$t('role.label.code')" prop="roleCode">
        <el-input v-model="queryParams.roleCode" :placeholder="$t('role.placeholder.code')" clearable style="width: 240px" @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{$t('commons.button.search')}}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{$t('commons.button.reset')}}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
                   :disabled="!checkPermit(['sys:role:create'])">{{$t('commons.button.create')}}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" @click="handleUpdate"
                   :disabled="single || !checkPermit(['sys:role:edit'])">{{$t('commons.button.edit')}}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" @click="handleDelete"
                   :disabled="multiple || !checkPermit(['sys:role:delete'])">{{$t('commons.button.delete')}}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
                   :disabled="!checkPermit(['sys:role:export'])">{{$t('commons.button.export')}}</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"/>
    </el-row>

    <el-table v-loading="loading" :data="roleList" @selection-change="handleSelectionChange">
      <el-table-column :selectable='selectable' type="selection" align="center" width="60" />
      <el-table-column :label="$t('commons.label.index')" type="index" align="center" width="60" >
        <template slot-scope="scope">
          <span>{{(queryParams.page - 1) * queryParams.pageSize + scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('role.label.name')" prop="roleName" align="center" :show-overflow-tooltip="true"/>
      <el-table-column :label="$t('role.label.code')" prop="roleCode" align="center" :show-overflow-tooltip="true"/>
      <el-table-column :label="$t('commons.label.date.create')" align="center" prop="createTime">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('commons.label.options')" align="center" class-name="small-padding fixed-width" width="350">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">{{$t('commons.button.edit')}}</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     :disabled="scope.row.roleCode === 'sysAdmin' || !checkPermit(['sys:role:delete'])">{{$t('commons.button.delete')}}</el-button>
          <el-button size="mini" type="text" @click="handleMenuScope(scope.row)"><svg-icon icon-class="pscope"/>{{$t('role.button.menus')}}</el-button>
          <el-button size="mini" type="text" @click="handleDataScope(scope.row)"
                     :disabled="!checkPermit(['sys:role:scope'])"><svg-icon icon-class="vscope"/> {{$t('role.button.scope')}}</el-button>
          <el-button size="mini" type="text" @click="handleAuthUser(scope.row)"><svg-icon icon-class="peoples"/> {{$t('role.button.grant')}}</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.page" :limit.sync="queryParams.pageSize" @pagination="getList"/>

    <!-- 新增修改对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item :label="$t('role.label.name')" prop="roleName">
          <el-input v-model="form.roleName" :placeholder="$t('role.placeholder.name')" />
        </el-form-item>
        <el-form-item :label="$t('role.label.code')" prop="roleCode">
          <el-input v-model="form.roleCode" :placeholder="$t('role.placeholder.code')" />
        </el-form-item>
        <el-form-item :label="$t('commons.label.remark')">
          <el-input v-model="form.remark" type="textarea" placeholder="..."></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm"
                   :disabled="form.roleCode === 'sysAdmin' || !checkPermit(['sys:role:edit'])">{{$t('commons.button.confirm')}}</el-button>
        <el-button @click="cancel">{{$t('commons.button.cancel')}}</el-button>
      </div>
    </el-dialog>

    <!-- 菜单权限对话框 -->
    <el-dialog :title="$t('role.button.menus')" :visible.sync="openMenuScope" width="700px" append-to-body>
      <el-form ref="form" :model="form" label-width="100px">
        <el-row>
          <el-col :span="8" :offset="0">
            <el-form-item :label="$t('role.label.name')" prop="roleName">
              <el-input  v-model="form.roleName" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="8" :offset="2">
            <el-form-item :label="$t('role.label.code')" prop="roleCode">
              <el-input v-model="form.roleCode" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item :label="$t('menu.button.select')">
          <el-checkbox v-model="menuExpand" @change="handleCheckedTreeExpand($event, 'menu')">{{$t('commons.button.expand')}}/{{$t('commons.button.collapse')}}</el-checkbox>
          <el-checkbox v-model="menuNodeAll" @change="handleCheckedTreeNodeAll($event, 'menu')">{{$t('commons.button.check')}}</el-checkbox>
          <el-checkbox v-model="form.menuCheckStrictly" @change="handleCheckedTreeConnect($event, 'menu')">{{$t('commons.button.parent')}}</el-checkbox>
          <el-tree ref="menu" :data="menuOptions" node-key="id" class="tree-border" show-checkbox
                   :check-strictly="!form.menuCheckStrictly" empty-text="..." :props="defaultProps">
            <span class="el-tree-node__label" slot-scope="{data}"
                  style="display: flex; justify-content: space-between; width: 100%;">
              {{ $t(data.label) }}
              <el-select v-if="data.menuType === 'B'" v-model="data.scope" placeholder="选择数据权限" size="small" class="tree-node-select">
                <el-option v-for="option in selectOptions" :key="option.value" :label="option.label" :value="option.value"/>
              </el-select>
            </span>
          </el-tree>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitMenuScope"
                   :disabled="form.roleCode === 'sysAdmin' || !checkPermit(['sys:role:menus'])">{{$t('commons.button.confirm')}}</el-button>
        <el-button @click="cancelMenuScope">{{$t('commons.button.cancel')}}</el-button>
      </div>
    </el-dialog>

    <!-- 数据权限对话框 -->
    <el-dialog :title="$t('role.button.scope')" :visible.sync="openDataScope" width="500px" append-to-body>
      <span>Todo in the future.</span>
<!--      <el-form :model="form" label-width="100px">-->
<!--        <el-form-item :label="$t('role.label.name')">-->
<!--          <el-input v-model="form.roleName" :disabled="true" />-->
<!--        </el-form-item>-->
<!--        <el-form-item :label="$t('role.label.code')">-->
<!--          <el-input v-model="form.roleCode" :disabled="true" />-->
<!--        </el-form-item>-->
<!--      </el-form>-->
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitDataScope">{{$t('commons.button.confirm')}}</el-button>
        <el-button @click="cancelDataScope">{{$t('commons.button.cancel')}}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getRoleList,
  getRoleInfo,
  delRole,
  addRole,
  updateRole,
  updateRoleMenus
} from '@/api/system/role'
import {getMenuTree} from "@/api/system/menu";
import {checkPermit} from "@/utils/permission";

export default {
  name: "Role",
  dicts: [],
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
      // 总条数
      total: 0,
      // 角色表格数据
      roleList: [],
      // 弹出层标题
      title: "",
      // 新增修改对话框
      open: false,
      // 数据权限对话框
      openDataScope: false,
      // 菜单权限对话框
      openMenuScope: false,
      menuExpand: false,
      menuNodeAll: false,
      // 菜单列表
      menuOptions: [],
      // 查询参数
      queryParams: {
        page: 1,
        pageSize: 10,
        roleName: undefined,
        roleCode: undefined
      },
      // 表单参数
      form: {},
      defaultProps: {
        children: "children",
        label: "label"
      },
      selectOptions: [
      { value: "option1", label: "选项1" },
      { value: "option2", label: "选项2" },
    ],
    };
  },
  created() {
    this.getList();
  },
  computed: {
    rules() {
      return {
        roleName: [
          { required: true, message: this.$t('role.rules.name'), trigger: "blur" }
        ],
        roleCode: [
          { required: true, message: this.$t('role.rules.code'), trigger: "blur" }
        ],
      };
    }
  },
  methods: {
    checkPermit,
    selectable(row, rowIndex){
      return row.roleCode !== 'sysAdmin';
    },
    /** 表单重置 */
    reset() {
      if (this.$refs.menu !== undefined) {
        this.$refs.menu.setCheckedKeys([]);
      }
      this.menuExpand = false,
      this.menuNodeAll = false,
      this.form = {
        roleId: undefined,
        roleName: undefined,
        roleCode: undefined,
        remark: undefined,
        menuIds: [],
        deptIds: [],
        menuCheckStrictly: true,
        deptCheckStrictly: true
      };
      this.resetForm("form");
    },
    /** 搜索 */
    handleQuery() {
      this.queryParams.page = 1;
      this.getList();
    },
    /** 重置 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 多选框 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.roleId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 角色列表 */
    getList() {
      this.loading = true;
      getRoleList(this.queryParams).then(response => {
          this.roleList = response.data.list;
          this.total = response.data.total;
          this.loading = false;
        }
      );
    },
    /** 新增 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = this.$t('role.dialog.new');
    },
    /** 修改 */
    handleUpdate(row) {
      this.reset();
      const roleId = row.roleId || this.ids
      getRoleInfo(roleId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = this.$t('role.dialog.edit');
      });
    },
    /** 删除 */
    handleDelete(row) {
      const roleIds = row.roleId || this.ids;
      const msg = row.roleId
          ? this.$t('role.confirm.delete', { arg1: row.roleName })
          : this.$t('role.confirm.delete_select');
      this.$modal.confirm(msg).then(function() {
        return delRole(roleIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t('commons.msg.success.delete'));
      }).catch(() => {});
    },
    /** 导出 */
    handleExport() {
      this.download('/admin/api/v1/role/export', {}, `role_${new Date().getTime()}.xlsx`)
    },
    /** 新增修改取消 */
    cancel() {
      this.open = false;
      this.reset();
    },
    /** 新增修改提交 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.roleId !== undefined) {
            updateRole(this.form).then(response => {
              this.$modal.msgSuccess(this.$t('commons.msg.success.edit'));
              this.open = false;
              this.getList();
            });
          } else {
            addRole(this.form).then(response => {
              this.$modal.msgSuccess(this.$t('commons.msg.success.create'));
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 角色菜单 */
    handleMenuScope(row){
      this.reset();
      getMenuTree().then(response => {
        this.menuOptions = response.data;
        getRoleInfo(row.roleId).then(response => {
          this.form.roleId = response.data.roleId;
          this.form.roleName = response.data.roleName;
          this.form.roleCode = response.data.roleCode;
          let menuIds = response.data.menuIds;
          menuIds.forEach((v) => {
            this.$nextTick(()=>{
              this.$refs.menu.setChecked(v, true ,false);
            })
          })
        });
      });
      this.openMenuScope = true;
    },
    /** 角色菜单取消 */
    cancelMenuScope(){
      this.openMenuScope = false;
      this.reset();
    },
    /** 角色菜单提交 */
    submitMenuScope(){
      this.form.menuIds = this.getCheckedMenus();
      updateRoleMenus(this.form).then(() => {
        this.$modal.msgSuccess(this.$t('commons.msg.success.edit'));
        this.openMenuScope = false;
        this.getList();
      });
    },
    /** 选中的菜单 */
    getCheckedMenus() {
      // 目前被选中的节点
      let checkedKeys = this.$refs.menu.getCheckedKeys();
      // 半选中的节点
      let halfCheckedKeys = this.$refs.menu.getHalfCheckedKeys();
      checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys);
      // let checkedMenus = checkedKeys.map(id => {
      //   let node = this.findNodeById(id, this.menuOptions) // 查找对应节点
      //   return {
      //     id: id,
      //     scope: node ? node.scope || '' : '' // 确保 scope 有值
      //   }
      // })
      return checkedKeys;
    },
    findNodeById(id, nodes) {
      for (let node of nodes) {
        if (node.id === id) return node
        if (node.children) {
          let found = this.findNodeById(id, node.children)
          if (found) return found
        }
      }
      return null
    },
    /** 展开/折叠 */
    handleCheckedTreeExpand(value, type) {
      let treeList = this.menuOptions;
      for (let i = 0; i < treeList.length; i++) {
        this.$refs.menu.store.nodesMap[treeList[i].id].expanded = value;
      }
    },
    /** 全选/全不选 */
    handleCheckedTreeNodeAll(value, type) {
      this.$refs.menu.setCheckedNodes(value ? this.menuOptions: []);
    },
    /** 父子联动 */
    handleCheckedTreeConnect(value, type) {
      this.form.menuCheckStrictly = value ? true: false;
    },
    /** 数据权限 */
    handleDataScope(row) {
      this.reset();
      getRoleInfo(row.roleId).then(response => {
        this.form.roleId = response.data.roleId;
        this.form.roleName = response.data.roleName;
        this.form.roleCode = response.data.roleCode;
        this.openDataScope = true;
        // this.$nextTick(() => {
        //   roleDeptTreeselect.then(res => {
        //     this.$refs.dept.setCheckedKeys(res.checkedKeys);
        //   });
        // });
      });
    },
    /** 数据权限取消 */
    cancelDataScope() {
      this.openDataScope = false;
      this.reset();
    },
    /** 数据权限提交 */
    submitDataScope: function() {
      this.openDataScope = false;
      // if (this.form.roleId !== undefined) {
      //   this.form.deptIds = this.getDeptAllCheckedKeys();
      //   dataScope(this.form).then(response => {
      //     this.$modal.msgSuccess("修改成功");
      //     this.openDataScope = false;
      //     this.getList();
      //   });
      // }
    },
    /** 分配用户 */
    handleAuthUser: function(row) {
      const roleId = row.roleId;
      this.$router.push("/system/role-auth/user/" + roleId);
    },
  }
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.tree-node-select {
  ::v-deep .el-input__inner {
    height: 20px;
    border: none;
    box-shadow: none;
  }
}
</style>
