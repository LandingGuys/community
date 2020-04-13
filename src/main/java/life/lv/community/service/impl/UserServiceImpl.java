package life.lv.community.service.impl;

import life.lv.community.mapper.UserExtMapper;
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
    @Autowired
    private UserExtMapper userExtMapper;
    @Override
    public void createOrUpdate(User user) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andAccountIdEqualTo(user.getAccountId());
        List<User> dbUsers = userMapper.selectByExample(userExample);
       if(dbUsers.size()!=0){
           //更新
           User dbUser=dbUsers.get(0);
           User updateUser = new User();
           updateUser.setGmtModified(System.currentTimeMillis());
           updateUser.setToken(user.getToken());
           UserExample example = new UserExample();
           example.createCriteria().andIdEqualTo(dbUser.getId());
           userMapper.updateByExampleSelective(updateUser, example);
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

    @Override
    public boolean findByName(String username) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andNameEqualTo(username);
        List<User> users = userMapper.selectByExample(userExample);
        if(users.size()!=0){
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void register(User user) {
        user.setGmtCreate(System.currentTimeMillis());
        user.setGmtModified(user.getGmtCreate());
        userMapper.insert(user);
    }

    @Override
    public boolean login(User user) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andNameEqualTo(user.getName())
                .andPasswordEqualTo(user.getPassword());
        List<User> users = userMapper.selectByExample(userExample);
        if(users.size()!=0){
            User dbUser=users.get(0);
            User updateUser = new User();
            updateUser.setGmtModified(System.currentTimeMillis());
            updateUser.setToken(user.getToken());
            UserExample example = new UserExample();
            example.createCriteria().andIdEqualTo(dbUser.getId());
            userMapper.updateByExampleSelective(updateUser, example);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public List<User> findNewUsers(Integer code) {
        List<User> newUsers = userExtMapper.findNewUsers(code);
        return newUsers;
    }

    @Override
    public User findById(Long id) {
        User user = userMapper.selectByPrimaryKey(id);
        return user;
    }
}
