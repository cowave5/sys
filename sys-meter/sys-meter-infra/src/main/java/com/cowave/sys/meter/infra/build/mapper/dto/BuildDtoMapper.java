package com.cowave.sys.meter.infra.build.mapper.dto;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author shanhuiming
 */
@Mapper
public interface BuildDtoMapper {

    /**
     * 目录中下一个序列
     */
    Integer nextIndexInFolder(Integer parentId);

    /**
     * 下一个构建序列
     */
    Long nextIndexOfBuild(Integer buildId);

    /**
     * 下级目录id
     */
    List<Integer> folderChildrenIds(Integer folderId);
}
