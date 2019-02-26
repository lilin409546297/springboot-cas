package com.cetc.config;

import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.validation.Cas30ServiceTicketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

@Configuration
@Import(CasProperties.class)
public class CasConfiguration {

    //cas相关参数
    @Autowired
    private CasProperties casProperties;

    //客户端的服务配置，主要用于跳转
    @Bean
    public ServiceProperties serviceProperties() {
        ServiceProperties serviceProperties = new ServiceProperties();
        //该项目的登录地址
        serviceProperties.setService(casProperties.getClientUrl() + casProperties.getClientLogin());
        serviceProperties.setAuthenticateAllArtifacts(true);
        return serviceProperties;
    }

    //cas认证点
    @Bean
    public CasAuthenticationEntryPoint casAuthenticationEntryPoint() {
        CasAuthenticationEntryPoint casAuthenticationEntryPoint = new CasAuthenticationEntryPoint();
        //cas的登录地址
        casAuthenticationEntryPoint.setLoginUrl(casProperties.getServerUrl() + casProperties.getServerLogin());
        //入口
        casAuthenticationEntryPoint.setServiceProperties(serviceProperties());
        return casAuthenticationEntryPoint;
    }

    //票据
    @Bean
    public Cas30ServiceTicketValidator cas30ServiceTicketValidator() {
        return new Cas30ServiceTicketValidator(casProperties.getServerUrl());
    }

    //认证支持
    @Bean
    public CasAuthenticationProvider casAuthenticationProvider(AuthDetailsService authDetailsService) {
        CasAuthenticationProvider casAuthenticationProvider = new CasAuthenticationProvider();
        casAuthenticationProvider.setKey("client1");
        casAuthenticationProvider.setServiceProperties(serviceProperties());
        casAuthenticationProvider.setTicketValidator(cas30ServiceTicketValidator());
        //本地登录后的操作，走security体系
        casAuthenticationProvider.setUserDetailsService(authDetailsService);
        //这里也可以使用setAuthenticationUserDetailsService管理
        //casAuthenticationProvider.setAuthenticationUserDetailsService();
        return casAuthenticationProvider;
    }

    //单点登录过滤
    @Bean
    public SingleSignOutFilter singleSignOutFilter() {
        SingleSignOutFilter singleSignOutFilter = new SingleSignOutFilter();
        singleSignOutFilter.setCasServerUrlPrefix(casProperties.getServerUrl());
        singleSignOutFilter.setIgnoreInitConfiguration(true);
        return singleSignOutFilter;
    }

    //登出过滤
    @Bean
    public LogoutFilter logoutFilter() {
        //重定向地址
        String logoutRedirectPath = casProperties.getServerUrl() + casProperties.getServerLogout() + "?service=" + casProperties.getClientUrl();
        LogoutFilter logoutFilter = new LogoutFilter(logoutRedirectPath, new SecurityContextLogoutHandler());
        //登出接口
        logoutFilter.setFilterProcessesUrl(casProperties.getServerLogout());
        return logoutFilter;
    }

}
