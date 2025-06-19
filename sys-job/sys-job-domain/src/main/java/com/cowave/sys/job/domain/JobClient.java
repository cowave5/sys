package com.cowave.sys.job.domain;

import com.cowave.sys.job.domain.request.ClientRegistry;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author xuxueli/shanhuiming
 */
@NoArgsConstructor
@Data
public class JobClient {

    private Integer id;

    private String clientName;

    private String clientAddress;

    private Date updateTime;

    public JobClient(ClientRegistry clientRegistry){
        this.clientName = clientRegistry.getClientName();
        this.clientAddress = clientRegistry.getClientAddress();
        this.updateTime = new Date();
    }
}
