package io.devpl.sdk.rest;

import io.devpl.sdk.KeyedEnumPool;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * 响应状态编码
 * 1.全部返回200: 不管对错，一律返回200，在返回的JSON中再具体指明错误的原因
 * 2.按照规范：使用规范的HTTP状态码。如果是没有登录，就返回401，如果是没权限就返回403。
 *
 * @since 0.0.1
 */
public final class Status {

    /**
     * 枚举常量池
     */
    private static final KeyedEnumPool<Integer, Status> pool = new KeyedEnumPool<>() {
    };
    /**
     * 预定义的HTTP错误状态，遵循HTTP规范
     */
    public static final Status HTTP_200 = valueOf(200, "OK");
    public static final Status HTTP_201 = valueOf(201, "created");
    public static final Status HTTP_202 = valueOf(202, "accepted");
    public static final Status HTTP_400 = valueOf(400, "bad request");
    public static final Status HTTP_401 = valueOf(401, "未登录");
    public static final Status HTTP_404 = valueOf(404, "not found");
    public static final Status HTTP_405 = valueOf(405, "unsupported media type");
    public static final Status HTTP_301 = valueOf(301, "moved permanently");
    public static final Status HTTP_500 = valueOf(500, "internal server error");
    public static final Status HTTP_503 = valueOf(503, "Service Unavailable");
    /**
     * 业务异常，由具体的业务定义，注意不要覆盖 HTTP 状态码
     */
    public static final Status UNCORRECT_PASSWORD = valueOf(10000, "密码错误");
    public static final Status NO_PASSWORD = valueOf(10001, "请输入密码");
    public static final Status TOKEN_EXPIRED = valueOf(10002, "用户TOKEN已过期");
    public static final Status FORBIDDEN = valueOf(10003, "无权限");
    public static final Status RESOURCE_NOT_EXISTED = valueOf(10004, "资源不存在");
    public static final Status INSERT_OK = valueOf(20000, "新增成功");
    public static final Status UPDATE_OK = valueOf(20001, "修改成功");
    public static final Status DELETE_OK = valueOf(20002, "删除成功");
    /**
     * 响应编码
     */
    private int code;
    /**
     * 描述信息
     */
    private String message;

    private Status(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static Status valueOf(int code, String message, boolean putIfNotExists) {
        Status status = pool.get(code);
        if (status == null) {
            if (putIfNotExists) {
                return pool.put(code, new Status(code, message));
            }
        }
        throw new NoSuchElementException(String.format("状态码[%s]不存在", code));
    }

    /**
     * 默认不存在时会创建并放入常量池
     *
     * @param code    响应状态码
     * @param message 响应信息
     * @return Status
     */
    public static Status valueOf(int code, String message) {
        return valueOf(code, message, true);
    }

    /**
     * 默认不存在时会创建并放入常量池
     *
     * @param code 响应状态码
     * @return Status
     */
    public static Status valueOf(int code) {
        return valueOf(code, "", false);
    }

    /**
     * 更新状态码定义
     *
     * @param code           响应状态码
     * @param message        携带的信息
     * @param putIfNotExists 不存在时是否新增
     */
    public static void update(int code, String message, boolean putIfNotExists) {
        Status status = pool.get(code);
        if (status == null) {
            if (putIfNotExists) {
                pool.put(code, new Status(code, message));
            }
        }
    }

    public static void add(int code, String message, boolean update) {
        Status status = pool.get(code);
        if (status != null) {
            if (update) {
                status.setCode(code);
                status.setMessage(message);
            }
        }
    }

    public static List<Status> listAll() {
        return pool.values();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "[Status] " + code + " " + message;
    }
}
