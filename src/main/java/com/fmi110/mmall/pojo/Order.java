package com.fmi110.mmall.pojo;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Order implements Serializable {
    private Integer id;

    private Long orderNo;

    private Integer userId;

    private Integer shippingId;

    private BigDecimal payment;

    private Integer paymentType;

    private Integer postage;

    private Integer status;

    private Date paymentTime;

    private Date sendTime;

    private Date endTime;

    private Date closeTime;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

}