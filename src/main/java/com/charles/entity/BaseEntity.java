package com.charles.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Description :
 *
 * @author : Sam
 * @created : 2021/6/4
 */
@Data
public class BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private LocalDateTime created;
    private LocalDateTime updated;
    private Integer statu;
}
