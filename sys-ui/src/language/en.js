export default {
  commons: {
    label: {
      index: "Index",
      status: "Status",
      remark: "Remark",
      options: "Options",
      readonly: "Readonly",
      time: {
        begin: "Time Begin",
        end: "Time End",
      },
      date: {
        create: "Create Date",
        begin: "Date Begin",
        end: "Date End",
      },
    },
    button: {
      refresh: "Refresh",
      search: "Search",
      search_hide: "Hide Search",
      search_show: "Show Search",
      reset: "Reset",
      hide: "Hide",
      show: "Show",
      enable: "Enable",
      disable: "Disable",
      save: "Save",
      cancel: "Cancel",
      confirm: "Confirm",
      close: "Close",
      edit: "Edit",
      status: 'Change Status',
      query: "Query",
      create: "New",
      delete: "Delete",
      upload: "Upload",
      import: "Import",
      export: "Export",
      cache: "Refresh Cache",
      diagram: "Diagram",
      expand: "Expand",
      collapse: "Collapse",
      more: "More",
      logout: "Sign out",
      check: "Check All/Cancel All",
      parent: "Parent-Child",
      select_column: "Select Column",
    },
    menu: {
      cowave: "Cowave",
      dashboard: "Dashboard",
      root: "Cowave",
      monitor: "System Monitor",
      tool: "System Tool",
      sys: {
        root: "System Manage",
        user: 'User',
        role: "Role",
        menu: "Menu",
        dept: "Department",
        post: "Post",
        dict: "Dictionary",
        config: "Configuration",
        notice: "Notice",
      },
      permits: {
        root: "Commons Permits",
        readonly: "Change Readonly",
      }
    },
    confirm: {
        logout: 'Sure to sign out?'
    },
    msg: {
      success: {
        create: 'Create success',
        edit: 'Edit success',
        delete: 'Delete success',
        reset: 'Reset success',
        grant: 'Grant success',
        refresh: 'Refresh success',
      },
    },
    theme: {
      title: 'Theme',
      color: 'Color',
      enable: 'Enable',
      fix: 'Fix',
      show: 'Show',
      dynamic: 'Dynamic Title',
      profile: "Profile",
      preference: "Preference",
    },
    frame: {
      layout: 'Layout',
      refresh: 'refresh',
      close: 'close',
      close_all: 'close all tabs',
      close_other: 'close other tabs',
      close_left: 'close tabs to the left',
      close_right: 'close tabs to the right',
    }
  },
  user: {
    label: {
      id: "Id",
      name: "Name",
      account: "Account",
      passwd: "Password",
      sex: "Sex",
      phone: "Phone",
      email: "Email",
      rank: "Rank",
      status: "Status",
      dept: "Dept/Post",
      post: "Post",
      role: "Role",
      report: "Report to",
      pwd_old: "Old Password",
      pwd_new: "New Password",
      pwd_confirm: "Confirm Password",
    },
    button: {
      grant: 'Grant Role',
      passwd: 'Reset Password',
      add: "Add User",
      remove: "Remove User",
      select: "Select User"
    },
    placeholder: {
      account: "Input User Account",
      passwd: "Input User Passwd",
      name: "Input User Name",
      phone: "Input User Phone",
      email: "Input User Email",
      status: "Select User Status",
      sex: "Select Sex",
      dept: "Select Dept/Post",
      role: "Select Role",
      rank: "Select Rank",
      post: "Select Post",
      report: "Select Report User",
      pwd_old: "Input old password",
      pwd_new: "Input new password",
      pwd_confirm: "Confirm new password"
    },
    dialog: {
      diagram: "User Diagram",
      new: "New User",
      edit: "Edit User",
      passwd: "Reset Password",
      avatar: "Change Avatar",
      import: "Import User",
      import_text1: "Drag files here, or",
      import_text2: " click here ",
      import_text3: "to upload",
      import_text4: "Whether to overwrite if user exist",
      import_text5: "Only accept files in xlsx and xls formats, ",
      import_text6: "Template Download",
      import_text7: "Import Result",
    },
    rules: {
      account: "user account can't be empty",
      name: "user name can't be empty",
      name_len: "user name length must be between 2 and 20",
      passwd: "user password can't be empty",
      passwd_len: "password length must be between 6 and 20",
      email: "invalid email",
      phone: "invalid phone number",
      pwd_old: "old password can't be empty",
      pwd_new: "new password can't be empty",
      pwd_confirm: "confirm password can't be empty",
      pwd_compare: "the two entered passwords doesn't match"
    },
    confirm: {
      status_enable: "Sure to enable user \"{arg1}\" ?",
      status_disable: "Sure to disable user \"{arg1}\" ?",
      readonly_set: "Sure to set user \"{arg1}\" as readonly ?",
      readonly_cancel: "Sure to cancel user \"{arg1}\" as readonly ?",
      delete: "Sure to delete user \"{arg1}\" ?",
      delete_select: "Sure to delete the selected user ?",
      remove: "Sure to remove user \"{arg1}\" ?",
      remove_select: "Sure to remove the selected user ?",
      passwd: "Input new password of user \"{arg1}\"",
    },
    msg: {
      avatar_failed: "Invalid file format, please upload the image type, such as: JPG, PNG"
    },
    text: {
      profile: "Profile",
      info: "User Info",
      basic: "Information",
      data: "user",
      list: "Members",
      template: "user_template",
      unselect: "No user selected yet",
    },
  },
  role: {
    label: {
      id: "Id",
      name: "Role Name",
      code: "Role Code",
    },
    button: {
      menus: 'Menus',
      scope: 'Data Scope',
      grant: 'Grant User',
      remove: "Remove Grant",
    },
    placeholder: {
      name: "Input role name",
      code: "Input role code",
    },
    dialog: {
      new: "New Role",
      edit: "Edit Role",
    },
    rules: {
      name: "role name can't be empty",
      code: "role code can't be empty",
    },
    confirm: {
      delete: "Sure to delete role \"{arg1}\" ?",
      delete_select: "Sure to delete the selected role ?",
      readonly_set: "Sure to set role \"{arg1}\" as readonly ?",
      readonly_cancel: "Sure to cancel role \"{arg1}\" as readonly ?",
      remove_grant: "Sure to remove the role of user \"{arg1}\" ?",
      remove_grant_select: "Sure to remove the role of selected user ?",
    },
    msg: {
      grant: "Grant Success",
      remove_grant: "Remove Grant Success",
    },
    text: {
      info: "Role Info",
    },
  },
  menu: {
    label: {
      root: "root",
      name: "Name",
      status: "Status",
      icon: "Icon",
      order: "Order",
      component: "Component",
      permission: "Permission",
      parent: "Parent",
      type: "Type",
      frame: "Frame",
      visible: "Visible",
      path: "Path",
      cacheable: "Cacheable",
      param: "Param",
      visibility: "Visibility"
    },
    button: {
      select: "Select: ",
    },
    placeholder: {
      name: "Input Menu Name",
      status: "Select Menu Status",
      parent: "Select Parent Menu",
      icon: "Select Menu Icon",
      path: "Input Route Path",
      param: "Input Route Param",
      permission: "Input Route Permission",
      component: "Input Component Path"
    },
    rules: {
      name: "menu name can't be empty",
      path: "route path can't be empty",
      order: "menu order can't be empty"
    },
    dialog: {
      new: "New Menu",
      edit: "Edit Menu"
    },
    confirm: {
      delete: "Sure to delete menu \"{arg1}\" ?",
      readonly_set: "Sure to set menu \"{arg1}\" as readonly ?",
      readonly_cancel: "Sure to cancel menu \"{arg1}\" as readonly ?",
    },
    content: {
      name: "The name supports internationalization and can be defined as the key in internationalized resource",
      status: "The disabled route will not appear in the sidebar and can't be accessed",
      frame: "The external route needs to start with `http(s)://`",
      visible: "The hidden route will not appear in the sidebar but still can be accessed",
      cacheable: "The cached route will be cached by `keep-alive`, but it's `name` and address of component need to be consistent",
      param: "The default parameters when you access route，such as：`{\"id\": 1, \"name\": \"cowave\"}`",
      visibility: "The public menu can be accessed by anyone",
      permission: "The permission is required when access if the menu is protected",
      component: "The path of the route component, such as: system/user/index, which is in the views directory by default"
    },
    text: {
      data: "menu",
    },
  },
  dept: {
    label: {
      name: "Name",
      phone: "Phone",
      leader: "Leader",
      addr: "Address",
      parent: "Parent"
    },
    button: {
      members: "Members",
      positions: "Positions",
    },
    placeholder: {
      name: "Input department name",
      phone: "Input phone number",
      addr: "Input department address",
    },
    dialog: {
      diagram: "Department Diagram",
      new: "New Department",
      edit: "Edit Department",
    },
    rules: {
      name: "department name can't be empty",
      parent: "parent department can't be empty"
    },
    confirm: {
      delete: "Sure to delete department \"{arg1}\" ?",
      readonly_set: "Sure to set dept \"{arg1}\" as readonly ?",
      readonly_cancel: "Sure to cancel dept \"{arg1}\" as readonly ?",
    },
    text: {
      name: "Department",
      info: "Department Info",
      data: "department",
      leader: "Department Leader",
      default_post: "Default Post",
      default_user_post: "User Default Post",
    },
  },
  post: {
    label: {
      name: "Name",
      type: "Type",
      parent: "Parent Post",
      level: "Level",
      status: "Status",
    },
    button: {
      add: "Add Post",
      remove: "Remove Post",
      select: "Select Post"
    },
    placeholder: {
      name: "Input post name",
      type: "Select post type",
      parent: "Select parent post",
    },
    dialog: {
      diagram: "Post Diagram",
      new: "New Post",
      edit: "Edit Post",
    },
    rules: {
      name: "post name can't be empty",
      type: "post type can't be empty",
      level: "post level can't be empty",
    },
    confirm: {
      delete: "Sure to delete post \"{arg1}\" ?",
      delete_select: "Sure to delete the selected post ?",
      remove: "Sure to remove post \"{arg1}\" ?",
      remove_select: "Sure to remove the selected post ?",
      readonly_set: "Sure to set post \"{arg1}\" as readonly ?",
      readonly_cancel: "Sure to cancel post \"{arg1}\" as readonly ?",
    },
    text: {
      name: "Post",
      list: "Posts",
      data: "post",
      unselect: "No post selected yet",
    },
  },
  dict: {
    label: {
      name: "Name",
      english: "English",
      code: "Code",
      type: "Type",
      group: "Group",
      order: "Order",
      css: "Css",
      value: "Value",
      default: "Default",
    },
    placeholder: {
      name: "Input dictionary name",
      english: "Input english name",
      code: "Input dictionary code",
      value: "Input dictionary value",
    },
    dialog: {
      new: "New Dictionary",
      edit: "Edit Dictionary",
    },
    rules: {
      groupcode: "group code can't be empty",
      typecode: "type code can't be empty",
      name: "dictionary name can't be empty",
      english: "english name can't be empty",
      code: "dictionary code can't be empty",
      value: "dictionary value can't be empty",
    },
    confirm: {
      delete: "Sure to delete dictionary \"{arg1}\" ？This will delete all associated sub dictionaries",
      delete_select: "Sure to delete the selected dictionary？This will delete all associated sub dictionaries",
      readonly_set: "Sure to set dictionary \"{arg1}\" as readonly ?",
      readonly_cancel: "Sure to cancel dictionary \"{arg1}\" as readonly ?",
    },
    text: {
      data: "dictionary",
    }
  },
  config: {
    label: {
      name : "Name",
      key: "Key",
      value: "Value",
      remark: "Remark",
      default: "System",
      parser: "Parser",
      param: "Param"
    },
    placeholder: {
      name : "Input config name",
      key: "Input config key",
      value: "Input config value",
      parser: "Input class name of value parser",
      param: "Input param of value parser"
    },
    dialog: {
      new: "New Config",
      edit: "Edit Config"
    },
    rules: {
      name: "config name can't be empty",
      key: "config key can't be empty",
      value: "config value can't be empty"
    },
    confirm: {
      delete: "Sure to delete config \"{arg1}\" ?",
      delete_select: "Sure to delete the selected config ?",
    },
    text: {
      parser: "Implementation class of ValueParser",
      param: "parameter type is string",
      data: "system_param",
    },
  },
}
