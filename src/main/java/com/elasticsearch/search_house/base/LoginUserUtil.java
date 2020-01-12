package com.elasticsearch.search_house.base;

import com.elasticsearch.search_house.model.User;
import org.springframework.security.core.context.SecurityContextHolder;
import sun.plugin.liveconnect.SecurityContextHelper;

import javax.jws.soap.SOAPBinding;

public class LoginUserUtil {

    public static User load(){

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal != null && principal instanceof User) {
            return (User)principal;
        }

        return null;
    }

    public static Long getLoginUserId(){
        User user = load();
        if (user == null){
            return -1L;
        }

        return user.getId();
    }
}
