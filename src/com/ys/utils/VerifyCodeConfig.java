package com.ys.utils;

public interface VerifyCodeConfig {
    String PHONE_PREFIX = "phoneno:";  //前缀

    String PHONE_SUFFIX = ":code";    //验证码key后缀

    String COUNT_SUFFIX = ":count";    //计数器key后缀

    int CODE_LEN = 6;   //随机码长度

    int CODE_TIMEOUT = 120;//随机码有效时间

    int COUNT_TIMES_1DAY = 3;  //单日最多发送次数

    int SECONDS_PER_DAY = 60 * 60 * 24; //单日秒数

    String HOST = "192.168.61.129"; //ip地址

    int PORT = 6379; //端口号
}

