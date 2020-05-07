package com.jtt.hhl.testapi.controller;

import com.jtt.hhl.testapi.model.ResMessage;
import com.jtt.hhl.testapi.model.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * 测试application/json和application/x-www-form-urlencode的区别
 *
 * @author Herman
 * @createDate 2020/4/3 12:13
 */

@RestController
public class TestAPI {

    /**
     * 表单提交方法测试
     *
     * @author      Herman
     * @createDate  2020/4/3 14:33
     * @param user :
     * @param name :
     * @return      com.jtt.hhl.testapi.model.ResMessage
     */
    @RequestMapping(value = "/testapi-form", method = RequestMethod.POST)
    public ResMessage testapifrom(User user, @RequestParam String name){
        ResMessage resMessage = new ResMessage();
        resMessage.setMessage(user.getAddress());
        resMessage.setCode(user.getName());
        return resMessage;
    }

    /**
     * json提交方法测试
     *
     * @author      Herman
     * @createDate  2020/4/3 14:33
     * @param user :
     * @param name :
     * @return      com.jtt.hhl.testapi.model.ResMessage
     */
    @RequestMapping(value = "/testapi-json", method = RequestMethod.POST)
    public ResMessage testapijson(@RequestBody User user, @RequestParam String name, HttpServletRequest request){
        ResMessage resMessage = new ResMessage();
        resMessage.setMessage(user.getAddress());
        resMessage.setCode(user.getName());
        return resMessage;
    }


    @RequestMapping(value = "/testapi-form-get", method = RequestMethod.GET)
    public ResMessage testapiformget(@RequestParam String name) throws InterruptedException {
        ResMessage resMessage = new ResMessage();
        resMessage.setMessage(name);
        resMessage.setCode(name);
        Thread.sleep(800000);
        return resMessage;
    }
}
