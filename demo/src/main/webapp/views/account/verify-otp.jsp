<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Xác thực OTP</title>
    <style>
        body { font-family: Arial, sans-serif; background:#f4f6f8; }
        .box { max-width:400px; margin:60px auto; background:#fff;
               padding:24px; border-radius:8px; box-shadow:0 2px 8px #0002; }
        h2 { text-align:center; }
        p.sub { text-align:center; color:#555; }
        input[name="otpCode"] { width:100%; padding:10px; margin-top:12px;
               box-sizing:border-box; text-align:center; font-size:22px;
               letter-spacing:6px; }
        button { width:100%; margin-top:16px; padding:10px; background:#2d6cdf;
                 color:#fff; border:none; border-radius:4px; cursor:pointer; }
        .link-btn { width:100%; margin-top:10px; padding:10px; background:#fff;
                 color:#2d6cdf; border:1px solid #2d6cdf; border-radius:4px;
                 cursor:pointer; }
        .error { color:#c0392b; margin-top:12px; text-align:center; }
        .message { color:#1a7a33; margin-top:12px; text-align:center; }
    </style>
</head>
<body>
<div class="box">
    <h2>Xác thực OTP</h2>
    <p class="sub">
        Mã OTP đã được gửi tới email:
        <b><%= request.getAttribute("email") %></b>
    </p>

    <% if (request.getAttribute("error") != null) { %>
        <div class="error"><%= request.getAttribute("error") %></div>
    <% } %>
    <% if (request.getAttribute("message") != null) { %>
        <div class="message"><%= request.getAttribute("message") %></div>
    <% } %>

    <form method="post" action="${pageContext.request.contextPath}/verify-otp">
        <input type="text" name="otpCode" maxlength="6" placeholder="------" required>
        <button type="submit">Xác nhận</button>
    </form>

    <form method="post" action="${pageContext.request.contextPath}/verify-otp">
        <input type="hidden" name="action" value="resend">
        <button type="submit" class="link-btn">Gửi lại mã OTP</button>
    </form>
</div>
</body>
</html>