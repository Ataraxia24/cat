package cn.service.impl;

import cn.domain.Medium;
import cn.mapper.MediumMapper;
import cn.service.MediumService;
import cn.service.MessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class MediumServiceImpl extends ServiceImpl<MediumMapper, Medium> implements MediumService {
}
