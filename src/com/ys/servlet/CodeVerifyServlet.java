package com.ys.servlet;

import com.ys.utils.VerifyCodeConfig;
import redis.clients.jedis.Jedis;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "CodeVerifyServlet", urlPatterns = "/CodeVerifyServlet")
public class CodeVerifyServlet extends HttpServlet implements VerifyCodeConfig {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String phoneNo = request.getParameter("phone_no");
        String verifyCode = request.getParameter("verify_code");
        if (phoneNo == null || verifyCode == null){
            return;
        }
        //保存redis一份
        String phoneNoKey = PHONE_PREFIX+phoneNo+PHONE_SUFFIX;
        Jedis jedis = new Jedis(HOST,PORT);
        String expectedCode = jedis.get(phoneNoKey);
        jedis.close();
        if (verifyCode.equals(expectedCode)){
            response.getWriter().print(true);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
