package com.hgq.security.support;

import com.hgq.security.beans.vo.ErrorResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

/**
 * @author houguangqiang
 * @date 2018-11-30
 * @since 1.0
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ErrorResultVo handleException(Exception e, WebRequest webRequest) {
        log.error("Unknown exception, you should check your code", e);
        return new ErrorResultVo();
    }

    private String getPath(RequestAttributes requestAttributes) {
        return getAttribute(requestAttributes, "javax.servlet.error.request_uri");
    }

    private String getAttribute(RequestAttributes requestAttributes, String name) {
        return (String) requestAttributes.getAttribute(name, RequestAttributes.SCOPE_REQUEST);
    }
}
