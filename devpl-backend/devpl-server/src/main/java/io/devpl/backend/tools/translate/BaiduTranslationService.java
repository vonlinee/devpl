package io.devpl.backend.tools.translate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import io.devpl.backend.utils.MD5Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <a href="http://api.fanyi.baidu.com/product/113">...</a>
 */
public class BaiduTranslationService implements TranslationService {

    /**
     * 通用翻译API HTTPS 地址
     */
    private final static String UNIVERSAL_API = "https://fanyi-api.baidu.com/api/trans/vip/translate";
    private final static String CHINESE = "zh";
    private static final HttpClient client = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(3))
        .build();
    private static final HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
        .expectContinue(true)
        .timeout(Duration.ofSeconds(5));
    /**
     * 在个人账号信息界面-开发者信息界面查看
     */
    private final String appId;
    /**
     * 密钥
     */
    private final String secret;
    ObjectMapper objectMapper = new ObjectMapper();

    public BaiduTranslationService(String appId, String secret) {
        this.appId = appId;
        this.secret = secret;
    }

    /**
     * 翻译成中文
     * @param content 需要翻译的文本
     * @return
     */
    @Override
    public List<TranslationVO> toChinese(String content) {
        MultiValueMap<String, String> request = buildParameter(content, CHINESE,
            this.appId, this.secret);

        StringBuilder sb = new StringBuilder();
        sb.append("?");
        for (Map.Entry<String, List<String>> entry : request.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue().get(0)).append("&");
        }
        int length = sb.length();
        String url = sb.substring(0, length - 1);
        url = UNIVERSAL_API + url;
        OkHttpClient okHttpClient = new OkHttpClient();

        Request okhttpRequest = new Request.Builder()
            .url(url)
            .build();

        try {
            Response response = okHttpClient.newCall(okhttpRequest).execute();
            String result = response.body().string();
            System.out.println(result);

            BaiduTranslationResult vo = objectMapper.readValue(result, BaiduTranslationResult.class);
            System.out.println(vo);

            List<TranslationResult> transResult = vo.getTransResult();

            List<TranslationVO> finalResult = new ArrayList<>();

            for (TranslationResult translationResult : transResult) {
                TranslationVO tvo = new TranslationVO(translationResult.getSrc(), translationResult.getDst());
                finalResult.add(tvo);
            }
            return finalResult;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 构建请求参数
     * @param query  需要翻译的文本
     * @param to     需要翻译的语种
     * @param appId  appid <a href="http://api.fanyi.baidu.com/api/trans/product/desktop?req=developer">...</a>
     * @param secret secret <a href="http://api.fanyi.baidu.com/api/trans/product/desktop?req=developer">...</a>
     */
    private MultiValueMap<String, String> buildParameter(String query, String to, String appId, String secret) {
        MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
        String random = String.valueOf(System.currentTimeMillis());

        request.add("from", "auto");
        request.add("to", to);
        request.add("appid", appId);
        request.add("salt", random);
        request.add("sign", buildSign(random, appId, secret, query));
        query = URLEncoder.encode(query, StandardCharsets.UTF_8);
        request.add("q", query);
        return request;
    }

    /**
     * 生成签名
     * 签名生成方法
     * 签名是为了保证调用安全，使用 MD5 算法生成的一段字符串，生成的签名长度为 32 位，签名中的英文字符均为小写格式。
     * <p>
     * 生成方法：
     * Step1. 将请求参数中的 APPID(appid)， 翻译 query(q，注意为UTF-8编码)，随机数(salt)，以及平台分配的密钥(可在管理控制台查看)
     * 按照 appid+q+salt+密钥的顺序拼接得到字符串 1。
     * Step2. 对字符串 1 做 MD5 ，得到 32 位小写的 sign。
     * 注：
     * 1. 待翻译文本（q）需为 UTF-8 编码；
     * 2. 在生成签名拼接 appid+q+salt+密钥 字符串时，q 不需要做 URL encode，在生成签名之后，发送 HTTP 请求之前才需要对要发送的待翻译文本字段 q 做 URL encode；
     * 3.如遇到报 54001 签名错误，请检查您的签名生成方法是否正确，在对 sign 进行拼接和加密时，q 不需要做 URL encode，很多开发者遇到签名报错均是由于拼接 sign 前就做了 URL encode；
     * 4.在生成签名后，发送 HTTP 请求时，如果将 query 拼接在URL上，需要对 query 做 URL encode。
     * @param random 盐值
     * @param appId  appID
     * @param secret 秘钥
     * @param query  查询参数
     * @return 签名
     */
    private String buildSign(String random, String appId, String secret, String query) {
        return MD5Utils.md5(appId + query + random + secret);
    }
}
