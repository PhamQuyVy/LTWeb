<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Thông tin cá nhân</title>
    <style>
        .profile-box { max-width:480px; margin:40px auto; background:#fff;
               padding:24px; border-radius:8px; box-shadow:0 2px 8px #0002; }
        .profile-box h2 { text-align:center; margin-top:0; }
        .avatar-preview { display:block; width:120px; height:120px; object-fit:cover;
               border-radius:50%; margin:0 auto 16px; border:2px solid #2d6cdf; background:#eee; }
        label { display:block; margin-top:12px; font-weight:bold; }
        input[type=text], input[type=tel] { width:100%; padding:8px; margin-top:4px; box-sizing:border-box; }
        input[type=file] { margin-top:6px; }
        button { width:100%; margin-top:20px; padding:10px; background:#2d6cdf;
                 color:#fff; border:none; border-radius:4px; cursor:pointer; }
        .msg-success { color:#1e7d34; background:#e9f9ee; padding:10px; border-radius:4px; margin-top:12px; text-align:center; }
        .msg-error { color:#c0392b; background:#fdecea; padding:10px; border-radius:4px; margin-top:12px; text-align:center; }
    </style>
</head>
<body>

<div class="profile-box">
    <h2>Thông tin cá nhân</h2>

    <c:choose>
        <c:when test="${not empty user.avatar and fn:startsWith(user.avatar, 'http')}">
            <c:url value="${user.avatar}" var="avatarUrl"/>
        </c:when>
        <c:when test="${not empty user.avatar}">
            <c:url value="/image?fname=${user.avatar}" var="avatarUrl"/>
        </c:when>
    </c:choose>

    <c:if test="${not empty avatarUrl}">
        <img class="avatar-preview" src="${avatarUrl}" alt="avatar">
    </c:if>
    <c:if test="${empty avatarUrl}">
        <img class="avatar-preview" src="https://via.placeholder.com/120?text=No+Avatar" alt="avatar">
    </c:if>

    <c:if test="${not empty message}">
        <div class="msg-success">${message}</div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="msg-error">${error}</div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/profile" enctype="multipart/form-data">
        <label>Tên đăng nhập</label>
        <input type="text" value="${user.username}" disabled>

        <label>Họ và tên</label>
        <input type="text" name="fullName" value="${user.fullName}" required maxlength="100">

        <label>Số điện thoại</label>
        <input type="tel" name="phone" value="${user.phone}" maxlength="20" placeholder="Vd: 0901234567">

        <label>Ảnh đại diện (JPG/PNG, tối đa 5MB)</label>
        <input type="file" name="avatarFile" accept="image/*">

        <button type="submit">Lưu thay đổi</button>
    </form>
</div>

</body>
</html>
