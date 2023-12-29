package io.devpl.sdk.rest;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * MVC规范
 * 1.C：控制层Controller,接口层，负责对请求的url分发到不同的网址，处理请求的入口。
 * 2.M：规范数据数据成Bean，并负责调用数据库
 * 3.V：只负责从数据库获取数据，并显示。
 * Controller负责调度View层和Model层，主要接收请求，然后转发到Model处理，处
 * <p>
 * 三层架构：实际上将Controller-Model层细分成三层
 *
 * @since 0.0.1
 */
public abstract class RestfulResultTemplate extends ResultTemplate {

    /**
     * HTTP状态码，不能为空，必须和HTTP header中的状态码一致
     */
    protected int code;

    /**
     * 对错误信息的简单解释
     */
    protected String message;

    /**
     * 用户提示信息，如果没有一定不要设置此值，避免造成混乱
     */
    protected String toast;

    /**
     * 对错误信息的详细解释的网址。比如可以包含，如果获取相应的权限等
     */
    protected String moreInfo;

    protected String stacktrace;

    /**
     * 访问的接口地址
     */
    private String path;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getToast() {
        return toast;
    }

    public String getPath() {
        return path;
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    public String getStackTrace() {
        return stacktrace;
    }

    /**
     * RESTful返回JSON字符串
     *
     * @return 标准JSON字符串
     */
    public String toJSONString() {
        StringBuilder sb = new StringBuilder("{");
        sb.append("\"timestamp\":").append(this.getTimestamp()).append(",");
        sb.append("\"code\": ").append(this.getCode()).append(",");
        sb.append("\"path\":").append(this.path).append("\",");
        sb.append("\"message\":\"").append(this.getMessage()).append("\",");
        sb.append("\"toast\":\"").append(this.getToast()).append("\",");
        sb.append("\"more_info\":\"").append(this.moreInfo == null ? "" : this.moreInfo).append("\",");
        this.toJSONString(sb);
        sb.append("\"stacktrace\":\"").append(this.throwable == null ? "" : this.throwable.toString()).append("\"");
        sb.append("}");
        return sb.toString();
    }

    /**
     * 补充额外的字段信息
     *
     * @param result 返回JSON字符串片段，不包含开头的{和}，如果包含双引号或者单引号需要进行转义
     */
    protected abstract void toJSONString(final StringBuilder result);

    /**
     * 获取 Throwable 的异常调用栈
     *
     * @param throwable
     * @return
     */
    public String getStackTrace(Throwable throwable) {
        if (throwable == null) return "";
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        pw.println(throwable);

        StackTraceElement[] trace = throwable.getStackTrace();
        for (StackTraceElement traceElement : trace) {
            if (traceElement.getClassName().startsWith("io.devpl")) {
                pw.println(traceElement);
            }
        }
        pw.flush();
        sw.flush();
        return sw.toString();
    }
}
