package com.thinkgem.jeesite.modules.agent.vo;

/**
 * Created by jfang on 2017/4/17.
 */
public class AgentCodeTypeGroup {
    private Integer codeType;
    private Integer codeCount;
    private Integer codeIsUse;

    public AgentCodeTypeGroup() {
        super();
    }

    public Integer getCodeType() {
        return codeType;
    }

    public void setCodeType(Integer codeType) {
        this.codeType = codeType;
    }

    public Integer getCodeCount() {
        return codeCount;
    }

    public void setCodeCount(Integer codeCount) {
        this.codeCount = codeCount;
    }

    public Integer getCodeIsUse() {
        return codeIsUse;
    }

    public void setCodeIsUse(Integer codeIsUse) {
        this.codeIsUse = codeIsUse;
    }
}
