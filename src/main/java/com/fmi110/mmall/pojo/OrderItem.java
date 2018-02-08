package com.fmi110.mmall.pojo;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderItem implements Serializable {
    private Integer id;

    private Integer userId;

    private Long orderNo;

    private Integer productId;

    private String productName;

    private String productImage;

    private BigDecimal currentUnitPrice;

    private Integer quantity;

    private BigDecimal totalPrice;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

}