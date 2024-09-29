package com.cowave.sys.admin.domain.rabc.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author shanhuiming
 */
@Data
public class PostReadUpdate {

    /**
     * 岗位id
     */
    @NotNull(message = "{post.notnull.id}")
    private Integer postId;

    /**
     * 岗位名称
     */
    @NotBlank(message = "{post.notnull.name}")
    private String postName;

    /**
     * 只读状态 TODO
     */
    @NotNull(message = "{post.notnull.readOnly}")
    private Integer readOnly;
}
