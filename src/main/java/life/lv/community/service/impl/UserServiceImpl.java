package life.lv.community.service.impl;

import life.lv.community.mapper.UserMapper;
import life.lv.community.model.User;
import life.lv.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public void createOrUpdate(User user) {
       User dbUser= userMapper.findByAccountId(user.getAccountId());
       if(dbUser!=null){
           //更新
           dbUser.setGmtModified(System.currentTimeMillis());
           dbUser.setName(user.getName());
           dbUser.setAvatarUrl(user.getAvatarUrl());
           dbUser.setToken(user.getToken());
           userMapper.update(dbUser);

       }else{
           //插入
           user.setGmtCreate(System.currentTimeMillis());
           user.setGmtModified(user.getGmtCreate());
           userMapper.insert(user);
       }


    }
}
