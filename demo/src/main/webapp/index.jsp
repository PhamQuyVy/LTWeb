<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.hcmute.demo.service.IProductService" %>
<%@ page import="com.hcmute.demo.service.impl.ProductServiceImpl" %>
<%
    IProductService productService = new ProductServiceImpl();
    request.setAttribute("listnewest", productService.findNewest(10));
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Trang chủ</title>
<style>
    body { font-family: Arial, sans-serif; margin:0; }
    header { background:#2d6cdf; color:#fff; padding:14px 24px; display:flex; justify-content:space-between; }
    header a { color:#fff; text-decoration:none; margin-left:14px; }
    .grid { display:flex; flex-wrap:wrap; gap:16px; padding:20px; }
    .card { width:200px; border:1px solid #ddd; border-radius:8px; padding:10px; text-align:center; }
    .card img { width:100%; height:140px; object-fit:cover; border-radius:4px; }
</style>
</head>
<body>

<header>
    <div><b>CRUD Demo</b></div>
    <div>
        <a href="<c:url value='/product'/>">Sản phẩm</a>
        <c:choose>
            <c:when test="${sessionScope.account != null}">
                <a href="<c:url value='/profile'/>">Trang cá nhân</a>
                <a href="<c:url value='/admin/products'/>">Quản lý sản phẩm</a>
                <a href="<c:url value='/admin/categories'/>">Quản lý danh mục</a>
                <a href="<c:url value='/logout'/>">Đăng xuất (${sessionScope.account.fullName})</a>
            </c:when>
            <c:otherwise>
                <a href="<c:url value='/login'/>">Đăng nhập</a>
                <a href="<c:url value='/register'/>">Đăng ký</a>
            </c:otherwise>
        </c:choose>
    </div>
</header>

<h2 style="text-align:center;">Sản phẩm mới nhất</h2>

<div class="grid">
<c:forEach items="${listnewest}" var="p">
    <c:if test="${p.images.length() >= 5 && p.images.substring(0,5)=='https'}">
        <c:url value="${p.images}" var="imgUrl"></c:url>
    </c:if>
    <c:if test="${p.images.length() < 5 || p.images.substring(0,5)!='https'}">
        <c:url value="/image?fname=${p.images}" var="imgUrl"></c:url>
    </c:if>
    <div class="card">
        <a href="<c:url value='/product/detail?id=${p.productId}'/>">
            <img src="${imgUrl}">
            <p>${p.productName}</p>
            <b>${p.price}</b>
        </a>
    </div>
</c:forEach>
</div>

</body>
</html>