package com.cowave.sys.meter.open;

import com.alibaba.fastjson.JSON;
import com.cowave.sys.meter.domain.torna.bean.*;
import com.cowave.sys.meter.domain.torna.dto.MessageDTO;
import com.cowave.sys.meter.domain.torna.dto.UpdateDocFolderDTO;
import com.cowave.sys.meter.domain.torna.entity.Module;
import com.cowave.sys.meter.domain.torna.enums.UserSubscribeTypeEnum;
import com.cowave.sys.meter.domain.torna.message.MessageEnum;
import com.cowave.sys.meter.domain.torna.param.*;
import com.cowave.sys.meter.domain.torna.result.DocCategoryResult;
import com.cowave.sys.meter.domain.torna.result.DocInfoDetailResult;
import com.cowave.sys.meter.domain.torna.result.DocResult;
import com.gitee.easyopen.ApiContext;
import com.gitee.easyopen.annotation.Api;
import com.gitee.easyopen.annotation.ApiService;
import com.gitee.easyopen.doc.annotation.ApiDoc;
import com.gitee.easyopen.doc.annotation.ApiDocMethod;
import com.gitee.easyopen.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author tanghc
 */
@Slf4j
@ApiService
@ApiDoc(value = "文档API", order = 1)
public class DocApi {

    @Api(name = "doc.push")
    @ApiDocMethod(description = "推送文档", order = 0, remark = "把第三方文档推送给Torna服务器")
    public void pushDoc(DocPushParam param) {
        RequestContext context = RequestContext.getCurrentContext();
        String token = context.getToken();
        Module module = context.getModule();
        String ip = context.getIp();
        log.info("【PUSH】收到文档推送，模块名称：{}，推送人：{}，ip：{}，token：{}", module.getName(), param.getAuthor(), ip, token);

        // 允许有相同的目录
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

        log.info(JSON.toJSONString(param));
    }

    @Api(name = "doc.category.create")
    @ApiDocMethod(description = "创建分类", order = 4)
    public DocCategoryResult addCategory(CategoryAddParam param) {
        long moduleId = RequestContext.getCurrentContext().getModuleId();
        User apiUser = RequestContext.getCurrentContext().getApiUser();

        return null;
    }

    @Api(name = "doc.category.name.update")
    @ApiDocMethod(description = "修改分类名称", order = 5)
    public void updateCategory(CategoryUpdateParam param) {
        String name = param.getName();
        User user = RequestContext.getCurrentContext().getApiUser();
        UpdateDocFolderDTO updateDocFolderDTO = new UpdateDocFolderDTO();
        updateDocFolderDTO.setId(param.getId());
        updateDocFolderDTO.setName(name);
        updateDocFolderDTO.setUser(user);
        updateDocFolderDTO.setParentId(0L);

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

    }

    @Api(name = "doc.list")
    @ApiDocMethod(description = "获取应用文档列表", order = 6)
    public DocResult docList(DocIdsParam param) {

        return null;
    }

    @Api(name = "doc.detail")
    @ApiDocMethod(description = "文档详情", order = 7)
    public DocInfoDetailResult docDetail(DocIdParam param) {

        return null;
    }

    @Api(name = "doc.details")
    @ApiDocMethod(description = "文档详情-批量", order = 8)
    public List<DocInfoDetailResult> docDetails(DocIdsParam param) {

        return null;
    }

    /**
     * 将相同的目录进行合并
     */
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
                this.sendErrorMessage(msg);
                throw new ApiException(msg);
            }
        });
    }

    private void sendErrorMessage(String msg) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setMessageEnum(MessageEnum.PUSH_ERROR);
        messageDTO.setType(UserSubscribeTypeEnum.PUSH_DOC);
        messageDTO.setLocale(ApiContext.getLocal());
        messageDTO.setSourceId(0L);
        // userMessageService.sendMessageToAdmin(messageDTO, msg); TODO
    }
}
