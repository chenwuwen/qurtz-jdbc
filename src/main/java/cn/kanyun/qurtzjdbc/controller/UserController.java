package cn.kanyun.qurtzjdbc.controller;

import cn.kanyun.qurtzjdbc.entity.Result;
import cn.kanyun.qurtzjdbc.entity.UserEntity;
import cn.kanyun.qurtzjdbc.entity.UserStatus;
import cn.kanyun.qurtzjdbc.service.UserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

/**
 * @author Kanyun
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 返回用户列表页面
     *
     * @return
     */
    @GetMapping(value = {"/", ""})
    public String userPage() {
        return "user";
    }


    /**
     * 用户列表
     *
     * @return
     */
    @GetMapping(value = {"/list"})
    @ResponseBody
    public Result<UserEntity> list(@RequestParam(value = "current") Long current, @RequestParam(value = "size") Integer size, @RequestParam(value = "entity", required = false) UserEntity entity) {
        Page pageObj = new Page<>(current, size);
        IPage<UserEntity> entityIPage = userService.queryPage(pageObj, entity);
        List<UserEntity> userEntityList = entityIPage.getRecords();
        Long count = entityIPage.getTotal();
        return Result.successHandler(userEntityList, count.intValue());
    }

    /**
     * 添加用户
     *
     * @param userEntity
     * @return
     */
    @PostMapping("/addUser")
    @ResponseBody
    public Result addUser(@RequestBody UserEntity userEntity) {
        try {
            userEntity.setStatus(UserStatus.NORMAL);
            userEntity.setRegisterDate(LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")));
            userService.insert(userEntity);
            return Result.successHandler();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.errorHandler("添加用户失败");
        }
    }

    /**
     * 更改密码页面
     *
     * @return
     */
    @GetMapping("/updatePassword")
    public String updatePassword() {
        return "updatePassword";
    }


    /**
     * 更改密码
     *
     * @return
     */
    @PostMapping("/updatePassword")
    public Result updatePassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword) {
        if (oldPassword.equals(newPassword)) {
            return Result.errorHandler("新密码不能与旧密码相同");
        }
        return Result.successHandler();
    }


}
