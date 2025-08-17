package com.cowave.sys.meter.service.torna;

import com.cowave.sys.meter.domain.torna.bean.RequestContext;
import com.cowave.sys.meter.domain.torna.bean.User;
import com.cowave.sys.meter.domain.torna.dto.DocInfoDTO;
import com.cowave.sys.meter.domain.torna.dto.UpdateDocFolderDTO;
import com.cowave.sys.meter.domain.torna.entity.ApiDefinition;
import com.cowave.sys.meter.domain.torna.param.DocPushParam;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface DocService {

    void doPush(DocPushParam param, RequestContext context);

    ApiDefinition createDocFolder(String folderName, long moduleId, User user);

    void updateDocFolderName(UpdateDocFolderDTO updateDocFolderDTO);

    DocInfoDTO getDocDetailView(long docId);

    List<DocInfoDTO> getDocDetailsView(List<Long> docIds);
}
