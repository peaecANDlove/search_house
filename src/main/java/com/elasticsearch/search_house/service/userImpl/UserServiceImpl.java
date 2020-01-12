package com.elasticsearch.search_house.service.userImpl;

import com.elasticsearch.search_house.mapper.RoleMapper;
import com.elasticsearch.search_house.mapper.UserMapper;
import com.elasticsearch.search_house.model.Role;
import com.elasticsearch.search_house.model.User;
import com.elasticsearch.search_house.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public User findUserByName(String userName) {
        User user = userMapper.findUserByUsername(userName);

        if(user == null){
            return null;
        }

        List<Role> roles = roleMapper.findRolesByUserId(user.getId());
        if(roles == null|| roles.isEmpty()){
            throw new DisabledException("权限非法");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getName())));
        user.setAuthorityList(authorities);
        return user;
    }
}
