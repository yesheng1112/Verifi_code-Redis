<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Insert title here</title>
    <script src="${pageContext.request.contextPath}/static/jquery/jquery-3.1.0.js"></script>
    <link href="${pageContext.request.contextPath}/static/bs/css/bootstrap.min.css" rel="stylesheet"/>
    <script src="${pageContext.request.contextPath}/static/bs/js/bootstrap.min.js"></script>

</head>
<body>
<div class="container">
    <div class="row">
        <div id="alertdiv" class="col-md-12">
            <form class="navbar-form navbar-left" role="search" id="codeform">
                <div class="form-group">
                    <input type="text" class="form-control" placeholder="填写手机号" name="phone_no">
                    <button type="button" class="btn btn-default" id="sendCode">发送验证码</button>
                    <br>
                    <font id="countdown" color="red"></font>
                    <br>
                    <input type="text" class="form-control" placeholder="填写验证码" name="verify_code">
                    <button type="button" class="btn btn-default" id="verifyCode">确定</button>
                    <font id="result" color="green"></font><font id="error" color="red"></font>
                </div>
            </form>
        </div>
    </div>
</div>

</body>
<script type="text/javascript">
    var t = 120;//设定倒计时的时间
    var interval;

    function refer() {
        $("#countdown").text("请于" + t + "秒内填写验证码 "); // 显示倒计时
        t--; // 计数器递减
        if (t <= 0) {
            clearInterval(interval);
            $("#countdown").text("验证码已失效，请重新发送！ ");
        }
    }

    $(function () {
        $("#sendCode").click(function () {

            $.post("${pageContext.request.contextPath}/CodeSenderServlet", $("#codeform").serialize(), function (data) {
                if (data == "true") {
                    t = 120;
                    clearInterval(interval);
                    interval = setInterval("refer()", 1000);//启动1秒定时
                } else if (data == "limit") {
                    clearInterval(interval);
                    $("#countdown").text("单日发送超过次数！ ")
                }
            });
        });

        $("#verifyCode").click(function () {

            $.post("${pageContext.request.contextPath}/CodeVerifyServlet", $("#codeform").serialize(), function (data) {
                if (data == "true") {
                    $("#result").attr("color", "green");
                    $("#result").text("验证成功");
                    clearInterval(interval);
                    $("#countdown").text("")
                } else {
                    $("#result").attr("color", "red");
                    $("#result").text("验证失败");
                }
            });
        });

    });
</script>
</html>