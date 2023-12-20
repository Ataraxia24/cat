package cn.service;

import cn.common.Result;
import cn.domain.Tag;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface TagService extends IService<Tag> {
    Result<String> saveTag(Tag tag);

    void remove(List<Long> ids);

    Result<String> update(Long id, String name);

    //pageNum为当前页面尺码, pageSize为每页显示多少条
    Result<List<Tag>> findTag(Integer pageNum, Integer pageSize);
}
