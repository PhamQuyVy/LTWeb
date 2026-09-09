<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Đặt lại mật khẩu</title>
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
    <h2>Đặt lại mật khẩu</h2>
    <p style="text-align:center;color:#555;">
        Mã OTP đã gửi tới: <b><%= request.getAttribute("email") %></b>
    </p>

    <% if (request.getAttribute("error") != null) { %>
        <div class="error"><%= request.getAttribute("error") %></div>
    <% } %>

    <form method="post" action="${pageContext.request.contextPath}/reset-password" novalidate
          onsubmit="return checkConfirm();">
        <input type="text" name="otpCode" maxlength="6" pattern="^\d{6}$"
               title="Mã OTP gồm 6 chữ số" placeholder="Mã OTP" required>
        <input type="password" id="newPassword" name="newPassword" placeholder="Mật khẩu mới"
               pattern="^(?=.*[A-Za-z])(?=.*\d).{6,}$"
               title="Tối thiểu 6 ký tự, gồm cả chữ và số" required>
        <input type="password" id="confirmPassword" name="confirmPassword"
               placeholder="Xác nhận mật khẩu mới" required>
        <button type="submit">Đặt lại mật khẩu</button>
    </form>
</div>
<script>
    function checkConfirm() {
        var a = document.getElementById('newPassword').value;
        var b = document.getElementById('confirmPassword').value;
        if (a !== b) { alert('Mật khẩu xác nhận không khớp.'); return false; }
        return true;
    }
</script>
</body>
</html>