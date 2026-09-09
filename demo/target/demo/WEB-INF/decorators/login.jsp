<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><sitemesh:write property='title'/></title>
<sitemesh:write property='head'/>
</head>
<body>
<sitemesh:write property='body'/>
<script>
document.querySelector("form").addEventListener("submit", function (e) {
    const u = this.username.value.trim();
    const p = this.password.value;
    if (!u || !p) {
        alert("Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu.");
        e.preventDefault();
    }
});
</script>
</body>
</html>