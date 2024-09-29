package com.cowave.sys.admin.domain.base.dto;

import com.cowave.sys.admin.domain.base.SysNotice;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author shanhuiming
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NoticeDto extends SysNotice {

    /**
	 * 创建人名称
	 */
	private String createUserName;
}
