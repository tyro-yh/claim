package com.claim.server.menu.api;

import com.claim.server.common.po.SUser;
import com.claim.server.common.vo.ApiResponse;
import com.claim.server.menu.dao.SMenuDao;
import com.claim.server.menu.po.Meta;
import com.claim.server.menu.po.SMenu;
import com.claim.server.menu.vo.MenuVo;
import com.claim.server.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/menu")
public class MenuApi {

    @Autowired
    private SMenuDao sMenuDao;

    @RequestMapping(value = "getMenuList",method = RequestMethod.POST)
    public ApiResponse getMenuList() {
        SUser user = CommonUtil.getLoginUser();
        String userCode = user.getUserCode();
        List<SMenu> list = sMenuDao.selectUserMenu(userCode);
        List<MenuVo> result = new ArrayList<>();
        for (SMenu sMenu : list) {
            MenuVo vo = new MenuVo();
            vo.setPath(sMenu.getPath());
            vo.setName(sMenu.getName());
            vo.setIconCls(sMenu.getIconCls());
            Meta meta = new Meta();
            meta.setTitle(sMenu.getTitle());
            meta.setClosable(sMenu.getClosable());
            vo.setMeta(meta);
            this.getMenuList(vo,userCode,sMenu.getId());
            result.add(vo);
        }
        return new ApiResponse(result);
    }

    private void getMenuList(MenuVo parent,String userCode,Integer parentId) {
        List<SMenu> chilMenu = sMenuDao.selectByParentId(userCode,parentId);
        List<MenuVo> menuList = new ArrayList<>();
        for (SMenu sMenu : chilMenu) {
            MenuVo vo = new MenuVo();
            vo.setPath(sMenu.getPath());
            vo.setName(sMenu.getName());
            vo.setIconCls(sMenu.getIconCls());
            Meta meta = new Meta();
            meta.setTitle(sMenu.getTitle());
            meta.setClosable(sMenu.getClosable());
            vo.setMeta(meta);
            this.getMenuList(vo,userCode,sMenu.getId());
            menuList.add(vo);
        }
        parent.setChildren(menuList);
    }
}
