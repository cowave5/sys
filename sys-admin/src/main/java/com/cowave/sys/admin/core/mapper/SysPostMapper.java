/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.core.mapper;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.sys.admin.core.entity.TreeChildren;
import com.cowave.sys.admin.core.entity.TreeNode;
import com.cowave.sys.model.admin.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cowave.sys.model.admin.SysPost;

/**
 *
 * @author shanhuiming
 *
 */
@Mapper
public interface SysPostMapper {

	/**
	 * 部门岗位选项
	 */
	List<TreeChildren> deptPostOptions();

	/**
	 * 列表
	 */
	Page<SysPost> list(Page<SysPost> page, @Param("post") SysPost sysPost);

	/**
	 * 详情
	 */
	SysPost info(long postId);

	/**
	 * 新增
	 */
	void insert(SysPost sysPost);

	/**
	 * 更新
	 */
	int update(SysPost sysPost);

	/**
	 * 用户计数
	 */
	int countUserByIdArray(Long[] array);

	/**
	 * 只读计数
	 */
	int countReadOnlyByIdArray(Long[] array);

	/**
	 * 删除
	 */
	void delete(Long[] array);

	/**
	 * 删除上级岗位关系
	 */
	void deletePostParentsByIdArray(Long[] array);

	/**
	 * 删除上级岗位关系
	 */
	void deletePostParents(long postId);

	/**
	 * 删除下级岗位关系
	 */
	void deletePostChildsByIdArray(Long[] array);

	/**
	 * 下级岗位id列表
	 */
	List<Long> childIds(long postId);

	/**
	 * 插入上级岗位
	 */
	void insertPostParent(@Param("postId") long postId, @Param("parentId") long parentId);

	/**
	 * 岗位关系
	 */
	List<TreeNode> listTree();

	/**
	 * id列表查询
	 */
	List<SysPost> queryByIdArray(Long[] array);

	/**
	 * 修改只读
	 */
	void changeReadonly(SysPost sysPost);

	/**
	 * 岗位人员，code获取
	 */
	List<SysUser> getUsersByCode(String postCode);
}
