package life.lv.community.provider;

import com.alibaba.fastjson.JSON;
import life.lv.community.dto.BaiduAccessTokenDTO;
import life.lv.community.dto.BaiduUser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Author: F
 * @Date: 2019/9/24 20:58
 */
@Component
public class BaiDuProvider {
    public static String getAccessToken(BaiduAccessTokenDTO accessTokenDTO) {
        OkHttpClient client = new OkHttpClient();
        String urlString = "https://openapi.baidu.com/oauth/2.0/token?grant_type=" + accessTokenDTO.getGrant_type() +
                "&code=" + accessTokenDTO.getCode() + "&client_id=" + accessTokenDTO.getClient_id() + "&client_secret=" +
                accessTokenDTO.getClient_secret() + "&redirect_uri=" + accessTokenDTO.getRedirect_uri();
        Request request = new Request.Builder().url(urlString).get().build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            String accessToken = JSON.parseObject(string).getString("access_token");
            return accessToken;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static BaiduUser getUser(String accessToken) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("https://openapi.baidu.com/rest/2.0/passport/users/getInfo?access_token=" + accessToken).build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            BaiduUser baiduUser = JSON.parseObject(string, BaiduUser.class);
            return baiduUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}