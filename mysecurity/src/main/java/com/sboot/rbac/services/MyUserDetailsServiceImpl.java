package com.sboot.rbac.services;

import com.sboot.rbac.entities.MyUserDetailsEntity;
import com.sboot.rbac.mappers.MyUserDetailsServiceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yinjiawei
 * @date 2021/06/15 23:26
 * @description 加载用户验证所需的信息
 */
@Service
public class MyUserDetailsServiceImpl implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(MyUserDetailsServiceImpl.class);

    @Resource
    MyUserDetailsServiceMapper myUserDetailsServiceMapper;

    /**
     * 这个方法是在spring-security的配置文件中配置usernameParameter("username")
     * 时调用的方法，所以这个方法的参数username就是前端中name为username的input框的值
     * 但是要注意通过username输入框传入的值务必能够唯一标识一位用户
     *
     * @param username 不要被名字所骗，这个username不仅可表示真正的username，还可以表示email、手机号等等
     *                 只要是能唯一的描述当前用户的字段即可
     * @return UserDetails
     * @throws UsernameNotFoundException 如果找不到抛出异常
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MyUserDetailsEntity userDetails = myUserDetailsServiceMapper.findUserDetail(username);
        if (null == userDetails) {
            throw new UsernameNotFoundException("用户不存在");
        }
        List<String> roles = myUserDetailsServiceMapper.findRoles(username);
        if (null == roles || roles.isEmpty()) {
            log.error("roles is null");
            return userDetails;
        }
        //根据规范需要添加前缀
        roles = roles.stream()
                .map(rc -> "ROLE_" + rc)
                .collect(Collectors.toList());
        List<String> authorities = new ArrayList<>(roles);
        //因为这里一个用户只对应一个角色，并且没有对api在数据库中进行建表，所以在这里进行简单粗暴的添加。
        //按正常流程这些都应该从数据库中查询获得
        if ("ROLE_user".equals(roles.get(0))) {
            authorities.add("/user/self");
        } else {
            authorities.add("/admin/self");
        }
        authorities.add("/user/home");
        userDetails.setAuthorities(AuthorityUtils.commaSeparatedStringToAuthorityList(
                String.join(",", authorities)
        ));
        return userDetails;
    }

}
