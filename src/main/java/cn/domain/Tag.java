package cn.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Tag implements Serializable {
    @TableId(type = IdType.ASSIGN_ID)
    private Long tagId;

    @NotEmpty(message = "标签名不能为空")
    private String tagName;

    private LocalDateTime tagMakeTime;

    @Min(value = 1, message = "排序规则必须大于等于1")
    private Integer tagSort;
}
