<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Kích hoạt thành công</title>
    <style>
        body { font-family: Arial, sans-serif; background:#f4f6f8; }
        .box { max-width:400px; margin:80px auto; background:#fff;
               padding:24px; border-radius:8px; box-shadow:0 2px 8px #0002;
               text-align:center; }
        h2 { color:#1a7a33; }
        a { display:inline-block; margin-top:16px; color:#2d6cdf; }
    </style>
</head>
<body>
<div class="box">
    <h2>&#10004; Kích hoạt tài khoản thành công!</h2>
    <p>Tài khoản của bạn đã được xác thực. Bạn có thể đăng nhập ngay bây giờ.</p>
    <a href="${pageContext.request.contextPath}/">Về trang chủ</a>
</div>
</body>
</html>