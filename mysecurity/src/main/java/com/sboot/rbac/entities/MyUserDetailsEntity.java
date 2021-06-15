package com.sboot.rbac.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author yinjiawei
 * @date 2021/06/15 23:29
 * @description 认证所需的信息
 */
public class MyUserDetailsEntity implements UserDetails {

    //注意这些域可以视为固定的，与需要实现的所有get方法对应,然后我们完成set方法对域进行设值

    /**
     * 用户的权限集合
     */
    Collection<? extends GrantedAuthority> authorities;
    /**
     * 密码
     */
    String password;
    /**
     * 用户名
     */
    String username;
    /**
     * 账号是否没过期，暂时不用
     */
    boolean accountNonExpired;
    /**
     * 账号是否没被锁定，暂时不用
     */
    boolean accountNonLocked;
    /**
     * 是否没过期，暂时不用
     */
    boolean credentialsNonExpired;
    /**
     * 账号是否可用
     */
    boolean enabled;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
