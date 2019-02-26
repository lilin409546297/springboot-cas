package com.cetc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = "classpath:config/cas.properties")
@ConfigurationProperties(prefix = "cas")
public class CasProperties {

    //客户端url(本机)
    private String clientUrl;
    //登录接口
    private String clientLogin;
    //登出接口
    private String clientLogout;
    //服务端url
    private String serverUrl;
    //登录接口
    private String serverLogin;
    //登出接口
    private String serverLogout;
    //证书密匙路径
    private String trustStorePath;
    //密码
    private String trustStorePassword;

    public String getClientUrl() {
        return clientUrl;
    }

    public void setClientUrl(String clientUrl) {
        this.clientUrl = clientUrl;
    }

    public String getClientLogin() {
        return clientLogin;
    }

    public void setClientLogin(String clientLogin) {
        this.clientLogin = clientLogin;
    }

    public String getClientLogout() {
        return clientLogout;
    }

    public void setClientLogout(String clientLogout) {
        this.clientLogout = clientLogout;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getServerLogin() {
        return serverLogin;
    }

    public void setServerLogin(String serverLogin) {
        this.serverLogin = serverLogin;
    }

    public String getServerLogout() {
        return serverLogout;
    }

    public void setServerLogout(String serverLogout) {
        this.serverLogout = serverLogout;
    }

    public String getTrustStorePath() {
        return trustStorePath;
    }

    public void setTrustStorePath(String trustStorePath) {
        this.trustStorePath = trustStorePath;
    }

    public String getTrustStorePassword() {
        return trustStorePassword;
    }

    public void setTrustStorePassword(String trustStorePassword) {
        this.trustStorePassword = trustStorePassword;
    }
}
