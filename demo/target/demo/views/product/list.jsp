<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Tất cả sản phẩm</title>
<style>
    body { font-family: Arial, sans-serif; }
    .grid { display:flex; flex-wrap:wrap; gap:16px; padding:20px; }
    .card { width:220px; border:1px solid #ddd; border-radius:8px; padding:10px; text-align:center; }
    .card img { width:100%; height:150px; object-fit:cover; border-radius:4px; }
    .pagination { text-align:center; margin:20px; }
    .pagination a { margin:0 4px; padding:6px 12px; border:1px solid #ccc; text-decoration:none; color:#333; }
    .pagination a.active { background:#2d6cdf; color:#fff; }
</style>
</head>
<body>

<h2 style="text-align:center;">Tất cả sản phẩm</h2>

<div class="grid">
<c:forEach items="${listproduct}" var="p">
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

<div class="pagination">
<c:forEach begin="0" end="${totalPages-1}" var="i">
    <a href="<c:url value='/product?page=${i}'/>" class="${i==currentPage ? 'active' : ''}">${i+1}</a>
</c:forEach>
</div>

</body>
</html>