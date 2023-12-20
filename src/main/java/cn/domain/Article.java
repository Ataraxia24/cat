package cn.domain;

import cn.dto.MessageDto;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class Article {
    @TableId(type = IdType.ASSIGN_ID)
    private Long articleId;

    private Long catId;

    private Long userId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDate publishTime;

    private Integer articleThumb;

    private Integer articleCollect;

    @TableField(exist = false)
    private MessageDto messageDto;
}
