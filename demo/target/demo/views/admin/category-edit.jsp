<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Sửa Category</title>
<style>.error{color:#c0392b;margin:10px 0;}</style>
</head>
<body>

<h2>Sửa danh mục</h2>

<c:if test="${not empty error}">
    <div class="error">${error}</div>
</c:if>

<form action="<c:url value="/admin/category/update"/>" method="post" enctype="multipart/form-data" novalidate>

<input type="hidden" name="categoryid" value="${cate.categoryid}">

<label for="categoryname">Category name:</label><br>
<input type="text" id="categoryname" name="categoryname"
       value="${not empty param.categoryname ? param.categoryname : cate.categoryname}" required maxlength="100"><br>

<label for="images">Link images:</label><br>
<input type="text" id="images" name="images" value="${param.images}"><br>

<c:if test="${cate.images.length() >= 5 && cate.images.substring(0,5)=='https'}">
    <c:url value="${cate.images}" var="imgUrl"></c:url>
</c:if>
<c:if test="${cate.images.length() < 5 || cate.images.substring(0,5)!='https'}">
    <c:url value="/image?fname=${cate.images}" var="imgUrl"></c:url>
</c:if>

<img height="150" width="200" src="${imgUrl}" /><br>

<label for="images1">Upload images:</label><br>
<input type="file" id="images1" name="images1" accept="image/*"><br>

<label>Status</label><br>
<input type="radio" id="ston" name="status" value="1" ${cate.status==1?'checked':''}>
<label for="ston">Hoạt động</label><br>
<input type="radio" id="stoff" name="status" value="0" ${cate.status!=1?'checked':''}>
<label for="stoff">Khóa</label>

<br><br>
<input type="submit" value="Update">

</form>

</body>
</html>