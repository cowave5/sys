package com.cowave.sys.admin.domain.auth.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shanhuiming
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MfaVo {

    private String mfaKey;

    private String url;
}
