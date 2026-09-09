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
        small { color:#777; }
    </style>
</head>
<body>
<div class="box">
    <h2>Đăng ký tài khoản</h2>

    <% if (request.getAttribute("error") != null) { %>
        <div class="error"><%= request.getAttribute("error") %></div>
    <% } %>

    <form method="post" action="${pageContext.request.contextPath}/register" novalidate
          onsubmit="return checkConfirmPassword();">
        <label>Họ và tên</label>
        <input type="text" name="fullName" value="${fullName}" required maxlength="100">

        <label>Email</label>
        <input type="email" name="email" value="${email}" required>

        <label>Tên đăng nhập</label>
        <input type="text" name="username" value="${username}" required
               pattern="^[a-zA-Z0-9_]{4,20}$" title="4-20 ký tự, chữ/số/gạch dưới">
        <small>4-20 ký tự, chỉ gồm chữ, số và dấu gạch dưới.</small>

        <label>Mật khẩu</label>
        <input type="password" id="password" name="password" required
               pattern="^(?=.*[A-Za-z])(?=.*\d).{6,}$"
               title="Tối thiểu 6 ký tự, gồm cả chữ và số">
        <small>Tối thiểu 6 ký tự, gồm cả chữ và số.</small>

        <label>Xác nhận mật khẩu</label>
        <input type="password" id="confirmPassword" name="confirmPassword" required>

        <button type="submit">Đăng ký</button>
    </form>
</div>

<script>
    function checkConfirmPassword() {
        var pw = document.getElementById('password').value;
        var cf = document.getElementById('confirmPassword').value;
        if (pw !== cf) {
            alert('Mật khẩu xác nhận không khớp.');
            return false;
        }
        return true;
    }
</script>
</body>
</html>