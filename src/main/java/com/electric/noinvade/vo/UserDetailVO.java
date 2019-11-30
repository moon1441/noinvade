package com.electric.noinvade.vo;

import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;

@Data
public class UserDetailVO {
    private String username;

    public static UserDetailVO fromUserDetails(UserDetails user) {
        if (user == null) {
            return null;
        }
        UserDetailVO vo = new UserDetailVO();
        vo.setUsername(user.getUsername());
        return vo;
    }
}
