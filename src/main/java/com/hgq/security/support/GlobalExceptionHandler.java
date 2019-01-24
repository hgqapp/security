package com.hgq.security.support;

import com.hgq.security.beans.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.Set;

import static org.springframework.http.HttpStatus.*;

/**
 * @author houguangqiang
 * @date 2018-11-30
 * @since 1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleSystemException(Exception ex, WebRequest request) {
        String path = getPath(request);
        if (log.isErrorEnabled()) {
            log.error("未处理的异常，请检查相关代码，并修复它，PATH=\"" + path + "\"", ex);
        }
        ResultVo body = ResultVo.builder()
                .status(INTERNAL_SERVER_ERROR.value())
                .message("系统繁忙")
                .build();
        return handleExceptionInternal(ex, body, new HttpHeaders(), INTERNAL_SERVER_ERROR, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (body == null) {
            String path = getPath(request);
            body = ResultVo.builder()
                    .status(status.value())
                    .message(ex.getMessage())
                    .path(path)
                    .build();
        }
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ResultVo body = null;
        if (ex instanceof MethodArgumentTypeMismatchException) {
            MethodArgumentTypeMismatchException e = (MethodArgumentTypeMismatchException) ex;
            String path = getPath(request);
            String requiredType = "";
            Class<?> type = e.getRequiredType();
            if (type != null) {
                requiredType = type.getSimpleName();
            }
            String msg = String.format("%s参数类型不匹配, 需要的类型:%s, 实际的值:%s", e.getName(), requiredType, e.getValue());
            body = ResultVo.builder()
                    .status(status.value())
                    .message(msg)
                    .path(path)
                    .build();
        }
        return handleExceptionInternal(ex, body, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String path = getPath(request);
        BindingResult bindingResult = ex.getBindingResult();
        FieldError fieldError = bindingResult.getFieldError();
        String msg = "未知的参数错误";
        if (fieldError != null) {
            msg = fieldError.getField() + fieldError.getDefaultMessage();
        }
        ResultVo body = buildBadRequestResult(status, msg, path);
        return handleExceptionInternal(ex, body, headers, status, request);
    }

    /**
     * Spring MVC 参数校验异常处理
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleException(ConstraintViolationException ex, WebRequest request) {
        String path = getPath(request);
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        StringBuilder sb = new StringBuilder();
        violations.forEach(v -> {
            String name = "";
            for (Path.Node node : v.getPropertyPath()) {
                name = node.getName();
            }
            sb.append(name).append(v.getMessage());
        });
        ResultVo body = buildBadRequestResult(BAD_REQUEST, sb.toString(), path);
        return handleExceptionInternal(ex, body, new HttpHeaders(), BAD_REQUEST, request);
    }

    private ResultVo buildBadRequestResult(HttpStatus status, String msg, String path) {
        log.warn("参数校验不通过：{}；PATH=\"{}\"", msg, path);
        return ResultVo.builder()
                .status(status.value())
                .message(msg)
                .path(path)
                .build();
    }

    /**
     * 无权限访问异常处理
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleException(AccessDeniedException ex, WebRequest request) {
        String path = getPath(request);
        ResultVo body = ResultVo.builder()
                .status(FORBIDDEN.value())
                .message(ex.getMessage())
                .path(path)
                .build();
        log.warn("权限校验不通过：{}, PATH=\"{}\"", ex.getMessage(), path);
        return handleExceptionInternal(ex, body, new HttpHeaders(), FORBIDDEN, request);
    }

    private String getPath(RequestAttributes requestAttributes) {
        String path = (String) requestAttributes.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
        if (path == null && requestAttributes instanceof ServletWebRequest) {
            path = ((ServletWebRequest) requestAttributes).getRequest().getRequestURI();
        }
        return path;
    }
}
