package com.xxxx.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.seckill.controller.GoodsController;
import com.xxxx.seckill.exception.GlobalException;
import com.xxxx.seckill.mapper.UserMapper;
import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.service.IUserService;
import com.xxxx.seckill.utils.CookieUtil;
import com.xxxx.seckill.utils.MD5Util;
import com.xxxx.seckill.utils.UUIDUtil;
import com.xxxx.seckill.utils.ValidatorUtil;
import com.xxxx.seckill.vo.LoginVo;
import com.xxxx.seckill.vo.RespBean;
import com.xxxx.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author guanpeng
 *
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;



    /**
     * 更新密码
     *
     * @param userTicket
     * @param id
     * @param password
     * @return
     */
    @Override
    public RespBean updatePassword(String userTicket, Long id, String password) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new GlobalException(RespBeanEnum.MOBILE_NOT_EXIST);
        }
        user.setPassword(MD5Util.inputPassToDBPass(password, user.getSalt()));
        int result = userMapper.updateById(user);
        if (1 == result) {
             //删除Redis
            redisTemplate.delete("user:" + userTicket);
            return RespBean.success();
        }
        return RespBean.error(RespBeanEnum.PASSWORD_UPDATE_FAIL);
    }

    @Override
    public RespBean login(HttpServletRequest request, HttpServletResponse response,LoginVo loginVo) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
//        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)){
//            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
//        }
//        if(!ValidatorUtil.isMobile(mobile)){
//            return RespBean.error(RespBeanEnum.MOBILE_ERROR);
//        }
        //根据手机号获取用户
        User user = userMapper.selectById(mobile);
        if(null == user){
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }

        if(!MD5Util.formPassToDBPass(password,user.getSalt()).equals(user.getPassword())){
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }

        //生成cookie
        String ticket = UUIDUtil.uuid();
        redisTemplate.opsForValue().set("user:"+ticket,user);
//        request.getSession().setAttribute(ticket,user);
        CookieUtil.setCookie(request,response,"userTicket",ticket);


        return RespBean.success(ticket);
    }

    /**
     * 根据cookie获取用户
     * @param userTicket
     * @param request
     * @param response
    GoodsController.java
     * @return
     */
    @Override
    public User getUserByCookie(String userTicket, HttpServletRequest request,
                                HttpServletResponse response) {
        if (StringUtils.isEmpty(userTicket)) {
            return null;
        }
        User user = (User) redisTemplate.opsForValue().get("user:" +
                userTicket);
//        User user = JsonUtil.jsonStr2Object(userJson, User.class);
        if (null != user) {
            CookieUtil.setCookie(request,response,"userTicket",userTicket);
        }
        return user;
    }
}
