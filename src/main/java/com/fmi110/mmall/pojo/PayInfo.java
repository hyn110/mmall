package com.fmi110.mmall.pojo;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Data
public class PayInfo implements Serializable {
    private Integer id;

    private Integer userId;

    private Long orderNo;

    private Integer payPlatform;

    private String platformNumber;

    private String platformStatus;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

}