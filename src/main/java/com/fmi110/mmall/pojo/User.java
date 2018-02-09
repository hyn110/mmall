package com.fmi110.mmall.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
@Data
public class User implements Serializable {
    private Integer id;

    private String username;
    @JsonIgnore
    private String password;

    private String email;

    private String phone;

    private String question;

    private String answer;

    private Integer role;
    @JsonIgnore
    private Date createTime;
    @JsonIgnore
    private Date updateTime;

    private static final long serialVersionUID = 1L;

}