package cn.cnic.autocheck.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * Created by liuang on 2017/5/27.
 */
public class HttpUtil {

    public static boolean get(String url) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();
        httpGet.setConfig(requestConfig);
        httpGet.setHeader("User-Agent", "CNICPunchCard/1.0 (iPhone; iOS 10.3.1; Scale/2.00)");
        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        if (entity == null)
            return false;
        String result = IOUtils.toString(entity.getContent(), "utf-8");
        if (StringUtils.isEmpty(result))
            return false;
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (jsonObject.getString("success").equals("true"))
            return true;
        return false;
    }
}
