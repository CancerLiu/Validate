package com.validate.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "validate")
public class ParameterCheckingProperties {

    private boolean enable;

    public boolean isEnable() {
        return enable;
    }

    public ParameterCheckingProperties setEnable(boolean enable) {
        this.enable = enable;
        return this;
    }
}
