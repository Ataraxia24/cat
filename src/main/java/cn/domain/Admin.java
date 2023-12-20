package cn.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Admin {
    @TableId(type = IdType.ASSIGN_ID)
    private Long adminId;

    private String adminUsername;

    private String adminPassword;

    private Long userId;

    @TableField(exist = false)
    private String code;
}
