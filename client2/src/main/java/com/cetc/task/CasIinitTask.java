package com.cetc.task;

import com.cetc.config.CasProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Component
@Import(CasProperties.class)
public class CasIinitTask {

    @Autowired
    private CasProperties casProperties;

    @PostConstruct
    public void loadKeystore() throws IOException {
        //如果使用https,则必须加入密钥
        Assert.isTrue(!(casProperties.getServerUrl().startsWith("https") && casProperties.getTrustStorePath() == null),
                "trustStorePath must not null to configuration https");
        //密钥
        if (!StringUtils.isEmpty(casProperties.getTrustStorePath())) {
            Resource resource = new ClassPathResource(casProperties.getTrustStorePath());
            System.setProperty("javax.net.ssl.trustStore", resource.getFile().getAbsolutePath());
        }
        //有可能密码的情况
        if (StringUtils.isEmpty(casProperties.getTrustStorePassword())) {
            System.setProperty("javax.net.ssl.trustStorePassword", casProperties.getTrustStorePassword());
        }
    }
}
