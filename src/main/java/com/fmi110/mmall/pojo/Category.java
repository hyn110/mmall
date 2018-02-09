package com.fmi110.mmall.pojo;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Data
public class Category implements Serializable {
    private Integer id;

    private Integer parentId;

    private String name;

    private Boolean status;

    private Integer sortOrder;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

}