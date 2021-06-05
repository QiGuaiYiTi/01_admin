package com.charles.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.charles.commons.dto.MenuDto;
import com.charles.dao.UserMapper;
import com.charles.entity.Menu;
import com.charles.dao.MenuMapper;
import com.charles.entity.User;
import com.charles.service.MenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.charles.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Charles
 * @since 2021-06-04
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    UserService userService;

    @Autowired
    UserMapper userMapper;

    /**
     * 获取当前用户菜单列表信息
     * @return
     */
    @Override
    public List<MenuDto> getCurrentUserNav() {
        //获取当前登录用户名
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //获取当前登录用户
        User user = userService.getByUsername(username);
        //根据当前用户id获取菜单id列表
        List<Long> menuIds = userMapper.getNavMenuIds(user.getId());
        //获取当前用户菜单列表
        List<Menu> menus = this.listByIds(menuIds);

        // 转树状结构
        List<Menu> menuTree = buildTreeMenu(menus);

        // 实体转DTO
        return convert(menuTree);
    }

    public List<Menu> tree() {
        // 获取所有菜单信息
        List<Menu> sysMenus = this.list(new QueryWrapper<Menu>().orderByAsc("orderNum"));

        // 转成树状结构
        return buildTreeMenu(sysMenus);
    }

    private List<MenuDto> convert(List<Menu> menuTree) {
        List<MenuDto> menuDtos = new ArrayList<>();
        //根据menu 生成 menuDto
        menuTree.forEach(m -> {
            MenuDto dto = new MenuDto();
            dto.setId(m.getId());
            dto.setName(m.getPerms());
            dto.setTitle(m.getName());
            dto.setComponent(m.getComponent());
            dto.setPath(m.getPath());

            if (m.getChildren().size() > 0) {

                // 子节点调用当前方法进行再次转换
                dto.setChildren(convert(m.getChildren()));
            }

            menuDtos.add(dto);
        });

        return menuDtos;
    }

    private List<Menu> buildTreeMenu(List<Menu> menus) {

        List<Menu> finalMenus = new ArrayList<>();

        // 先各自寻找到各自的孩子
        for (Menu menu : menus) {

            for (Menu e : menus) {
                if (menu.getId() == e.getParentId()) {
                    menu.getChildren().add(e);
                }
            }

            // 提取出父节点
            if (menu.getParentId() == 0L) {
                finalMenus.add(menu);
            }
        }
//        System.out.println(JSONUtil.toJsonStr(finalMenus));
        return finalMenus;
    }
}
