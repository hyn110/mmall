package com.fmi110.mmall.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Cart implements Serializable {
    private Integer id;

    private Integer userId;

    private Integer productId;

    private Integer quantity;

    private Integer checked;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;


}