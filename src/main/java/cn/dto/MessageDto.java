package cn.dto;

import cn.domain.Image;
import cn.domain.Message;
import cn.domain.Tag;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class MessageDto extends Message {

    @NotBlank(message = "标签不能为空")
    private List<Tag> tags;

    @NotBlank(message = "图片不能为空")
    private List<Image> picture;
}
