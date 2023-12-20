package cn.service.impl;

import cn.common.Code;
import cn.common.Result;
import cn.domain.Tag;
import cn.exception.AuthorizationException;
import cn.mapper.TagMapper;
import cn.service.TagService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
    @Override
    public Result<String> saveTag(Tag tag) {
        if (isExist(tag.getTagName()))      return Result.fail("标签名重复!", Code.BAD_REQUEST);

        tag.setTagMakeTime(LocalDateTime.now());
        this.save(tag);

        return Result.success("操作成功!", Code.CREATED);
    }

    @Override
    public void remove(List<Long> ids){

        this.removeByIds(ids);
    }

    @Override
    public Result<String> update(Long id, String name) {

        if (isExist(name))      return Result.fail("标签名重复!", Code.BAD_REQUEST);

        LambdaUpdateWrapper<Tag> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Tag::getTagId, id);
        wrapper.set(Tag::getTagName, name);
        wrapper.set(Tag::getTagMakeTime, LocalDateTime.now());

        this.update(wrapper);

        return Result.success("更新完成!", Code.OK);
    }

    @Override
    public Result<List<Tag>> findTag(Integer pageNum, Integer pageSize) {
        //时间的优先级高于排序, 但仅限于在相同的排序序号中
        //因此先对序号进行分组管理
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.groupBy(Tag::getTagId);                                 //分组查询会去掉重复, 添加id使不去重
        wrapper.groupBy(Tag::getTagSort);

        //再对分组后的时间进行升序排序
        wrapper.orderByAsc(Tag::getTagMakeTime);

        Page<Tag> page = new Page(pageNum, pageSize);

        this.page(page, wrapper);

        return Result.success(page.getRecords(), Code.OK);
    }

    private boolean isExist(String name) {
            //判断当前表内是否存在相同标签
            LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Tag::getTagName, name);

            Tag one = this.getOne(wrapper);

            if (one != null) {
                return true;
            }

            return false;
    }
}
