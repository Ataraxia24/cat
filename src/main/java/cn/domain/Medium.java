package cn.domain;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Medium {
    @NotNull(message = "数据错误")
    private Long catId;

    @NotNull(message = "数据错误")
    private Long tagId;
}
