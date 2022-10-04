package com.xxxx.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.vo.LoginVo;
import com.xxxx.seckill.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author guanpeng
 *
 */
public interface IUserService extends IService<User> {

//    RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);
    RespBean login(HttpServletRequest request, HttpServletResponse response,
               LoginVo loginVo);

    User getUserByCookie(String userTicket, HttpServletRequest request,
                         HttpServletResponse response);
    /**
     * 更新密码
     * @param userTicket
     * @param id
     * @param password
     * @return
     */
    RespBean updatePassword(String userTicket,Long id,String password);

}
