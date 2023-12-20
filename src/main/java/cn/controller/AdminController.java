package cn.controller;

import cn.common.Code;
import cn.common.Result;
import cn.domain.Admin;
import cn.domain.Users;
import cn.service.AdminService;
import cn.service.UserService;
import cn.utils.JwtUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    @Autowired
    private AdminService adminService;

    //登录, 由前端传递是否验证成功的参数,发放token
    @PostMapping("/verifyAdmin")
    public Result<String> login(@RequestBody Admin admin) {

        return adminService.enterUser(admin);
    }

    //注册, 由管理员注册, 需要token
    @PostMapping("/registerAdmin")
    public Result<String> register(@RequestBody Admin admin) {

        return adminService.insertUser(admin);
    }

    //查找所有管理员信息
    @GetMapping("/page/{pageNum}/{pageSize}")
    public Result<List<Admin>> getAdminPage(@PathVariable Integer pageNum, @PathVariable Integer pageSize) {
        log.info("pageNum={}, pageSize={}", pageNum, pageSize);

        Page<Admin> page = new Page(pageNum, pageSize);

        adminService.page(page);

        return Result.success(page.getRecords(), Code.OK);
    }

    //修改当前管理员的账密
    @PutMapping("/update")
    public Result<String> updateAdmin(@RequestBody Admin admin) {
        //加密
        String password = DigestUtils.md5DigestAsHex(admin.getAdminPassword().getBytes());

        //根据管理员id修改
        LambdaUpdateWrapper<Admin> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Admin::getAdminId, admin.getAdminId());
        wrapper.set(StringUtils.hasText(admin.getAdminUsername()), Admin::getAdminUsername, admin.getAdminUsername());
        wrapper.set(StringUtils.hasText(password), Admin::getAdminPassword, password);

        boolean update = adminService.update(wrapper);

        return update ? Result.success("修改成功!", Code.OK) : Result.fail("修改失败!", Code.BAD_REQUEST);
    }

    //删除管理员账户
    @DeleteMapping("/delete/{id}")
    public Result<String> deleteAdmin(@PathVariable Long id) {
        adminService.removeById(id);

        return Result.success("删除成功", Code.DELETED);
    }
}
