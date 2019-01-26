package com.hgq.security.support.beans;

/**
 * @author houguangqiang
 * @date 2019-01-18
 * @since 1.0
 */
public class ResultVo {

    private int status;
    private String message;
    private Object data;
    private long timestamp;
    private String path;

    public ResultVo(int status, String message, Object data, long timestamp, String path) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.timestamp = timestamp;
        this.path = path;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getPath() {
        return path;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private long timestamp = System.currentTimeMillis();
        private int status;
        private String message;
        private Object data;
        private String path;

        public Builder status(int status) {
            this.status = status;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder data(Object data) {
            this.data = data;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public ResultVo build() {
            return new ResultVo(status, message, data, timestamp, path);
        }
    }
}
