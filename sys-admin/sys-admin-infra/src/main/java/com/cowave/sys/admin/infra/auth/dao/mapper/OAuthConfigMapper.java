package com.cowave.sys.admin.infra.auth.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cowave.sys.admin.domain.auth.OAuthConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author shanhuiming
 */
@Mapper
public interface OAuthConfigMapper extends BaseMapper<OAuthConfig> {

}
