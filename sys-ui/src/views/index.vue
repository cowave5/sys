<template>
  <div class="app-container home">
    <el-row :gutter="20">
      <el-col :sm="24" :lg="12" style="padding-left: 20px">
        <h2>Cowave管理系统</h2>
        <p>
          初衷希望写一款适合自己业务，简单通用，并且方便扩展定制的管理系统，也看了一些优秀的开源项目，最后选择了若依作为参考来进行设计开发。
        </p>
        <p>
          非常感谢若依的开源，站在别人的肩膀上继续做了一些优化和设计，主要是对服务和存储进行了重写，前端基本复用了若依的框架。
        </p>
        <p>
          <b>若依版本:</b> <span>3.8.2@<a href="http://vue.ruoyi.vip" target="_blank" style="color: #409eff;">http://vue.ruoyi.vip</a></span>
        </p>
        <p>
          <b>当前版本:</b> <span>{{ version }}</span>
        </p>
      </el-col>
      <el-col :sm="24" :lg="12" style="padding-left: 50px">
        <el-row>
          <el-col :span="12">
            <h2>技术选型</h2>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="5">
            <h4>服务模块</h4>
            <ul>
              <li><label style="font-weight: normal; display:inline-block; width: 80px">sys-blog     </label>80</li>
              <li><label style="font-weight: normal; display:inline-block; width: 80px">sys-ui       </label>81</li>
              <li><label style="font-weight: normal; display:inline-block; width: 80px">sys-nacos        </label>8848</li>
              <li><label style="font-weight: normal; display:inline-block; width: 80px">sys-gateway  </label>19000</li>
              <li><label style="font-weight: normal; display:inline-block; width: 80px">sys-admin    </label>19010</li>
              <li><label style="font-weight: normal; display:inline-block; width: 80px">sys-flow     </label>19020</li>
              <li><label style="font-weight: normal; display:inline-block; width: 80px">sys-code     </label>19030</li>
              <li><label style="font-weight: normal; display:inline-block; width: 80px">sys-quartz   </label>19040</li>
              <li><label style="font-weight: normal; display:inline-block; width: 80px">sys-meter </label>19050</li>
            </ul>
          </el-col>
          <el-col :span="4">
            <h4>构建部署</h4>
            <ul>
              <li>maven</li>
              <li>liquibase</li>
              <li>smart-doc</li>
              <li>nginx</li>
              <li>docker-compose</li>
            </ul>
          </el-col>
          <el-col :span="4">
            <h4>存储服务</h4>
            <ul>
              <li>Postgres</li>
              <li>Elasticsearch</li>
              <li>Redis</li>
              <li>Kafka</li>
              <li>Minio</li>
            </ul>
          </el-col>
          <el-col :span="4">
            <h4>前端选型</h4>
            <ul>
              <li>Vue 2.x</li>
              <li>Axios</li>
              <li>Element-ui</li>
              <li>Sass</li>
              <li>Quill</li>
              <li>socket.io</li>
              <li>...</li>
            </ul>
          </el-col>
          <el-col :span="5">
            <h4>后端选型</h4>
            <ul>
              <li>Java 17</li>
              <li>Spring-boot 2.7.0</li>
              <li>Nacos 2.3.0</li>
              <li>Flowable 6.8.0</li>
              <li>Mybatis-plus</li>
              <li>Quartz</li>
              <li>...</li>
            </ul>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
    <el-divider />

    <el-row :gutter="20">
      <el-col :sm="24" :lg="12" style="padding-left: 20px">
        <el-row>
          <el-col :span="24">
            <h3>更新日志：</h3>
            <h4>1.0.3</h4>
            <hr style="margin: 3px 0;">
            <ul style="list-style-type: disc; padding-left: 15px;">
              <li>流程设计（可视化编辑/部署等）</li>
              <li>流程实例（流程进度/操作记录/过程变量查询修改/挂起/激活/待办/办理人修改/节点跳转等）</li>
              <li>流程示例：请假申请(审批操作/节点事件触发/表单操作)、会议预约(会签)、采购申请(子流程操作)</li>
            </ul>
            <h4>1.0.2</h4>
            <hr style="margin: 3px 0;">
            <ul style="list-style-type: disc; padding-left: 15px;">
              <li>支持Ldap方式认证</li>
            </ul>
            <h4>1.0.1</h4>
            <hr style="margin: 3px 0;">
            <ul style="list-style-type: disc; padding-left: 15px;">
              <li>支持OAuth2方式，使用Gitlab认证</li>
            </ul>
            <h4>1.0.0</h4>
            <hr style="margin: 3px 0;">
            <ul style="list-style-type: disc; padding-left: 15px;">
              <li>登录/注册/验证码/国际化</li>
              <li>用户/角色/部门/岗位/菜单/字典/系统参数/系统消息/操作日志/操作权限</li>
              <li>代码模板、工程模板、SQL转换（Mysql/Postgres）、定时任务（Quartz）</li>
            </ul>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
  </div>
</template>
<script>
export default {
  name: "Index",
  dicts: [],
  data() {
    return {
      version: "",
      isPermissionVisible: false
    };
  },
  created() {
    this.version = process.env.VUE_APP_VERSION;
  },
  methods: {
    togglePermission() {
      this.isPermissionVisible = !this.isPermissionVisible;
    },
  },
};
</script>

<style scoped lang="scss">
.el-table {
  cursor: pointer;
}

.red-point{
  position: relative;
}

.red-point::before{
  content: " ";
  border: 4px solid #1890ff;
  border-radius:3px;
  position: absolute;
  z-index: 1000;
  left: 0;
  top: 1px;
  margin-left: -10px;
}

.home {
  blockquote {
    padding: 10px 20px;
    margin: 0 0 20px;
    font-size: 17.5px;
    border-left: 5px solid #eee;
  }
  hr {
    margin-top: 20px;
    margin-bottom: 20px;
    border: 0;
    border-top: 1px solid #eee;
  }
  .col-item {
    margin-bottom: 20px;
  }

  ul {
    padding: 0;
    margin: 0;
  }

  font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;

  ul {
    list-style-type: none;
    padding-bottom: 2px;
  }

  h3 {
    margin-top: 0px;
    font-size: 15px;
    font-weight: 300;
  }

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    margin-top: 10px;

    b {
      font-weight: 700;
    }
  }

  .permission-toggle-enter-active, .permission-toggle-leave-active {
    transition: opacity 1s, transform 1s;
  }

  .permission-toggle-enter, .permission-toggle-leave-to /* .permission-toggle-leave-active below version 2.1.8 */ {
    opacity: 0;
    transform: translateY(-10px);
  }
}
</style>

