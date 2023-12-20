package cn.service.impl;

import cn.domain.Speak;
import cn.mapper.SpeakMapper;
import cn.service.SpeakService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class SpeakServiceImpl extends ServiceImpl<SpeakMapper, Speak> implements SpeakService {
}
