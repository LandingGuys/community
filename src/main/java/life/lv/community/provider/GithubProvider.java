package life.lv.community.provider;

import com.google.gson.Gson;
import life.lv.community.dto.AccessTokenDTO;
import life.lv.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GithubProvider {
    Gson gson=new Gson();
    public String getAccessToken(AccessTokenDTO accessTokenDTO){

        MediaType JSON= MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, gson.toJson(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string= response.body().string();
            String token=string.split("&")[0].split("=")[1];
            return token;
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }
    public GithubUser getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+accessToken)
                .build();
        try  {
            Response response = client.newCall(request).execute();
            String string= response.body().string();
            GithubUser githubUser = gson.fromJson(string, GithubUser.class);
            return  githubUser;
        }catch (IOException e){

        }
        return null;
    }
}
