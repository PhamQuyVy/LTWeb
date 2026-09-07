<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><sitemesh:write property='title'/></title>
<style>
    body { font-family: Arial, sans-serif; margin:0; background:#f4f6f8; }
    header { background:#2d6cdf; color:#fff; padding:14px 24px; display:flex; justify-content:space-between; }
    header a { color:#fff; text-decoration:none; margin-left:14px; }
</style>
<sitemesh:write property='head'/>
</head>
<body>

<header>
    <div><b>CRUD Demo</b></div>
    <div>
        <a href="<c:url value='/'/>">Trang chủ</a>
        <a href="<c:url value='/profile'/>">Trang cá nhân</a>
        <c:if test="${sessionScope.account != null}">
            <a href="<c:url value='/logout'/>">Đăng xuất (${sessionScope.account.fullName})</a>
        </c:if>
    </div>
</header>

<sitemesh:write property='body'/>

</body>
</html>