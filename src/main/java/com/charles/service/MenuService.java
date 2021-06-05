package com.charles.service;

import com.charles.commons.dto.MenuDto;
import com.charles.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Charles
 * @since 2021-06-04
 */
public interface MenuService extends IService<Menu> {

    /**
     * 获取用户菜单导航栏信息
     * @return
     */
    List<MenuDto> getCurrentUserNav();

    /**
     * 获取菜单树
     * @return
     */
    List<Menu> tree();
}
