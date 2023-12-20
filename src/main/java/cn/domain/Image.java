package cn.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class Image {
    @NotNull(message = "信息不能为空")
    private Long imageId;

    @NotNull(message = "信息不能为空")
    private Long catId;

    private String imageName;

    private String imageRemark;

    @NotEmpty(message = "图片不能为空")
    private String imageAddress;        //图片信息地址
}
