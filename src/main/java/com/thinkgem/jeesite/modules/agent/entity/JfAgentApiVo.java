package com.thinkgem.jeesite.modules.agent.entity;

/**
 * Created by asus on 2017/3/7.
 */
public class JfAgentApiVo {
    private String auth;
    private String udid;
    private long time;
    private String project;

    private String config;//配置字符串

    public JfAgentApiVo() {

    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }
}
