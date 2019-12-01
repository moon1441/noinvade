package com.electric.noinvade.controller;

import com.electric.noinvade.vo.AuthFormVO;
import com.electric.noinvade.vo.UserDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Value("${custom.user-management.enable:false}")
    private boolean userManagementEnabled;

    private UserDetailsManager userDetailsManager;
    private PasswordEncoder passwordEncoder;

    @GetMapping("/info")
    public UserDetailVO getAuth(
        @AuthenticationPrincipal UserDetails user
    ) {
        return UserDetailVO.fromUserDetails(user);
    }

    @GetMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.logout();
            Cookie cookie = new Cookie("JSESSIONID", "");
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        } catch (ServletException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public void login(
        @AuthenticationPrincipal UserDetails user,
        @RequestBody AuthFormVO form,
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        if (user != null) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        try {
            request.login(form.getUsername(), form.getPassword());
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
        } catch (ServletException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    @PostMapping("/create")
    public UserDetailVO create(
        @RequestBody AuthFormVO form,
        HttpServletResponse response
    ) {
        if (!userManagementEnabled) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return null;
        }
        if (StringUtils.isEmpty(form.getUsername()) ||
            StringUtils.isEmpty(form.getPassword()) ||
            userDetailsManager.userExists(form.getUsername())
        ) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
        UserDetails user = new User(
            form.getUsername(),
            passwordEncoder.encode(form.getPassword()),
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
        userDetailsManager.createUser(user);
        response.setStatus(HttpServletResponse.SC_CREATED);
        return UserDetailVO.fromUserDetails(user);
    }

    @PostMapping("/update")
    public UserDetailVO update(
        @RequestBody AuthFormVO form,
        HttpServletResponse response
    ) {
        if (!userManagementEnabled) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return null;
        }
        if (StringUtils.isEmpty(form.getUsername()) ||
            StringUtils.isEmpty(form.getPassword()) ||
            !userDetailsManager.userExists(form.getUsername())
        ) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
        UserDetails user = new User(
            form.getUsername(),
            passwordEncoder.encode(form.getPassword()),
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
        userDetailsManager.updateUser(user);
        return UserDetailVO.fromUserDetails(user);
    }

    @Autowired
    public void setUserDetailsManager(UserDetailsManager userDetailsManager) {
        this.userDetailsManager = userDetailsManager;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
}
