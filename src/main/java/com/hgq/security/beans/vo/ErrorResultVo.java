package com.hgq.security.beans.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author houguangqiang
 * @date 2018-11-30
 * @since 1.0
 */
@Data
@NoArgsConstructor
public class ErrorResultVo {

    private long timestamp = System.currentTimeMillis();
    private int status;
    private String error;
    private String message;
    private String path;

    public ErrorResultVo(int status, String error, String message, String path) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }
}
