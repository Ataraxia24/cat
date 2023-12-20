package cn.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *     @Null 被注释的元素必须为 null
 *     @NotNull 被注释的元素必须不为 null
 *     @AssertTrue 被注释的元素必须为 true
 *     @AssertFalse 被注释的元素必须为 false
 *     @Min(value) 被注释的元素必须是一个数字，其值必须大于等于指定的最小值
 *     @Max(value) 被注释的元素必须是一个数字，其值必须小于等于指定的最大值
 *     @DecimalMin(value) 被注释的元素必须是一个数字，其值必须大于等于指定的最小值
 *     @DecimalMax(value) 被注释的元素必须是一个数字，其值必须小于等于指定的最大值
 *     @Size(max=, min=)  被注释的元素的大小必须在指定的范围内
 *     @Digits (integer, fraction)    被注释的元素必须是一个数字，其值必须在可接受的范围内
 *     @Past 被注释的元素必须是一个过去的日期
 *     @Future 被注释的元素必须是一个将来的日期
 *     @Pattern(regex=,flag=) 被注释的元素必须符合指定的正则表达式
 *
 *     @NotBlank(message =)  一般常用于验证集合类型的对象,验证字符串非null，且长度必须大于0
 *     @Email 被注释的元素必须是电子邮箱地址
 *     @Length(min=,max=) 被注释的字符串的大小必须在指定的范围内
 *     @NotEmpty 一般常用于验证String类型的对象，对象不为null，且不为空串("")，不为只有空格的字符串(" ")，其实相当于执行了trim()；
 *     @Range(min=,max=,message=) 被注释的元素必须在合适的范围内
 */

@Data
public class Users {
    @TableId(type = IdType.ASSIGN_ID)
    private Long userId;        //主键

    private String userIp;

    @NotNull(message = "密码不能为空")
    private String userPassword;

    @Email(message = "email格式错误")
    @NotNull(message = "账户不能为空")
    private String userEmail;           //唯一

    private String userProfilePhoto;

    private LocalDateTime userRegisterTime;

    private LocalDate userBirthday;

    private Integer userAge;

    private String userNickname;        //唯一

    private Integer userSex;            //默认男

    //可以接收前端传递的验证码, 不对应参与数据库的任何运算
    @TableField(exist = false)
    private String code;

    @TableLogic(value = "0", delval = "1")
    private Integer deletedFlag;        //逻辑删除, 0表示未删除, 1表示已删除, 2表示封禁
}
