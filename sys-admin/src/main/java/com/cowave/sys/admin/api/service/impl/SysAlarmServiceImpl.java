/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.api.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.tools.Asserts;
import com.cowave.sys.admin.api.service.SysAlarmService;
import com.cowave.sys.model.admin.SysAlarm;
import com.cowave.sys.model.admin.SysAlarmType;
import org.springframework.stereotype.Service;

import com.cowave.sys.admin.core.entity.AlarmHandles;
import com.cowave.sys.admin.core.mapper.SysAlarmMapper;

import lombok.RequiredArgsConstructor;

/**
 *
 * @author shanhuiming
 *
 */
@RequiredArgsConstructor
@Service
public class SysAlarmServiceImpl implements SysAlarmService {

	private final SysAlarmMapper sysAlarmMapper;

	@Override
	public void add(SysAlarm sysAlarm) {
        if(sysAlarmMapper.alarmIncrease(sysAlarm) == 0) {
            sysAlarmMapper.insert(sysAlarm);
        }
	}

	@Override
	public Page<SysAlarmType> typeList(SysAlarmType sysAlarmType) {
		return sysAlarmMapper.typeList(Access.page(), sysAlarmType);
	}

	@Override
	public void typeAdd(SysAlarmType sysAlarmType) {
	    sysAlarmMapper.insertType(sysAlarmType);
	}

	@Override
	public void typeEdit(SysAlarmType sysAlarmType) {
	    sysAlarmMapper.updateType(sysAlarmType);
	}

	@Override
	public void typeDelete(Long id) {
	    Asserts.isNull(sysAlarmMapper.selectOne(id), "{forbid.delete.alarm.type}");
	    sysAlarmMapper.deleteType(id);
	}

	@Override
	public Page<SysAlarm> list(SysAlarm sysAlarm) {
		return sysAlarmMapper.list(Access.page(), sysAlarm);
	}

	@Override
	public SysAlarm info(Long id) {
		return sysAlarmMapper.info(id);
	}

	@Override
	public void delete(Long id) {
	    sysAlarmMapper.delete(id);
	}

    @Override
    public void handle(AlarmHandles alarmHandles) {
        sysAlarmMapper.updateHandle(alarmHandles);
    }
}
