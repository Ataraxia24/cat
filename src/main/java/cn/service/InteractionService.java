package cn.service;

import cn.common.Result;
import cn.domain.Interaction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface InteractionService extends IService<Interaction> {
    Result<String> save(String message);

    /**
     * 后台管理, 查找详细信息
     * @param pageNum
     * @param pageSize
     * @param messageValue      搜索的留言信息
     * @return
     */
    Result<Page> getList(Integer pageNum, Integer pageSize, String messageValue);
}
