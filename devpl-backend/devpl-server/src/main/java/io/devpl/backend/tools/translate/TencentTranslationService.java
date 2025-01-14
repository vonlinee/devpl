package io.devpl.backend.tools.translate;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.tmt.v20180321.TmtClient;
import com.tencentcloudapi.tmt.v20180321.models.TextTranslateRequest;
import com.tencentcloudapi.tmt.v20180321.models.TextTranslateResponse;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TencentTranslationService implements TranslationService {

    private final String secretId;

    private final String secretKey;
    private final Pattern p = Pattern.compile("\\[([^]]*)]");

    public TencentTranslationService(String secretId, String secretKey) {
        this.secretId = secretId;
        this.secretKey = secretKey;
    }

    @Override
    public List<TranslationVO> toChinese(String content) {
        TextTranslateResponse resp = requestApi(content);
        List<TranslationVO> translationVOList = new ArrayList<>();
        if (null == resp) {
            return translationVOList;
        }
        String targetText = resp.getTargetText();
        if (StringUtils.isBlank(targetText)) {
            return new ArrayList<>();
        }

        Matcher contentMatcher = p.matcher(content);
        Matcher targetTextMatcher = p.matcher(targetText);
        while (contentMatcher.find() && targetTextMatcher.find()) {
            translationVOList.add(new TranslationVO(contentMatcher.group()
                .substring(1, contentMatcher.group().length() - 1),
                targetTextMatcher.group().substring(1, targetTextMatcher.group().length() - 1)));
        }
        return translationVOList;
    }

    private TextTranslateResponse requestApi(String content) {
        Credential cred = new Credential(secretId, secretKey);

        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("tmt.tencentcloudapi.com");

        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);

        TmtClient client = new TmtClient(cred, "ap-guangzhou", clientProfile);

        TextTranslateRequest req = new TextTranslateRequest();
        req.setSourceText(content);
        req.setSource("auto");
        req.setTarget("zh");
        req.setProjectId(0L);

        TextTranslateResponse resp = null;
        try {
            resp = client.TextTranslate(req);
        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
        }
        return resp;
    }

}
