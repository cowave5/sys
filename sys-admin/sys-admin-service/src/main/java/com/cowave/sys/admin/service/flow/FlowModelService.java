/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.service.flow;

import com.cowave.commons.client.http.asserts.HttpAsserts;
import com.cowave.commons.client.http.response.Response;
import com.cowave.commons.framework.access.Access;
import com.cowave.sys.admin.domain.flow.FlowModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.poi.util.IOUtils;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.editor.language.json.converter.BpmnJsonConverter;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ModelQuery;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import static com.cowave.commons.client.http.constants.HttpCode.BAD_REQUEST;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class FlowModelService {

    private final ObjectMapper objectMapper;

    private final RepositoryService repositoryService;

    /**
     * 列表
     */
    public Response.Page<Model> list(FlowModel flowModel) {
        ModelQuery modelQuery = repositoryService.createModelQuery();
        flowModel.fillModelQuery(modelQuery);
        List<Model> list = modelQuery.orderByCreateTime().desc().listPage(Access.pageOffset(), Access.pageSize());
        return new Response.Page<>(list, modelQuery.count());
    }

    /**
     * 新增
     */
    public void add(FlowModel flowModel) throws JsonProcessingException {
        ModelQuery modelQuery = repositoryService.createModelQuery();
        long count = modelQuery.modelKey(flowModel.getKey()).count();
        HttpAsserts.isTrue(count == 0, BAD_REQUEST, "{flow.key.duplicate}");

        Model model = repositoryService.newModel();
        flowModel.fillModel(model);
        // 保存模型到act_re_model表
        repositoryService.saveModel(model);
        // 保存模型文件到act_ge_bytearray表
        repositoryService.addModelEditorSource(model.getId(), flowModel.buildModelContent(model.getId()));
    }

    /**
     * 发布
     */
    public void publish(String modelId) throws IOException {
        Model model = repositoryService.getModel(modelId);
        byte[] modelData = repositoryService.getModelEditorSource(modelId);
        JsonNode jsonNode = objectMapper.readTree(modelData);
        BpmnModel bpmnModel = (new BpmnJsonConverter()).convertToBpmnModel(jsonNode);
        Deployment deploy = repositoryService.createDeployment().category(model.getCategory())
                .name(model.getName()).key(model.getKey())
                .addBpmnModel(model.getKey() + ".bpmn20.xml", bpmnModel).deploy();
        model.setDeploymentId(deploy.getId());
        repositoryService.saveModel(model);
    }

    /**
     * 删除
     */
    public void delete(String[] modelIds) {
        for(String modelId : modelIds){
            repositoryService.deleteModel(modelId);
        }
    }

    /**
     * 导出
     */
    public void export(String modelId, HttpServletResponse response) throws IOException {
        byte[] modelData = repositoryService.getModelEditorSource(modelId);
        JsonNode jsonNode = objectMapper.readTree(modelData);
        BpmnModel bpmnModel = (new BpmnJsonConverter()).convertToBpmnModel(jsonNode);
        byte[] xmlBytes = (new BpmnXMLConverter()).convertToXML(bpmnModel, "UTF-8");
        try(ByteArrayInputStream in = new ByteArrayInputStream(xmlBytes);
            OutputStream out = response.getOutputStream()){
            IOUtils.copy(in, out);
        }
        String filename = bpmnModel.getMainProcess().getId() + ".bpmn20.xml";
        response.setHeader("Content-Disposition","attachment;filename=" + filename);
        response.setHeader("content-Type", "application/xml");
        response.flushBuffer();
    }
}
