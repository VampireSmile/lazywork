package com.sboot.rbac.configs;

import com.sboot.rbac.handlers.MyLogoutHandler;
import com.sboot.rbac.services.MyUserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import javax.annotation.Resource;

/**
 * @author yinjiawei
 * @date 2021/06/15 22:40
 * @description 权限配置类
 */
@Configuration
@EnableWebSecurity
public class MySecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * 密码加密所需的密钥
     */
    private static final String ENCODER_SECRET_KEY = "aheader";

    @Resource
    MyUserDetailsServiceImpl myUserDetailsServiceImpl;

    @Resource
    MyLogoutHandler myLogoutHandler;

    /**
     * 使用formlogin
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .formLogin()
                //一旦用户的请求没有权限就跳转到该页面
                .loginPage("/my/login")
                //登录表单form中action的地址，也就是处理认证请求的路径
                .loginProcessingUrl("/login")
                //登录表单form中用户名输入框input的name属性值
                .usernameParameter("username")
                //登录表单form中密码输入框input的name属性值
                .passwordParameter("password")
                .defaultSuccessUrl("/user/home")
                .failureUrl("/my/error")
                .and()
                .logout()
                .logoutSuccessHandler(myLogoutHandler)
                .and()
                .authorizeRequests()
                //允许任何人访问的路径
                .antMatchers("/", "/my/login",
                        "/logout", "/my/error", "/my/encode/**", "/favicon.ico").permitAll()
                .anyRequest().access("@rService.hasPermission(request,authentication)");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsServiceImpl)
                //配置加密规则
                .passwordEncoder(pbkdf2PasswordEncoder());
    }

    /**
     * 放行静态资源
     * 不在第一个config方法里面添加规则的原因：第一个config中的所有接口都是要经过过滤器的，
     * 而这里配置的路径是不需要经过过滤器的。
     * 所以一般推荐在第一个config方法用来添加接口规则，在下面这个config方法用来添加静态资源访问规则
     */
    @Override
    public void configure(WebSecurity web) {
        //将项目中静态资源路径开放出来，这些也是不需要任何权限就能访问的资源
        web.ignoring().antMatchers("/css/**", "/fonts/**", "/img/**", "/js/**");
    }

    /**
     * 使用Pbkdf2PasswordEncoder对密码加密
     *
     * @return PasswordEncoder
     */
    @Bean
    public Pbkdf2PasswordEncoder pbkdf2PasswordEncoder() {
        return new Pbkdf2PasswordEncoder(ENCODER_SECRET_KEY);
    }

    @Override
    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}
