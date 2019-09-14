package life.lv.community.controller;

import life.lv.community.dto.AccessTokenDTO;
import life.lv.community.dto.GithubUser;
import life.lv.community.model.User;
import life.lv.community.provider.GithubProvider;
import life.lv.community.service.UserService;
import life.lv.community.utils.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
@Slf4j
public class AuthorizeController {
    @Autowired
    private UserService userService;
    @Autowired
    private GithubProvider githubProvider;
//    @Autowired
//    private StringRedisTemplate redisTemplate;

    @Value("${client_id}")
    private String clientId;
    @Value("${client_secret}")
    private String clientSecret;
    @Value("${Redirect_uri}")
    private String redirectUri;

    @GetMapping("/callback")
    public String callback(@RequestParam("code") String code,
                           @RequestParam("state") String state,
                           HttpServletResponse response){

        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        String accessToken=githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        if(githubUser!=null && githubUser.getId()!=null){
            //登录成功,写cookie和session/改为redis
            String token=UUID.randomUUID().toString();
//            Integer expire=7200;
//            redisTemplate.opsForValue().set(String.format("token_%s",token),String.valueOf(githubUser.getId()),expire, TimeUnit.SECONDS);
            User user=new User();
            user.setAccountId(String.valueOf(githubUser.getId()));
            if(githubUser.getName()!=null){
                user.setName(githubUser.getName());
            }else{
                user.setName("github用户"+githubUser.getId());
            }
            if(githubUser.getAvatarUrl()!=null){
                user.setAvatarUrl(githubUser.getAvatarUrl());
            }else{
                user.setAvatarUrl("https://shuixin.oss-cn-beijing.aliyuncs.com/tian.png");
            }
            user.setToken(token);
            userService.createOrUpdate(user);
            response.addCookie(new Cookie("token",token));
            return "redirect:/";
        }else{
            //登录失败，请重新登录
            log.error("callback get github error,{}",githubUser);
            return "redirect:/";
        }

    }
    @ResponseBody
    @GetMapping("/repeatName")
    public String repeatName(@RequestParam("username") String username){
        if(!userService.findByName(username)){
            return "用户已存在";
        }
        return null;
    }
    @PostMapping("/register")
    public String register(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           Model model){
            if(!StringUtils.isEmpty(username) &&!StringUtils.isEmpty(password)){
                if(userService.findByName(username)){
                    User user=new User();
                    user.setAccountId(UUID.randomUUID().toString());
                    user.setName(username);
                    user.setPassword(MD5Util.md5(password));
                    user.setAvatarUrl("https://shuixin.oss-cn-beijing.aliyuncs.com/tian.png");
                    userService.register(user);
                    return "result";
                }else{
                    model.addAttribute("msg","用户名已存在");
                    return "register";
                }

            }
            return null;
    }
    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        HttpServletResponse response, Model model){
           if(!StringUtils.isEmpty(username) &&!StringUtils.isEmpty(password)) {
               String token = UUID.randomUUID().toString();
               User user = new User();
               user.setAccountId(UUID.randomUUID().toString());
               user.setName(username);
               user.setPassword(MD5Util.md5(password));
               user.setToken(token);
               if (userService.login(user)) {
                   response.addCookie(new Cookie("token", token));
                   return "redirect:/";
               } else {
                   model.addAttribute("msg", "用户名或密码错误");
                   return "login";
               }

           }
           return "";

    }
    @GetMapping("/logout")
    public String logout(HttpServletResponse response,
                         HttpServletRequest request){
        request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return "redirect:/";

    }

}
