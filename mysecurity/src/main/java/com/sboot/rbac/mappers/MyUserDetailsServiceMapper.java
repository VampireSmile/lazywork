package com.sboot.rbac.mappers;

import com.sboot.rbac.entities.MyUserDetailsEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author yinjiawei
 * @date 2021/01/08 16:32
 * @description 从数据库动态查询权限信息
 */
@Mapper
public interface MyUserDetailsServiceMapper {

    /**
     * 查询用户信息
     *
     * @param queryStr 查询的字符串
     * @return 用户相关信息
     */
    MyUserDetailsEntity findUserDetail(String queryStr);

    /**
     * 查询用户角色
     *
     * @param queryStr 查询的字符串
     * @return roles of user
     */
    List<String> findRoles(String queryStr);
}
