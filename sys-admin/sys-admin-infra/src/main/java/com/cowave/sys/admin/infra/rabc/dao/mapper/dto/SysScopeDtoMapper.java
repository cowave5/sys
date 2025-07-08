package com.cowave.sys.admin.infra.rabc.dao.mapper.dto;

import com.cowave.sys.admin.domain.rabc.SysScope;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shanhuiming
 */
@Mapper
public interface SysScopeDtoMapper {

    /**
     * 获取数据权限
     */
    List<SysScope> listScopeByPermit(@Param("permit") String permit, @Param("list")  List<String> roleCodes);
}
