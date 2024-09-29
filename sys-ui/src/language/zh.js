export default {
  commons: {
    label: {
      index: "序号",
      status: "状态",
      remark: "备注",
      options: "操作",
      readonly: "只读",
      time: {
        begin: "开始时间",
        end: "结束时间",
      },
      date: {
        create: "创建时间",
        begin: "开始日期",
        end: "结束日期",
      }
    },
    button: {
      refresh: "刷新",
      search: "搜索",
      search_hide: "隐藏搜索",
      search_show: "显示搜索",
      reset: "重置",
      hide: "隐藏",
      show: "显示",
      enable: "启用",
      disable: "停用",
      save: "保存",
      cancel: "取消",
      confirm: "确定",
      close: "关闭",
      edit: "修改",
      status: '修改状态',
      query: "查询",
      create: "新增",
      delete: "删除",
      upload: "上传",
      import: "导入",
      export: "导出",
      cache: "刷新缓存",
      diagram: "组织架构",
      expand: "展开",
      collapse: "折叠",
      more: "更多",
      logout: "退出登录",
      check: "全选/全不选",
      parent: "父子联动",
      select_column: "选择列",
    },
    menu: {
      cowave: "控维官网",
      dashboard: "首页",
      root: "控维通信",
      monitor: "系统监控",
      tool: "系统工具",
      notice: "通知公告",
      sys: {
        root: "系统管理",
        user: '用户管理',
        role: "角色管理",
        menu: "菜单管理",
        dept: "部门管理",
        post: "岗位管理",
        dict: "字典管理",
        config: "系统参数",
      },
      permits: {
        root: "通用权限",
        readonly: "修改只读"
      }
    },
    confirm: {
        logout: '确定注销并退出系统？'
    },
    msg: {
      success: {
        create: '新增成功',
        edit: '修改成功',
        delete: '删除成功',
        reset: '重置成功',
        grant: '授权成功',
        refresh: '刷新成功',
      },
    },
    theme: {
      title: '主题风格',
      color: '主题颜色',
      enable: '开启',
      fix: '固定',
      show: '显示',
      dynamic: '动态标题',
      profile: "个人中心",
      preference: "偏好设置",
    },
    frame: {
      layout: '系统布局',
      refresh: '刷新页面',
      close: '关闭页面',
      close_all: '全部关闭',
      close_other: '关闭其他',
      close_left: '关闭左侧',
      close_right: '关闭右侧',
    }
  },
  user: {
    label: {
      id: "用户Id",
      name: "用户名称",
      account: "用户账号",
      passwd: "用户密码",
      sex: "性别",
      phone: "手机号码",
      email: "邮箱",
      rank: "职级",
      status: "状态",
      dept: "部门/岗位",
      post: "用户岗位",
      role: "用户角色",
      report: "汇报对象",
      pwd_old: "旧密码",
      pwd_new: "新密码",
      pwd_confirm: "确认密码",
    },
    button: {
      grant: '分配角色',
      passwd: '重置密码',
      add: "添加用户",
      remove: "移除成员",
      select: "选择用户",
    },
    placeholder: {
      account: "请输入用户账号",
      passwd: "请输入用户密码",
      name: "请输入用户名称",
      phone: "请输入手机号码",
      email: "请输入邮箱地址",
      status: "选择用户状态",
      sex: "请选择性别",
      dept: "请选择部门/岗位",
      role: "请选择角色",
      rank: "请选择职级",
      post: "请选择岗位",
      report: "请选择汇报对象",
      pwd_old: "请输入旧密码",
      pwd_new: "请输入新密码",
      pwd_confirm: "请确认密码"
    },
    dialog: {
      diagram: "人员组织架构",
      new: "新增用户",
      edit: "修改用户",
      passwd: "修改密码",
      avatar: "修改头像",
      import: "导入用户",
      import_text1: "将文件拖到此处，或",
      import_text2: "点击上传",
      import_text3: "",
      import_text4: "是否更新已经存在的用户数据",
      import_text5: "仅允许导入xls、xlsx格式文件, ",
      import_text6: "下载模板",
      import_text7: "导入结果",
    },
    rules: {
      account: "用户账号不能为空",
      name: "用户名称不能为空",
      name_len: "用户名称长度必须介于 2 和 20 之间",
      passwd: "用户密码不能为空",
      passwd_len: "密码长度必须介于 6 和 20 之间",
      email: "请输入正确的邮箱地址",
      phone: "请输入正确的手机号码",
      pwd_old: "旧密码不能为空",
      pwd_new: "新密码不能为空",
      pwd_confirm: "确认密码不能为空",
      pwd_compare: "两次输入的密码不一致"
    },
    confirm: {
      status_enable: "确认要启用用户“{arg1}”吗？",
      status_disable: "确认要停用用户“{arg1}”吗？",
      readonly_set: "确认要设置用户“{arg1}”为只读？",
      readonly_cancel: "确认要取消用户“{arg1}”为只读？",
      delete: "确认删除用户“{arg1}”？",
      delete_select: "确认删除所选用户？",
      remove: "确认移除成员“{arg1}”？",
      remove_select: "确认移除所选成员？",
      passwd: "请输入用户“{arg1}”的新密码",
    },
    msg: {
      avatar_failed: "文件格式错误，请上传图片类型，如：JPG，PNG"
    },
    text: {
      profile: "个人信息",
      info: "用户信息",
      basic: "基本信息",
      data: "用户数据",
      list: "成员列表",
      template: "用户数据模板",
      unselect: "未选择用户",
    },
  },
  role: {
    label: {
      id: "角色Id",
      name: "角色名称",
      code: "角色编码",
    },
    button: {
      menus: '菜单权限',
      scope: '数据权限',
      grant: '角色成员',
      remove: "取消授权"
    },
    placeholder: {
      name: "请输入角色名称",
      code: "请输入角色编码",
    },
    dialog: {
      new: "新增角色",
      edit: "修改角色",
    },
    rules: {
      name: "角色名称不能为空",
      code: "角色编码不能为空"
    },
    confirm: {
      delete: "确认删除角色“{arg1}”？",
      delete_select: "确认删除所选角色？",
      readonly_set: "确认要设置角色“{arg1}”为只读？",
      readonly_cancel: "确认要取消角色“{arg1}”为只读？",
      remove_grant: "确认取消用户“{arg1}”的角色？",
      remove_grant_select: "确认取消所选用户的角色？",
    },
    msg: {
      grant: "授权成功",
      remove_grant: "取消授权成功",
    },
    text: {
      info: "角色信息",
    },
  },
  menu: {
    label: {
      root: "根目录",
      name: "菜单名称",
      status: "菜单状态",
      order: "显示排序",
      component: "组件路径",
      permission: "权限标识",
      parent: "上级菜单",
      type: "菜单类型",
      icon: "菜单图标",
      frame: "是否内链",
      visible: "是否显示",
      path: "路由地址",
      cacheable: "是否缓存",
      param: "路由参数",
      visibility: "访问控制"
    },
    button: {
      select: "选择菜单: ",
    },
    placeholder: {
      name: "请输入菜单名称",
      status: "选择菜单状态",
      parent: "选择上级菜单",
      icon: "选择菜单图标",
      path: "请输入路由地址",
      param: "请输入路由参数",
      permission: "请输入权限标识",
      component: "请输入组件路径"
    },
    dialog: {
      new: "新增菜单",
      edit: "修改菜单",
    },
    rules: {
      name: "菜单名称不能为空",
      path: "路由地址不能为空",
      order: "菜单顺序不能为空"
    },
    confirm: {
      delete: "确认要删除菜单“{arg1}”吗？",
      readonly_set: "确认要设置菜单“{arg1}”为只读？",
      readonly_cancel: "确认要取消菜单“{arg1}”为只读？",
    },
    content: {
      name: "名称支持国际化，可以定义成国际化资源中的key",
      status: "停用的路由不会出现在侧边栏，也不能被访问",
      frame: "外链路由需要以`http(s)://`开头",
      visible: "隐藏的路由不会出现在侧边栏，但仍然可以访问",
      cacheable: "缓存的路由会被`keep-alive`缓存，需要匹配组件的`name`和地址保持一致",
      param: "访问路由的默认参数，如：`{\"id\": 1, \"name\": \"cowave\"}`",
      visibility: "公开的菜单可以被任何人访问",
      permission: "如果菜单是受保护的，则访问时需要此权限定义",
      component: "访问的组件路径，如：system/user/index，默认在views目录下"
    },
    text: {
      data: "菜单数据",
    },
  },
  dept: {
    label: {
      name: "部门名称",
      phone: "部门电话",
      leader: "部门负责人",
      addr: "部门地址",
      parent: "上级部门"
    },
    button: {
      members: "部门成员",
      positions: "部门岗位",
    },
    placeholder: {
      name: "请输入部门名称",
      phone: "请输入部门电话",
      addr: "请输入部门地址",
    },
    dialog: {
      diagram: "部门组织架构",
      new: "新增部门",
      edit: "修改部门",
    },
    rules: {
      name: "部门名称不能为空",
      parent: "上级部门不能为空"
    },
    confirm: {
      delete: "确认删除部门“{arg1}”？",
      readonly_set: "确认要设置部门“{arg1}”为只读？",
      readonly_cancel: "确认要取消部门“{arg1}”为只读？",
    },
    text: {
      name: "部门",
      info: "部门信息",
      data: "部门数据",
      leader: "部门负责人",
      default_post: "默认岗位",
      default_user_post: "用户默认岗位",
    },
  },
  post: {
    label: {
      name: "岗位名称",
      type: "岗位类型",
      parent: "上级岗位",
      level: "岗位级别",
      status: "岗位状态",
    },
    button: {
      add: "新增岗位",
      remove: "取消岗位",
      select: "选择岗位"
    },
    placeholder: {
      name: "请输入岗位名称",
      type: "选择岗位类型",
      parent: "选择上级岗位",
    },
    dialog: {
      diagram: "岗位组织架构",
      new: "新增岗位",
      edit: "修改岗位",
    },
    rules: {
      name: "岗位名称不能为空",
      type: "岗位类型不能为空",
      level: "岗位级别不能为空",
    },
    confirm: {
      delete: "确认删除岗位“{arg1}”？",
      delete_select: "确认删除所选岗位？",
      remove: "确认移除岗位“{arg1}”？",
      remove_select: "确认移除所选岗位？",
      readonly_set: "确认要设置岗位“{arg1}”为只读？",
      readonly_cancel: "确认要取消岗位“{arg1}”为只读？",
    },
    text: {
      name: "岗位",
      list: "岗位列表",
      data: "岗位数据",
      unselect: "未选择岗位",
    },
  },
  dict: {
    label: {
      name: "字典",
      english: "英文名称",
      code: "字典编码",
      type: "字典类型",
      group: "字典分组",
      order: "排序",
      css: "字典样式",
      value: "字典值",
      default: "是否默认",
    },
    placeholder: {
      name: "请输入字典名称",
      english: "请输入英文名称",
      code: "请输入字典编码",
      value: "请输入字典值",
    },
    dialog: {
      new: "新增字典",
      edit: "修改字典",
    },
    rules: {
      groupcode: "分组编码不能为空",
      typecode: "类型编码不能为空",
      name: "字典名称不能为空",
      english: "英文名称不能为空",
      code: "字典编码不能为空",
      value: "字典值不能为空",
    },
    confirm: {
      delete: "确认删除字典“{arg1}”？删除操作将会删除所有关联的子字典",
      delete_select: "确认删除所选字典？删除操作将会删除所有关联的子字典！",
      readonly_set: "确认要设置字典“{arg1}”为只读？",
      readonly_cancel: "确认要取消字典“{arg1}”为只读？",
    },
    text: {
      data: "字典数据",
    }
  },
  config: {
    label: {
      name : "参数名称",
      key: "参数标识",
      value: "参数值",
      remark: "备注",
      default: "系统内置",
      parser: "值转换器",
      param: "转换参数"
    },
    placeholder: {
      name: "请输入参数名称",
      key: "请输入参数标识",
      value: "请输入参数值",
      parser: "请输入配置值转换器类名",
      param: "请输入配置值转换参数"
    },
    dialog: {
      new: "新增配置",
      edit: "修改配置"
    },
    rules: {
      name: "参数名称不能为空",
      key: "参数标识不能为空",
      value: "参数值不能为空"
    },
    confirm: {
      delete: "确认删除配置“{arg1}”？",
      delete_select: "确认删除所选配置？",
    },
    text: {
      parser: "ValueParser的实现类",
      param: "转换参数类型为字符串",
      data: "系统参数",
    },
  },
}
