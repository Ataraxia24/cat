package cn.service.impl;

import cn.domain.Image;
import cn.mapper.ImageMapper;
import cn.service.ImageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ImageServiceImpl extends ServiceImpl<ImageMapper, Image> implements ImageService {
}
