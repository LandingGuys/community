package life.lv.community.service.impl;

import life.lv.community.CommunityApplicationTests;
import life.lv.community.mapper.UserExtMapper;
import life.lv.community.model.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author lv
 * @date 2019-11-22 23:03
 */
public class UserServiceImplTest extends CommunityApplicationTests {
    @Autowired
    private UserExtMapper userExtMapper;
    @Test
    public void findNewUsers() {
        List<User> newUsers = userExtMapper.findNewUsers(10);
        System.out.println(newUsers.toString());

    }
}