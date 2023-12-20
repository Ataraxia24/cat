package cn.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class Speak {
    @TableId(type = IdType.ASSIGN_ID)
    private Long speakId;

    @NotNull(message = "信息有误")
    private String speakMessage;

    @TableField(fill = FieldFill.INSERT)
    private LocalDate publishTime;
}
