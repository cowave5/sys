/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.core.service.impl;

import cn.hutool.core.util.IdUtil;
import com.cowave.commons.framework.helper.minio.MinioHelper;
import com.cowave.commons.framework.helper.minio.MinioProperties;
import com.cowave.commons.response.exception.Asserts;
import com.cowave.commons.tools.DateUtils;
import com.cowave.sys.admin.core.service.SysAttachService;
import com.cowave.sys.admin.core.dao.mapper.dto.SysAttachDtoMapper;
import com.cowave.sys.admin.core.entity.dto.SysAttachDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class SysAttachServiceImpl implements SysAttachService {

    private final MinioProperties minioProperties;

    private final MinioHelper minioHelper;

    private final SysAttachDtoMapper sysAttachMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysAttachDto upload(MultipartFile file, SysAttachDto sysAttach, boolean isPublic) throws Exception {
        String fileName = file.getOriginalFilename();
        sysAttach.setAttachName(fileName);
        sysAttach.setAttachSize(file.getSize());
        sysAttach.setCreateTime(new Date());
        sysAttach.setIsPublic(isPublic ? 1 : 0);
        String filePath = sysAttach.getAttachType()
                + File.pathSeparator + DateUtils.format("yyyy-MM") + File.pathSeparator + IdUtil.randomUUID() + "." + fileName;
        sysAttach.setAttachPath(filePath);
        sysAttachMapper.insert(sysAttach);

        minioHelper.upload(file, sysAttach.getAttachGroup(), filePath, isPublic);
        return sysAttach;
    }

    @Override
    public String preview(Long attachId) throws Exception {
        SysAttachDto sysAttach = sysAttachMapper.info(attachId);
        Asserts.notNull(sysAttach, "{attach.notexist}");
        return preview(sysAttach);
    }

    @Override
    public String preview(SysAttachDto sysAttach) throws Exception {
        if(sysAttach.getIsPublic() == 1){
            return minioProperties.getEndpoint() + "/" + sysAttach.getAttachGroup() + "/" + sysAttach.getAttachPath();
        }else{
            return minioHelper.preview(sysAttach.getAttachGroup(), sysAttach.getAttachPath());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Long attachId) throws Exception {
        if(attachId == null){
            return;
        }
        delete(sysAttachMapper.info(attachId));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(SysAttachDto sysAttach) throws Exception {
        if(sysAttach == null){
            return;
        }
        sysAttachMapper.delete(sysAttach.getAttachId());
        minioHelper.delete(sysAttach.getAttachGroup(), sysAttach.getAttachPath());
    }

    @Override
    public void download(HttpServletResponse response, Long attachId) throws Exception {
        SysAttachDto sysAttach = sysAttachMapper.info(attachId);
        Asserts.notNull(sysAttach, "{attach.notexist}");
        minioHelper.download(response, sysAttach.getAttachGroup(), sysAttach.getAttachPath(), sysAttach.getAttachName());
    }

    @Override
    public List<SysAttachDto> list(Long masterId, String attachGroup, String attachType) {
        return sysAttachMapper.list(masterId, attachGroup, attachType);
    }

    @Override
    public void masterUpdate(List<Long> attachIds, Long masterId) {
        sysAttachMapper.masterUpdate(attachIds, masterId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void masterDelete(Long masterId, String attachGroup, String attachType) throws Exception {
        sysAttachMapper.masterDelete(masterId, attachGroup, attachType);
        List<SysAttachDto> list = sysAttachMapper.list(masterId, attachGroup, attachType);
        for(SysAttachDto sysAttach : list) {
            minioHelper.delete(attachGroup, sysAttach.getAttachPath());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void masterReserve(Long masterId, String attachGroup, String attachType, int reserve) throws Exception {
        List<SysAttachDto> list = list(masterId, attachGroup, attachType);
        for(int i = reserve; i < list.size(); i++){
            SysAttachDto attach = list.get(i);
            sysAttachMapper.delete(attach.getAttachId());
            minioHelper.delete(attachGroup, attach.getAttachPath());
        }
    }

    @Override
    public void updateMasterById(Long masterId, Long attachId) {
        sysAttachMapper.updateMasterById(masterId, attachId);
    }

    @Override
    public SysAttachDto latestOfMaster(Long masterId, String attachGroup) throws Exception {
        SysAttachDto attach = sysAttachMapper.latestOfMaster(masterId, attachGroup);
        if(attach != null){
            attach.setViewUrl(preview(attach));
        }
        return attach;
    }
}
