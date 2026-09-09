<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quên mật khẩu</title>
    <style>
        body { font-family: Arial, sans-serif; background:#f4f6f8; }
        .box { max-width:400px; margin:60px auto; background:#fff;
               padding:24px; border-radius:8px; box-shadow:0 2px 8px #0002; }
        h2 { text-align:center; }
        input { width:100%; padding:8px; margin-top:12px; box-sizing:border-box; }
        button { width:100%; margin-top:20px; padding:10px; background:#2d6cdf;
                 color:#fff; border:none; border-radius:4px; cursor:pointer; }
        .error { color:#c0392b; margin-top:12px; text-align:center; }
    </style>
</head>
<body>
<div class="box">
    <h2>Quên mật khẩu</h2>
    <p style="text-align:center;color:#555;">Nhập email đã đăng ký để nhận mã OTP.</p>

    <% if (request.getAttribute("error") != null) { %>
        <div class="error"><%= request.getAttribute("error") %></div>
    <% } %>

    <form method="post" action="${pageContext.request.contextPath}/forgot-password">
        <input type="email" name="email" value="${email}" placeholder="Email" required>
        <button type="submit">Gửi mã OTP</button>
    </form>
</div>
</body>
</html>