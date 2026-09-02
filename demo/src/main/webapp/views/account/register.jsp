<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Đăng ký tài khoản</title>
    <style>
        body { font-family: Arial, sans-serif; background:#f4f6f8; }
        .box { max-width:400px; margin:60px auto; background:#fff;
               padding:24px; border-radius:8px; box-shadow:0 2px 8px #0002; }
        h2 { text-align:center; }
        label { display:block; margin-top:12px; font-weight:bold; }
        input { width:100%; padding:8px; margin-top:4px; box-sizing:border-box; }
        button { width:100%; margin-top:20px; padding:10px; background:#2d6cdf;
                 color:#fff; border:none; border-radius:4px; cursor:pointer; }
        .error { color:#c0392b; margin-top:12px; }
    </style>
</head>
<body>
<div class="box">
    <h2>Đăng ký tài khoản</h2>

    <% if (request.getAttribute("error") != null) { %>
        <div class="error"><%= request.getAttribute("error") %></div>
    <% } %>

    <form method="post" action="${pageContext.request.contextPath}/register">
        <label>Họ và tên</label>
        <input type="text" name="fullName" required>

        <label>Email</label>
        <input type="email" name="email" required>

        <label>Tên đăng nhập</label>
        <input type="text" name="username" required>

        <label>Mật khẩu</label>
        <input type="password" name="password" required>

        <button type="submit">Đăng ký</button>
    </form>
</div>
</body>
</html>