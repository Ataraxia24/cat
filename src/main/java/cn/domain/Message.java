package cn.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class Message {
    @TableId(type = IdType.ASSIGN_ID)
    private Long catId;

    @NotNull(message = "该请求参数不能为空")
    private String catKind;

    @NotNull(message = "该请求参数不能为空")
    private String catBirthplace;

    @NotNull(message = "该请求参数不能为空")
    private String catFeature;

    private String catDescribe;

    @NotNull(message = "该请求参数不能为空")
    private String catDetail;

    private Integer catAdvice;

    private String catName;

}
