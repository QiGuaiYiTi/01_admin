package com.charles.service.impl;

import com.charles.entity.Menu;
import com.charles.dao.MenuMapper;
import com.charles.service.MenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
