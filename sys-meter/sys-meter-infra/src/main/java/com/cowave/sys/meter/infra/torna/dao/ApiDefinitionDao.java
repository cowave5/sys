package com.cowave.sys.meter.infra.torna.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.meter.domain.torna.bean.Booleans;
import com.cowave.sys.meter.domain.torna.bean.EnvironmentKeys;
import com.cowave.sys.meter.domain.torna.entity.ApiDefinition;
import com.cowave.sys.meter.domain.torna.enums.DocSortType;
import com.cowave.sys.meter.domain.torna.enums.OperationMode;
import com.cowave.sys.meter.infra.torna.dao.mapper.ApiDefinitionMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;

@Repository
public class ApiDefinitionDao extends ServiceImpl<ApiDefinitionMapper, ApiDefinition> {

    /**
     * 获取Module文档
     */
    public List<ApiDefinition> listByModuleId(Long moduleId) {
        return lambdaQuery().eq(ApiDefinition::getFolderId, moduleId).list();
    }

    public ApiDefinition getByDocId(Long docId) {
        return lambdaQuery()
                .eq(ApiDefinition::getId, docId)
                .eq(ApiDefinition::getIsShow, Booleans.TRUE)
                .one();
    }

    public List<ApiDefinition> listByDocIds(List<Long> docIds) {
        return lambdaQuery()
                .in(ApiDefinition::getId, docIds)
                .eq(ApiDefinition::getIsShow, Booleans.TRUE)
                .list();
    }

    public List<ApiDefinition> listModuleDoc(long moduleId, List<Long> docIds) {
        List<ApiDefinition> list = lambdaQuery()
                .eq(ApiDefinition::getFolderId, moduleId)
                .in(!CollectionUtils.isEmpty(docIds), ApiDefinition::getId, docIds)
                .list();
        sortDocInfo(list);
        return list;
    }

    public static void sortDocInfo(List<ApiDefinition> apiDefinitionList) {
        if (CollectionUtils.isEmpty(apiDefinitionList)) {
            return;
        }
        String value = EnvironmentKeys.TORNA_DOC_SORT_TYPE.getValue();
        Comparator<ApiDefinition> comparator;
        switch (DocSortType.of(value)) {
            case BY_URL:
                comparator = Comparator.comparing(ApiDefinition::getUrl).thenComparing(ApiDefinition::getOrderIndex);
                break;
            case BY_NAME:
                comparator = Comparator.comparing(ApiDefinition::getName).thenComparing(ApiDefinition::getOrderIndex);
                break;
            default: {
                comparator = Comparator.comparing(ApiDefinition::getOrderIndex);
            }
        }
        apiDefinitionList.sort(comparator);
    }

    public ApiDefinition getByModuleIdAndParentIdAndName(long moduleId, long parentId, String name) {
        return lambdaQuery()
                .eq(ApiDefinition::getFolderId, moduleId)
                .eq(ApiDefinition::getParentId, parentId)
                .eq(ApiDefinition::getName, name)
                .one();
    }

    public List<Long> listDocIdByModuleId(Long moduleId) {
        return lambdaQuery()
                .eq(ApiDefinition::getFolderId, moduleId)
                .select(ApiDefinition::getFolderId)
                .list().stream().map(ApiDefinition::getId).toList();
    }

    public void deleteOpenAPIModuleDocs(long moduleId) {
        lambdaUpdate()
                .eq(ApiDefinition::getFolderId, moduleId)
                .eq(ApiDefinition::getCreateMode, OperationMode.OPEN.getType())
                .eq(ApiDefinition::getIsLocked, Booleans.FALSE)
                .and(wrapper -> wrapper
                        .isNull(ApiDefinition::getVersion)
                        .or()
                        .eq(ApiDefinition::getVersion, "")
                ).remove();
    }
}
