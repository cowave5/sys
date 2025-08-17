package com.cowave.sys.meter.infra.torna.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.meter.domain.torna.entity.ApiDefinitionParam;
import com.cowave.sys.meter.domain.torna.entity.ConstantInfo;
import com.cowave.sys.meter.domain.torna.entity.ModuleConfig;
import com.cowave.sys.meter.domain.torna.util.HtmlTableBuilder;
import com.cowave.sys.meter.infra.torna.dao.mapper.ConstantInfoMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ConstantInfoDao extends ServiceImpl<ConstantInfoMapper, ConstantInfo> {

    public void setCommonErrorCodeList(List<ApiDefinitionParam> docParamList, long moduleId) {
        if (CollectionUtils.isEmpty(docParamList)) {
            return;
        }
        List<ModuleConfig> moduleConfigs = docParamList.stream().map(
                docParam -> {
                    ModuleConfig config = new ModuleConfig();
                    config.setModuleId(moduleId);
                    config.setConfigKey(docParam.getName());
                    config.setConfigValue(docParam.getExample());
                    config.setDescription(docParam.getDescription());
                    return config;
                })
                .collect(Collectors.toList());
        ConstantInfo constantInfo = this.buildConstantInfo(moduleId, moduleConfigs);
        saveModuleConstantInfo(moduleId, constantInfo.getContent());
    }

    public ConstantInfo buildConstantInfo(long moduleId, List<ModuleConfig> moduleConfigs) {
        ConstantInfo errorCodeInfo = new ConstantInfo();
        errorCodeInfo.setModuleId(moduleId);
        String content = buildModuleHtmlTable(moduleConfigs);
        errorCodeInfo.setContent(content);
        return errorCodeInfo;
    }

    private String buildModuleHtmlTable(List<ModuleConfig> moduleConfigs) {
        HtmlTableBuilder htmlTableBuilder = new HtmlTableBuilder();
        htmlTableBuilder.heads("错误码", "错误描述", "解决方案");
        for (ModuleConfig moduleConfig : moduleConfigs) {
            htmlTableBuilder.addRow(
                    Arrays.asList(moduleConfig.getConfigKey(),
                            moduleConfig.getDescription(),
                            moduleConfig.getConfigValue()
                    )
            );
        }
        return htmlTableBuilder.toString();
    }

    public void saveModuleConstantInfo(long moduleId, String content) {
        ConstantInfo errorCodeInfo = lambdaQuery().eq(ConstantInfo::getModuleId, moduleId).one();
        if (errorCodeInfo == null) {
            errorCodeInfo = new ConstantInfo();
            errorCodeInfo.setModuleId(moduleId);
            errorCodeInfo.setContent(content);
            this.save(errorCodeInfo);
        } else {
            errorCodeInfo.setContent(content);
            this.updateById(errorCodeInfo);
        }
    }
}
