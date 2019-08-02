package com.zinnaworks.nxpgtool.entity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class User {
    @NotNull(message = "用户名不能为空")
    @Length(min=5, max=20, message="用户名长度必须在5-20之间")
    @Pattern(regexp = "^[a-zA-Z_]\\w{4,19}$", message = "用户名必须以字母下划线开头，可由字母数字下划线组成")
    private String name;
    @NotNull(message = "age不能为空")
    private int age;
    @NotNull(message = "sex不能为空")
    private String sex;
}
