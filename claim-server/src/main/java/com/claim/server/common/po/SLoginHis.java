package com.claim.server.common.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "s_loginhis")
public class SLoginHis {

    @TableId(value = "Id",type = IdType.UUID)
    private String id;

    @TableField(value = "UserCode")
    private String userCode;

    @TableField(value = "LoginTime")
    private Date loginTime;
}
