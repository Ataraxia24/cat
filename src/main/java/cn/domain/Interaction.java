package cn.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class Interaction {
    @TableId(type = IdType.ASSIGN_ID)
    private Long interactionId;

    private Long userId;

    @NotEmpty(message = "不能为空")
    private String interactionMessage;

    @TableField(fill = FieldFill.INSERT)
    private LocalDate publishTime;

    @TableField(exist = false)
    private String userEmail;
}
