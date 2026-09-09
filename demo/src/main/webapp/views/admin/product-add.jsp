<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Thêm Product</title>
<style>.error{color:#c0392b;margin:10px 0;}</style>
</head>
<body>

<h2>Thêm sản phẩm</h2>

<c:if test="${not empty error}">
    <div class="error">${error}</div>
</c:if>

<form action="<c:url value="/admin/product/insert"/>" method="post" enctype="multipart/form-data" novalidate>

<label>Tên sản phẩm:</label><br>
<input type="text" name="productname" value="${param.productname}" required maxlength="150"><br>

<label>Giá:</label><br>
<input type="number" step="0.01" min="0.01" name="price" value="${param.price}" required><br>

<label>Mô tả:</label><br>
<textarea name="description" rows="4" cols="40">${param.description}</textarea><br>

<label>Danh mục:</label><br>
<select name="cateid" required>
    <option value="">-- Chọn danh mục --</option>
    <c:forEach items="${listcate}" var="c">
        <option value="${c.categoryid}" ${param.cateid == c.categoryid ? 'selected' : ''}>${c.categoryname}</option>
    </c:forEach>
</select><br>

<label>Link images:</label><br>
<input type="text" name="images" value="${param.images}"><br>

<label>Upload images:</label><br>
<input type="file" name="images1" accept="image/*"><br>

<label>Status</label><br>
<input type="radio" name="status" value="1" checked> Hoạt động<br>
<input type="radio" name="status" value="0"> Khóa

<br><br>
<input type="submit" value="Submit">

</form>

</body>
</html>