package com.cowave.sys.meter.open;

import com.cowave.commons.tools.Collections;
import com.cowave.commons.tools.Converts;
import com.cowave.sys.meter.domain.torna.bean.*;
import com.cowave.sys.meter.domain.torna.dto.DocInfoDTO;
import com.cowave.sys.meter.domain.torna.dto.UpdateDocFolderDTO;
import com.cowave.sys.meter.domain.torna.entity.ApiDefinition;
import com.cowave.sys.meter.domain.torna.entity.ApiFolder;
import com.cowave.sys.meter.domain.torna.param.*;
import com.cowave.sys.meter.domain.torna.result.DocCategoryResult;
import com.cowave.sys.meter.domain.torna.result.DocInfoDetailResult;
import com.cowave.sys.meter.domain.torna.result.DocInfoResult;
import com.cowave.sys.meter.domain.torna.result.DocResult;
import com.cowave.sys.meter.infra.torna.dao.ApiDefinitionDao;
import com.cowave.sys.meter.service.torna.DocService;
import com.gitee.easyopen.annotation.Api;
import com.gitee.easyopen.annotation.ApiService;
import com.gitee.easyopen.doc.annotation.ApiDoc;
import com.gitee.easyopen.doc.annotation.ApiDocMethod;
import com.gitee.easyopen.exception.ApiException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author tanghc
 */
@Slf4j
@RequiredArgsConstructor
@ApiService
@ApiDoc(value = "文档API")
public class DocApi {
    private final Gson gson = new Gson();
    private final DocService docService;
    private final ApiDefinitionDao apiDefinitionDao;

    @Api(name = "doc.push")
    @ApiDocMethod(description = "推送文档")
    public void pushDoc(DocPushParam param) {
        RequestContext context = RequestContext.getCurrentContext();
        String ip = context.getIp();
        ApiFolder apiFolder = context.getModule();
        log.info("Api Push ==> {} {} {}", ip, param.getAuthor(), apiFolder.getName());

        // 允许相同目录
        String allowSameFolder = EnvironmentKeys.TORNA_PUSH_ALLOW_SAME_FOLDER.getValue();
        if (Boolean.parseBoolean(allowSameFolder)) {
            this.mergeSameFolder(param);
        } else {
            this.checkSameFolder(param);
        }

        String author = param.getAuthor();
        if (StringUtils.hasText(author)) {
            ApiUser user = (ApiUser) context.getApiUser();
            user.setNickname(author);
        }
        docService.doPush(param, context);
    }

    @Api(name = "doc.category.create")
    @ApiDocMethod(description = "创建分类", order = 4)
    public DocCategoryResult addCategory(CategoryAddParam param) {
        log.info("--------------------");
        long moduleId = RequestContext.getCurrentContext().getModuleId();
        User apiUser = RequestContext.getCurrentContext().getApiUser();
        ApiDefinition category = docService.createDocFolder(param.getName(), moduleId, apiUser);
        return Converts.copyProperties(category, DocCategoryResult.class);
    }

    @Api(name = "doc.category.name.update")
    @ApiDocMethod(description = "修改分类名称", order = 5)
    public void updateCategory(CategoryUpdateParam param) {
        log.info("--------------------");
        String name = param.getName();
        User user = RequestContext.getCurrentContext().getApiUser();
        UpdateDocFolderDTO updateDocFolderDTO = new UpdateDocFolderDTO();
        updateDocFolderDTO.setId(param.getId());
        updateDocFolderDTO.setName(name);
        updateDocFolderDTO.setUser(user);
        updateDocFolderDTO.setParentId(0L);
        docService.updateDocFolderName(updateDocFolderDTO);
    }

    @Api(name = "doc.list")
    @ApiDocMethod(description = "获取应用文档列表", order = 6)
    public DocResult docList(DocIdsParam param) {
        log.info("--------------------");
        long moduleId = RequestContext.getCurrentContext().getModuleId();
        List<Long> docIds = param.getDocIds();
        List<ApiDefinition> apiDefinitions = apiDefinitionDao.listModuleDoc(moduleId, docIds);
        List<DocInfoResult> docInfoResults = Collections.convertToList(apiDefinitions, DocInfoResult.class);
        DocResult docResult = new DocResult();
        docResult.setDocList(docInfoResults);
        return docResult;
    }

    @Api(name = "doc.detail")
    @ApiDocMethod(description = "文档详情", order = 7)
    public DocInfoDetailResult docDetail(DocIdParam param) {
        log.info("--------------------");
        DocInfoDTO docDetailView = docService.getDocDetailView(param.getDocId());
        String json = gson.toJson(docDetailView);
        return gson.fromJson(json, DocInfoDetailResult.class);
    }

    @Api(name = "doc.details")
    @ApiDocMethod(description = "文档详情-批量", order = 8)
    public List<DocInfoDetailResult> docDetails(DocIdsParam param) {
        log.info("--------------------");
        List<Long> docIds = param.getDocIds();
        List<DocInfoDTO> docDetailsView = docService.getDocDetailsView(docIds);
        String json = gson.toJson(docDetailsView);
        Type listType = new TypeToken<List<DocInfoDetailResult>>(){}.getType();
        return gson.fromJson(json, listType);
    }

    @Api(name = "swagger-json.push")
    @ApiDocMethod(description = "推送swagger文档（json）", order = 6, remark = "请求body：{\n" +
            "    \"name\": \"swagger-json.push\",\n" +
            "    \"version\": \"1.0\",\n" +
            "    \"data\": {\n" +
            "        \"author\": \"jim\",\n" +
            "        \"json\": {\n" +
            "            // swagger json内容\n" +
            "        }\n" +
            "    },\n" +
            "    \"access_token\": \"3a6f9fd55e7547a78493e1082ecc1782\"\n" +
            "}")
    public void pushSwaggerDoc(SwaggerJsonParam param) {
        log.info("--------------------");

    }

    private void mergeSameFolder(DocPushParam param) {
        List<DocPushItemParam> apis = param.getApis();
        if (CollectionUtils.isEmpty(apis)) {
            return;
        }
        // key:分类名称， value:分类下的文档
        Map<DocPushItemParam, List<DocPushItemParam>> folderItems = new LinkedHashMap<>(8);
        for (DocPushItemParam api : apis) {
            if (api.getIsFolder() == Booleans.TRUE) {
                List<DocPushItemParam> docItemList = folderItems.computeIfAbsent(api, (k) -> new ArrayList<>());
                docItemList.addAll(api.getItems());
            }
        }
        if (folderItems.isEmpty()) {
            return;
        }
        folderItems.forEach(DocPushItemParam::setItems);
        param.setApis(new ArrayList<>(folderItems.keySet()));
    }

    private void checkSameFolder(DocPushParam param) {
        List<DocPushItemParam> apis = param.getApis();
        if (CollectionUtils.isEmpty(apis)) {
            return;
        }
        // key:分类名称，value:相同文档数量
        Map<String, AtomicInteger> folderCount = new HashMap<>(8);
        for (DocPushItemParam api : apis) {
            if (api.getIsFolder() == Booleans.TRUE) {
                AtomicInteger count = folderCount.computeIfAbsent(api.getName(), (k) -> new AtomicInteger(0));
                count.incrementAndGet();
            }
        }
        folderCount.forEach((name, count) -> {
            if (count.get() > 1) {
                String msg = "文档名称重复【" + name + "】";
//                this.sendErrorMessage(msg);
                throw new ApiException(msg);
            }
        });
    }
}
