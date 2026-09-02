<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Product List</title>
</head>
<body>

<a href="<c:url value='/'/>">&larr; Trang chủ</a><br><br>
<a href="<c:url value="/admin/product/add"/>">Add Product</a><br>
<hr>
<table border="1" width="100%">
<tr>
<th>STT</th><th>Images</th><th>Name</th><th>Price</th><th>Category</th><th>Status</th><th>Action</th>
</tr>
<c:forEach items="${listproduct}" var="p" varStatus="STT">
<tr>
<td>${STT.index+1}</td>

<c:if test="${p.images.length() >= 5 && p.images.substring(0,5)=='https'}">
    <c:url value="${p.images}" var="imgUrl"></c:url>
</c:if>
<c:if test="${p.images.length() < 5 || p.images.substring(0,5)!='https'}">
    <c:url value="/image?fname=${p.images}" var="imgUrl"></c:url>
</c:if>

<td><img height="100" width="130" src="${imgUrl}" /></td>
<td>${p.productName}</td>
<td><fmt:formatNumber value="${p.price}" type="number"/></td>
<td>${p.category.categoryname}</td>
<td>
    <c:if test="${p.status==1}">Hoạt động</c:if>
    <c:if test="${p.status!=1}">Khóa</c:if>
</td>
<td><a href="<c:url value='/admin/product/edit?id=${p.productId}'/>">Sửa</a>
| <a href="<c:url value='/admin/product/delete?id=${p.productId}'/>">Xóa</a>
</td>
</tr>
</c:forEach>
</table>

</body>
</html>