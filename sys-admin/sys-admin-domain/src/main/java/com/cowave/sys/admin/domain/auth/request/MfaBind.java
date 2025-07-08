package com.cowave.sys.admin.domain.auth.request;

import lombok.Data;

/**
 * @author shanhuiming
 */
@Data
public class MfaBind {

    private String mfaKey;

    private String mfaCode;
}
