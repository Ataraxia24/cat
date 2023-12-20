package cn.service;

import cn.common.Result;
import cn.domain.Article;
import cn.dto.MessageDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ArticleService extends IService<Article> {
    void save(MessageDto messageDto);

    void remove(List<Long> ids);

    void updateMessage(MessageDto messageDto);

    Result<Page> selectPage(Integer pageNum, Integer pageSize, String name, List<Long> tagId);

    Result<Article> selectOne(Long articleId);
}
