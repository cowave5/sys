package com.cowave.sys.admin.domain.base.request;

import com.cowave.commons.framework.access.security.AccessInfoSetter;
import com.cowave.sys.admin.domain.base.SysNotice;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author shanhuiming
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NoticeCreate extends SysNotice implements AccessInfoSetter {

    /**
	 * 图片附件
	 */
	private List<AttachView> attaches;
}
