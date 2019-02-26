package com.cetc.config;

import org.jasig.cas.client.session.SingleSignOutFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
@Import(CasProperties.class)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    //认证
    @Autowired
    private CasAuthenticationProvider authenticationProvider;

    //认证点
    @Autowired
    private CasAuthenticationEntryPoint authenticationEntryPoint;

    //登出过滤
    @Autowired
    private LogoutFilter logoutFilter;

    //单点登出
    @Autowired
    private SingleSignOutFilter singleSignOutFilter;

    //cas配置
    @Autowired
    private CasProperties casProperties;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .exceptionHandling()
            .authenticationEntryPoint(authenticationEntryPoint)
        .and()
            .authorizeRequests()
            .anyRequest().authenticated()
        .and()
            //添加认证过滤(这里我遇到一个坑，如果通过注入方式加入，会出现循环依赖问题)
            .addFilter(casAuthenticationFilter())
            //登出过滤
            .addFilterBefore(logoutFilter, LogoutFilter.class)
            //单点登出过滤
            .addFilterBefore(singleSignOutFilter, CasAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //认证方式
        auth.authenticationProvider(authenticationProvider);
    }

    @Bean
    public CasAuthenticationFilter casAuthenticationFilter() throws Exception {
        //过滤器配置
        CasAuthenticationFilter casAuthenticationFilter = new CasAuthenticationFilter();
        //使用security的认证管理
        casAuthenticationFilter.setAuthenticationManager(authenticationManager());
        //拦截登录接口
        casAuthenticationFilter.setFilterProcessesUrl(casProperties.getClientLogin());
        return casAuthenticationFilter;
    }
}
