package com.charles.commons.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Description :Menu数据传输对象
 *
 * @author : Charles
 * @created : 2021/6/5
 */
@Data
public class MenuDto implements Serializable {

    private Long id;
    private String name;
    private String title;
    private String icon;
    private String path;
    private String component;
    private List<MenuDto> children = new ArrayList<>();


}
