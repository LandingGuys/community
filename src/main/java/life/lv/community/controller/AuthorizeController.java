package life.lv.community.controller;

import life.lv.community.dto.*;
import life.lv.community.model.User;
import life.lv.community.provider.BaiDuProvider;
import life.lv.community.provider.GithubProvider;
import life.lv.community.provider.QQProvider;
import life.lv.community.service.UserService;
import life.lv.community.utils.MD5Util;
import life.lv.community.utils.ResultVoUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
@Slf4j
public class AuthorizeController {
    @Autowired
    private UserService userService;
    @Autowired
    private GithubProvider githubProvider;
    @Autowired
    private BaiDuProvider baiDuProvider;
    @Autowired
    JavaMailSenderImpl javaMailSender;
    @Autowired
    private StringRedisTemplate redisTemplate;
//    @Autowired
//    private StringRedisTemplate redisTemplate;

    @Value("${client_id}")
    private String clientId;
    @Value("${client_secret}")
    private String clientSecret;
    @Value("${Redirect_uri}")
    private String redirectUri;

    @Value("${Baidu_client_id}")
    private String BaiduClientId;
    @Value("${Baidu_client_secret}")
    private String BaiduClientSecret;
    @Value("${Baidu_Redirect_uri}")
    private String BaiduRedirectUri;

    @Value("${QQ_client_id}")
    private String QQClientId;
    @Value("${QQ_client_secret}")
    private String QQClientSecret;
    @Value("${QQ_Redirect_uri}")
    private String QQRedirectUri;

    /**
     *
     * @param code
     * @param response
     * @return
     */

    @GetMapping("/qqcallback")
    public String QQCallback(@RequestParam("code") String code,
                             HttpServletResponse response){
        QQAccessTokenDTO qqAccessTokenDTO=new QQAccessTokenDTO();
        qqAccessTokenDTO.setClient_id(QQClientId);
        qqAccessTokenDTO.setClient_secret(QQClientSecret);
        qqAccessTokenDTO.setCode(code);
        qqAccessTokenDTO.setGrant_type("authorization_code");
        qqAccessTokenDTO.setRedirect_uri(QQRedirectUri);
        String accessToken= QQProvider.getAccessToken(qqAccessTokenDTO);
        String openId=QQProvider.getOpenId(accessToken);
        QQUser qqUser=QQProvider.getUserInfo(openId,QQClientId,accessToken);

        if(qqUser!=null){
            //登录成功,写cookie和session/改为redis
            String token=UUID.randomUUID().toString();
//            Integer expire=7200;
//            redisTemplate.opsForValue().set(String.format("token_%s",token),String.valueOf(githubUser.getId()),expire, TimeUnit.SECONDS);
            User user=new User();
            user.setAccountId(String.valueOf(openId));
            user.setUserRegion("china");
            user.setUserIntroduction("这个人很懒，还没有简介");
            if(qqUser.getNickname()!=null){
                user.setName(qqUser.getNickname());
            }else{
                user.setName("QQ用户"+openId);
            }
            if(qqUser.getFigureurl_qq_1()!=null){
                user.setAvatarUrl(qqUser.getFigureurl_qq_1());
            }else{
                user.setAvatarUrl("https://shuixin.oss-cn-beijing.aliyuncs.com/tian.png");
            }
            user.setToken(token);
            userService.createOrUpdate(user);
            response.addCookie(new Cookie("token",token));
            return "redirect:/";
        }else{
            //登录失败，请重新登录
            log.error("callback get github error,{}",qqUser);
            return "redirect:/";
        }
    }

    /**
     * 百度OAuth2授权登录
     * @param code
     * @param response
     * @return
     */
    @GetMapping("/baiducallback")
    public String BaiduCallback(@RequestParam("code") String code,
                                HttpServletResponse response){
        BaiduAccessTokenDTO baiduAccessTokenDTO=new BaiduAccessTokenDTO();
        baiduAccessTokenDTO.setClient_id(BaiduClientId);
        baiduAccessTokenDTO.setClient_secret(BaiduClientSecret);
        baiduAccessTokenDTO.setRedirect_uri(BaiduRedirectUri);
        baiduAccessTokenDTO.setCode(code);
        baiduAccessTokenDTO.setGrant_type("authorization_code");
        String accessToken=BaiDuProvider.getAccessToken(baiduAccessTokenDTO);
        BaiduUser baiduUser=BaiDuProvider.getUser(accessToken);
        if(baiduUser!=null && baiduUser.getUserid()!=null){
            //登录成功,写cookie和session/改为redis
            String token=UUID.randomUUID().toString();
//            Integer expire=7200;
//            redisTemplate.opsForValue().set(String.format("token_%s",token),String.valueOf(githubUser.getId()),expire, TimeUnit.SECONDS);
            User user=new User();
            user.setAccountId(String.valueOf(baiduUser.getUserid()));
            user.setUserRegion("china");
            user.setUserIntroduction("这个人很懒，还没有简介");
            if(baiduUser.getUsername()!=null){
                user.setName(baiduUser.getUsername());
            }else{
                user.setName("BaiDu用户"+baiduUser.getUserid());
            }
            if(baiduUser.getPortrait()!=null){
                user.setAvatarUrl("http://tb.himg.baidu.com/sys/portrait/item/"+baiduUser.getPortrait());
            }else{
                user.setAvatarUrl("https://shuixin.oss-cn-beijing.aliyuncs.com/tian.png");
            }
            user.setToken(token);
            userService.createOrUpdate(user);
            response.addCookie(new Cookie("token",token));
            return "redirect:/";
        }else{
            //登录失败，请重新登录
            log.error("callback get github error,{}",baiduUser);
            return "redirect:/";
        }
    }

    /**
     * github授权登录
     * @param code
     * @param state
     * @param response
     * @return
     */
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
            user.setUserRegion("china");
            user.setUserIntroduction("这个人很懒，还没有简介");
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

    /**
     * 注册检查用户名重复
     * @param username
     * @return
     */
    @ResponseBody
    @GetMapping("/repeatName")
    public String repeatName(@RequestParam("username") String username){
        if(!userService.findByName(username)){
            return "用户已存在";
        }
        return null;
    }
    @ResponseBody
    @GetMapping("/emailYanZheng")
    public Object email(@RequestParam("mail") String mail){
        String result=String.valueOf((int)((Math.random()*9+1)*100000));
        redisTemplate.opsForValue().set(mail+"emailKey",result,15, TimeUnit.MINUTES);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        try {
            helper.setSubject("欢迎使用天空之城社区，离完成就差一步了");
            helper.setText("亲爱的用户：您好！感谢您使用天空之城社区服务。" +
                    "您正在进行邮箱验证，请在验证码输入框中输入此次验证码："+"<b style='color:red'>"+result+"</b>"+
                    " 请在15分钟内按页面提示提交验证码以完成验证，切勿将验证码泄露于他人。" +
                    "如非本人操作，请忽略此邮件，由此给您带来的不便请您谅解！",true);
            helper.setTo(mail);
            helper.setFrom("community@wast.club");
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        log.info("邮箱发送成功，验证码为"+result);
        return ResultVoUtil.success(result);
    }
    /**
     * 注册
     * @param username
     * @param password
     * @param model
     * @return
     */
    @PostMapping("/register")
    public String register(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam("mail") String mail,
                           @RequestParam("emailYan") String emailYan,
                           Model model){
        if(!StringUtils.isEmpty(username) &&!StringUtils.isEmpty(password) && !StringUtils.isEmpty(emailYan)){

            if(redisTemplate.hasKey(mail+"emailKey")){
                if(emailYan.equals(redisTemplate.opsForValue().get(mail+"emailKey"))){
                    if(userService.findByName(username)){
                        User user=new User();
                        user.setAccountId(UUID.randomUUID().toString());
                        user.setName(username);
                        user.setPassword(MD5Util.md5(password));
                        user.setAvatarUrl("https://shuixin.oss-cn-beijing.aliyuncs.com/tian.png");
                        user.setUserRegion("china");
                        user.setUserIntroduction("这个人很懒，还没有简介");
                        userService.register(user);
                        return "result";
                    }else{
                        model.addAttribute("msg","用户名已存在");
                        return "register";
                    }
                }else{
                    model.addAttribute("msg","邮箱验证码错误");
                    return "register";
                }
            }else{
                model.addAttribute("msg","验证码失效，请重新验证！");
                return "register";
            }


        }else{
            model.addAttribute("msg","用户名或密码或邮箱验证码为空");
            return "register";
        }

    }
    /**
     * 登录
     * @param username
     * @param password
     * @param response
     * @param model
     * @return
     */
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

    /**
     * 登出
     * @param response
     * @param request
     * @return
     */
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
