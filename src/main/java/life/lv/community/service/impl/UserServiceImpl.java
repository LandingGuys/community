package life.lv.community.service.impl;

import life.lv.community.mapper.UserMapper;
import life.lv.community.model.User;
import life.lv.community.model.UserExample;
import life.lv.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public void createOrUpdate(User user) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andAccountIdEqualTo(user.getAccountId());
        List<User> dbUsers = userMapper.selectByExample(userExample);
        //User dbUser= userMapper.findByAccountId(user.getAccountId());
       if(dbUsers.size()!=0){
           //更新
           User dbUser=dbUsers.get(0);
//           dbUser.setGmtModified(System.currentTimeMillis());
//           dbUser.setName(user.getName());
//           dbUser.setAvatarUrl(user.getAvatarUrl());
//           dbUser.setToken(user.getToken());
           User updateUser = new User();
           updateUser.setGmtModified(System.currentTimeMillis());
           updateUser.setToken(user.getToken());

           UserExample example = new UserExample();
           example.createCriteria().andIdEqualTo(dbUser.getId());
           userMapper.updateByExampleSelective(updateUser, example);
           //userMapper.update(dbUser);
       }else{
           //插入
           user.setGmtCreate(System.currentTimeMillis());
           user.setGmtModified(user.getGmtCreate());
           userMapper.insert(user);
       }
    }

    @Override
    public void updateUser(User user) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andAccountIdEqualTo(user.getAccountId());
        List<User> users = userMapper.selectByExample(userExample);

       // User dbUser= userMapper.findByAccountId(user.getAccountId());
        if(users.size()!=0){
            User dbUser=users.get(0);
            User updateUser = new User();
            updateUser.setUserName(user.getUserName());
            updateUser.setName(user.getName());
            updateUser.setUserIndustry(user.getUserIndustry());
            updateUser.setUserRegion(user.getUserRegion());
            updateUser.setUserIntroduction(user.getUserIntroduction());
            UserExample example = new UserExample();
            example.createCriteria().andIdEqualTo(dbUser.getId());
            userMapper.updateByExampleSelective(updateUser, example);
            //userMapper.update(dbUser);
        }


    }
}
