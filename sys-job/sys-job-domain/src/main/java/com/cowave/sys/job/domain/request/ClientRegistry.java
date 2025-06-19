package com.cowave.sys.job.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Collection;

/**
 * @author xuxueli/shanhuiming
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClientRegistry {

    @NotBlank(message = "clientName can't be empty")
    private String clientName;

    @NotBlank(message = "clientAddress can't be empty")
    private String clientAddress;

    private Collection<String> handlerList;
}
