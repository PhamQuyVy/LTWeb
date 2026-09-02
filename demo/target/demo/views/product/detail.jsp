<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${product.productName}</title>
<style>
    body { font-family: Arial, sans-serif; }
    .detail { max-width:800px; margin:30px auto; display:flex; gap:24px; }
    .detail img { width:320px; height:320px; object-fit:cover; border-radius:8px; }
</style>
</head>
<body>

<c:if test="${product.images.length() >= 5 && product.images.substring(0,5)=='https'}">
    <c:url value="${product.images}" var="imgUrl"></c:url>
</c:if>
<c:if test="${product.images.length() < 5 || product.images.substring(0,5)!='https'}">
    <c:url value="/image?fname=${product.images}" var="imgUrl"></c:url>
</c:if>

<div class="detail">
    <img src="${imgUrl}">
    <div>
        <h2>${product.productName}</h2>
        <p><b>Giá:</b> ${product.price}</p>
        <p><b>Danh mục:</b> ${product.category.categoryname}</p>
        <p><b>Mô tả:</b><br>${product.description}</p>
        <a href="<c:url value='/product'/>">&larr; Quay lại danh sách</a>
    </div>
</div>

</body>
</html>