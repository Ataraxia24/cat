package cn.service.impl;

import cn.common.*;
import cn.domain.*;
import cn.dto.MessageDto;
import cn.mapper.InteractionMapper;
import cn.service.InteractionService;
import cn.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class InteractionServiceImpl extends ServiceImpl<InteractionMapper, Interaction> implements InteractionService {

    @Autowired
    private UserService userService;

    @Override
    public Result<String> save(String message) {
        //判断每个用户所留言的条数, 限制一人五条
        Long userId = BaseContext.get();
        log.info("interactionUserId-{}", userId);

        Interaction interaction = new Interaction();
        interaction.setUserId(userId);

        LambdaQueryWrapper<Interaction> interactionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        interactionLambdaQueryWrapper.eq(Interaction::getUserId, userId);

        List<Interaction> list = this.list(interactionLambdaQueryWrapper);

        if (list != null && list.size() > 4) {
            return Result.fail("留言信息数过多!", Code.BAD_REQUEST);
        }

        //替换敏感词汇
        message = WordFilter.replaceWords(message);
        interaction.setInteractionMessage(message);

        boolean isSave = this.save(interaction);
        log.error("userId-{}",userId);
        return isSave == true ? Result.success("留言成功!", Code.CREATED) : Result.fail("留言失败,请稍后再试", Code.BAD_REQUEST);
    }

    @Override
    public Result<Page> getList(Integer pageNum, Integer pageSize, String messageValue) {
        /**
         * 为防止遍历留言请求user表时过于频繁, 可以将留言表中的userId收集起来(map k)并去重, 再请求user表将所需的数据(map v)对应
         * 遍历map判断userId相等并将留言类中的额外属性赋值
         */

        //取出结果集
        LambdaQueryWrapper<Interaction> wrapper = new LambdaQueryWrapper();
        wrapper.like(StringUtils.hasText(messageValue), Interaction::getInteractionMessage, messageValue);
        wrapper.orderByDesc(Interaction::getPublishTime);

        Page<Interaction> page = new Page<>(pageNum, pageSize);
        this.page(page, wrapper);

        //存储留言表中的userId, 使用set去重
        List<Interaction> pageList = page.getRecords();
        Set<Long> userId = pageList.stream().map((item) -> {

            return item.getUserId();
        }) .collect(Collectors.toSet());

        //查找出对应的所有user数据, 使用map存储便于之后的判断
        Map<Long, String> map = new HashMap<>();
        for (Users users : userService.listByIds(userId)) {
            map.put(users.getUserId(), users.getUserEmail());
        }

        //重写records对象, 找出对应的UserId给该类的额外属性赋值
        pageList = pageList.stream().map((item) -> {
            item.setUserEmail(map.get(item.getUserId()));
            return item;
        }).collect(Collectors.toList());

        page.setRecords(pageList);

        return Result.success(page, Code.OK);
    }

}
