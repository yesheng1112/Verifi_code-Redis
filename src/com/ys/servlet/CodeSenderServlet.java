package com.ys.servlet;

import com.ys.utils.VerifyCodeConfig;
import redis.clients.jedis.Jedis;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;

@WebServlet(name = "CodeSenderServlet", urlPatterns = "/CodeSenderServlet")
public class CodeSenderServlet extends HttpServlet implements VerifyCodeConfig {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String phoneNo = request.getParameter("phone_no");
        if (phoneNo == null) {
            return;
        }
        //生成验证码
        String verifyCode = genCode(CODE_LEN);

        //保存redis一份
        String phoneNoKey = PHONE_PREFIX+phoneNo+PHONE_SUFFIX;
        String countKey = PHONE_PREFIX+phoneNo+COUNT_SUFFIX;

        Jedis jedis = new Jedis(HOST,PORT);

        String countStr = jedis.get(countKey);
        if (countStr != null){
            int count = Integer.parseInt(countStr);
            if (count>=COUNT_TIMES_1DAY){
                response.getWriter().print("limit");
                jedis.close();
                return;
            }
            jedis.incr(countKey);
        }else {
            jedis.setex(countKey,SECONDS_PER_DAY,"1");
        }

        jedis.setex(phoneNoKey,CODE_TIMEOUT,verifyCode);
        jedis.close();

        //发送验证码
        System.out.println("验证码:"+verifyCode);

        //返回
        response.getWriter().print(true);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    private String genCode(int len) {
        String code = "";
        for (int i = 0; i < len; i++) {
            int rand = new Random().nextInt(10);
            code += rand;
        }
        return code;
    }
}
