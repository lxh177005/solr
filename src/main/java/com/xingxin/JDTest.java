package com.xingxin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JDTest {

    public static void main(String[] args) throws Exception {
        String accessToken = getAccessToken();
        System.out.println("accessToken = " + accessToken);
    }
    // 获取access token
    private static String getAccessToken()  {
        String clientId = "zlpzRu9PWWHKoQmWayqk";
        String clientSecret = "oVNC3LZBB1MvlpnJxbCl";
        String tokenPin = "工行北分测试管理员";
        String tokenPinPwd = "JD123456";

        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        String grantType = "access_token";
        nameValuePairList.add(new BasicNameValuePair("grant_type", grantType));
        nameValuePairList.add(new BasicNameValuePair("client_id", clientId));
        nameValuePairList.add(new BasicNameValuePair("client_secret", clientSecret));
        String timestamp = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
        System.out.println("timestamp:"+timestamp);
        nameValuePairList.add(new BasicNameValuePair("timestamp", timestamp));
        nameValuePairList.add(new BasicNameValuePair("username", tokenPin));
        String pwdMd5 = StringUtils.upperCase(DigestUtils.md5Hex(tokenPinPwd));
        nameValuePairList.add(new BasicNameValuePair("password", pwdMd5));
        String sign = StringUtils.upperCase(DigestUtils
                .md5Hex(clientSecret + timestamp + clientId + tokenPin + pwdMd5 + grantType + clientSecret));
        nameValuePairList.add(new BasicNameValuePair("sign",sign));
        UrlEncodedFormEntity urlEncodedFormEntity = null;
        try {
            urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        StringBuffer params = new StringBuffer();
        params.append("grant_type=access_token");
        params.append("&client_id="+clientId);
        params.append("&client_secret="+clientSecret);
        try {
            params.append("&username="+URLEncoder.encode(tokenPin,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        params.append("&password="+pwdMd5);
        params.append("&sign="+sign);
        System.out.println("params = " + params);

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost("https://bizapi.jd.com/oauth2/accessToken"+"?"+params);

        httpPost.setEntity(urlEncodedFormEntity);

        HttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String jsonStr = null;
        try {
            jsonStr = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        return jsonObject.getJSONObject("result").getString("access_token");
    }
}
