/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.service.base.impl;

import com.cowave.commons.client.http.response.Response;
import com.cowave.commons.framework.access.Access;
import com.cowave.sys.admin.domain.base.SysOperation;
import com.cowave.sys.admin.domain.base.request.OperationQuery;
import com.cowave.sys.admin.infra.base.dao.mapper.dto.SysLogDtoMapper;
import com.cowave.sys.admin.domain.base.dto.SysLogDto;
import com.cowave.sys.admin.domain.base.vo.LogQuery;
import com.cowave.sys.admin.infra.base.es.EsHelper;
import com.cowave.sys.admin.service.base.SysLogService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class SysLogServiceImpl implements SysLogService {

	private final SysLogDtoMapper sysLogMapper;

	private final EsHelper esHelper;

	@Override
	public Response.Page<SysOperation> list(OperationQuery query) {
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

		if(StringUtils.isNotBlank(query.getOpModule())) {
            boolQuery.filter(QueryBuilders.termsQuery("opModule", query.getOpModule()));
        }
		if(StringUtils.isNotBlank(query.getOpType())) {
            boolQuery.filter(QueryBuilders.termsQuery("opType", query.getOpType()));
        }

		if (query.getBeginTime() != null || query.getEndTime() != null) {
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("opTime");
            if (query.getBeginTime() != null) {
                rangeQueryBuilder.gte(query.getBeginTime());
            }
            if (query.getEndTime() != null) {
                rangeQueryBuilder.lte(query.getEndTime().getTime());
            }
            boolQuery.filter(rangeQueryBuilder);
        }

		if (StringUtils.isNotBlank(query.getOpUser())) {
			BoolQueryBuilder orCondition = QueryBuilders.boolQuery();
			orCondition.should(QueryBuilders.wildcardQuery("access.accessUserName", query.getOpUser()));
			orCondition.should(QueryBuilders.wildcardQuery("access.accessUserAccount", query.getOpUser()));
			boolQuery.filter(orCondition);
		}

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        if(boolQuery.hasClauses()){
            searchSourceBuilder.query(boolQuery);
        }

		searchSourceBuilder.sort("opTime", SortOrder.DESC);
		searchSourceBuilder.size(Access.pageSize());
		searchSourceBuilder.from(Access.pageOffset());
		return esHelper.query(SysOperation.INDEX_NAME, searchSourceBuilder, SysOperation.class);
	}

	@Override
	public void add(SysLogDto sysLog) {
		sysLogMapper.insert(sysLog);
	}

	@Override
	public SysLogDto info(Long id) {
		return sysLogMapper.info(id);
	}

	@Override
	public void delete(Long[] operId) {
		sysLogMapper.delete(operId);
	}

	@Override
	public void clean() {
		sysLogMapper.clean();
	}

	@Override
	public LogQuery userLogQuery(LogQuery logQuery) {
		if(CollectionUtils.isNotEmpty(logQuery.getRoleIds())){
			logQuery.setRoles(sysLogMapper.queryNameByRoleIds(logQuery.getRoleIds()));
		}
		if(CollectionUtils.isNotEmpty(logQuery.getParentIds())){
			logQuery.setParents(sysLogMapper.queryNameByUserIds(logQuery.getParentIds()));
		}

//		if(CollectionUtils.isNotEmpty(logQuery.getDeptPostIds())){
//			List<String> deptPost = new ArrayList<>();
//			for(String deptPostId : logQuery.getDeptPostIds()){
//				String[] arr = deptPostId.split("-");
//				deptPost.add(sysLogMapper.queryNameOfDeptPost(Integer.parseInt(arr[0]), Integer.parseInt(arr[1])));
//			}
//			logQuery.setPosts(deptPost);
//		}
		return logQuery;
	}

	@Override
	public LogQuery deptLogQuery(LogQuery logQuery) {
		if(CollectionUtils.isNotEmpty(logQuery.getParentIds())){
			logQuery.setParents(sysLogMapper.queryNameByDeptIds(logQuery.getParentIds()));
		}
		return logQuery;
	}

	@Override
	public LogQuery postLogQuery(LogQuery logQuery) {
		if(logQuery.getParentId() != null){
			logQuery.setParent(sysLogMapper.queryNameByPostId(logQuery.getParentId()));
		}
		return logQuery;
	}
}
