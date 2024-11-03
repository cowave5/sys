<template>
  <div class="app-container">
    <el-form ref="form" :model="form" size="small" :inline="true">
      <el-form-item :label="$t(`dept.label.name`)" prop="userAccount">
        <el-input v-model="form.deptName" disabled/>
      </el-form-item>
      <el-form-item :label="$t(`dept.label.phone`)" prop="userName">
        <el-input v-model="form.deptPhone" disabled/>
      </el-form-item>
    </el-form>

    <h4 class="form-header h4">>> 成员列表</h4>

    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item :label="$t(`role.user.label.name`)" prop="userName">
        <el-input v-model="queryParams.userName" :placeholder="$t(`user.placeholder.name`)" clearable
                  style="width: 240px" @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item :label="$t(`role.user.label.phone`)" prop="userPhone">
        <el-input v-model="queryParams.userPhone" :placeholder="$t(`user.placeholder.phone`)" clearable
                  style="width: 240px" @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{ $t('button.search') }}
        </el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ $t('button.reset') }}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="openSelectUser">
          {{ $t('role.user.button.add') }}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-circle-close" size="mini" @click="cancelAuthUserAll"
                   :disabled="multiple || !checkPermit(['sys:role:grant'])"
        >{{ $t('role.user.button.remove') }}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-close" size="mini" @click="handleClose">{{ $t('button.close') }}
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :row-key="getRowKey" ref="table" @select="selectSingle" :data="list">
      <el-table-column type="selection" :reserve-selection="true" width="50"/>
      <el-table-column :label="$t(`label.index`)" type="index" align="center" width="55">
        <template slot-scope="scope">
          <span>{{ (queryParams.page - 1) * queryParams.pageSize + scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t(`dept.user.name`)" align="center" prop="userName"/>
      <el-table-column :label="$t(`dept.user.rank`)" align="center">
        <template slot-scope="{row: {rank}}">
          <template v-for="item in dict.type.post_level">
            <span v-if="rank === item.value && $i18n.locale==='zh'">{{ item.value }}/{{ item.label }}</span>
            <span v-if="rank === item.value && $i18n.locale==='en'">{{ item.value }}/{{ item.labelEn }}</span>
          </template>
        </template>
      </el-table-column>
      <el-table-column :label="$t(`dept.user.position`)" align="center" prop="postName">
        <template slot-scope="scope">
          <el-select v-model="scope.row.postId" :placeholder="$t(`user.placeholder.role`)">
            <el-option v-for="item in postOptions" :key="item.postId" :label="item.postName" :value="item.postId"/>
          </el-select>
        </template>
      </el-table-column>
      <el-table-column :label="$t(`dept.user.default`)" align="center">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.isDefault" :active-value=1 :inactive-value=0
                     @change="(val)=>handleDefaultChange(val,scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column :label="$t(`dept.user.leader`)" align="center">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.isLeader" :active-value=1 :inactive-value=0
                     @change="(val)=>handleLeaderChange(val,scope.row)"
          />
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.page" :limit.sync="queryParams.pageSize"
                @pagination="getList"
    />

    <el-form label-width="100px">
      <el-form-item style="text-align: center;margin-left:-120px;margin-top:30px;">
        <el-button type="primary" @click="submitForm()"
                   :disabled="!checkPermit(['sys:dept:members'])"
        >{{ $t('button.confirm') }}
        </el-button>
        <el-button @click="close()">{{ $t('button.cancel') }}</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { getDept, getDeptPosts, listMembers, setDeptUsers } from '@/api/system/dept'
import { checkPermit } from '@/utils/permission'

export default {
  name: 'DeptPost',
  dicts: ['post_type', 'post_level'],
  data() {
    return {
      // 遮罩层
      loading: false,
      // 查询参数
      queryParams: {
        page: 1,
        pageSize: 10
      },
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 列表数据
      list: [],
      // 分页总数
      total: 0,
      // 选中数据
      chooseRows: [],
      // 对象id
      infoId: undefined,
      // 对象信息
      form: {},
      // 岗位选项
      postOptions: [],
      // 默认岗位
      defaultPostId: null
    }
  },
  created() {
    this.infoId = this.$route.params && this.$route.params.deptId
    if (this.infoId) {
      getDept(this.infoId).then((resp) => {
        this.form = resp.data
      })
      getDeptPosts(this.infoId).then((resp) => {
        this.postOptions = resp.data
        this.postOptions.forEach((post) => {
          if (post.isDefault === 1) {
            this.defaultPostId = post.postId
          }
        })
      })

      // getDeptUsersById(this.infoId).then((resp) => {
      //   this.chooseRows = resp.data;
      // });
    }
  },
  mounted() {
    this.getList()
  },
  methods: {
    checkPermit,
    selectRow() {
      if (this.infoId) {
        this.$nextTick(() => {
          this.list.forEach((row) => {
            if (row.deptId !== null) {
              this.$refs.table.toggleRowSelection(row, true)
            }
          })
        })
      }
    },
    /** 列表数据 */
    getList() {
      listMembers(this.infoId, this.queryParams.page, this.queryParams.pageSize).then((resp) => {
        this.list = resp.data.list
        this.total = resp.data.total
        this.list.forEach((v) => {
          if (v.deptId === null) {
            v.postId = this.defaultPostId
          }
        })
        this.selectRow()
      })
    },
    /** 选中岗位变化 */
    selectSingle(selection, row) {
      if (selection.findIndex(v => v.userId === row.userId) !== -1) {
        if (this.chooseRows.findIndex(v => v.userId === row.userId) === -1) this.chooseRows.push(row)
      } else {
        const index = this.chooseRows.findIndex(v => v.userId === row.userId)
        this.chooseRows.splice(index, 1)
      }
    },
    /** 单击选中行 */
    clickRow(row) {
      this.$refs.table.toggleRowSelection(row)
    },
    /** 保存选中的数据编号 */
    getRowKey(row) {
      return row.postId
    },
    /** 提交 */
    submitForm() {
      this.chooseRows.forEach((row) => {
        row.deptId = this.infoId
      })
      setDeptUsers(this.chooseRows).then((response) => {
        this.$modal.msgSuccess(this.$t(`msg.success_edit`))
        this.close()
      })
    },
    /** 关闭 */
    close() {
      const obj = { path: '/system/dept' }
      this.$tab.closeOpenPage(obj)
    },
    /** 设置部门负责人 */
    handleLeaderChange(val, row) {
      const index = this.chooseRows.findIndex(v => v.userId === row.userId)
      if (index !== -1) {
        this.chooseRows[index].isLeader = val
      }
    },
    /** 设置用户默认岗位 */
    handleDefaultChange(val, row) {
      const index = this.chooseRows.findIndex(v => v.userId === row.userId)
      if (index !== -1) {
        this.chooseRows[index].isDefault = val
      }
    }
  }
}
</script>
