package com.charles.dao;

import com.charles.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Charles
 * @since 2021-06-04
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户id获取导航菜单id
     * @param userId
     * @return
     */
    List<Long> getNavMenuIds(Long userId);

    List<User> listByMenuId(Long menuId);
}
