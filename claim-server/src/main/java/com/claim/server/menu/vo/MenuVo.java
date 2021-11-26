package com.claim.server.menu.vo;

import com.claim.server.menu.po.Meta;
import lombok.Data;

import java.util.List;

@Data
public class MenuVo {

    private String path;

    private String name;

    private String iconCls;

    private Meta meta;

    private List<MenuVo> children;
}
