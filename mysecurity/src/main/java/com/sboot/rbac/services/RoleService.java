package com.sboot.rbac.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * @author yinjiawei
 * @date 2021/06/15 22:57
 * @description TODO
 */
@Service("rService")
public class RoleService {
    private static final Logger log = LoggerFactory.getLogger(RoleService.class);

    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            //本次要请求的资源的所需权限
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(request.getRequestURI());
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
            if (null == authorities || authorities.isEmpty()) {
                log.error("authorities 为空");
                return false;
            }
            return authorities.contains(authority);
        }
        return false;
    }
}
