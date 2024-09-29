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
    @NotNull(message = "{admin.post.id.notnull}")
    private Integer postId;

    /**
     * 岗位名称
     */
    @NotBlank(message = "{admin.post.name.notnull}")
    private String postName;

    /**
     * 只读状态 TODO
     */
    @NotNull(message = "{post.notnull.readOnly}")
    private Integer readOnly;
}
