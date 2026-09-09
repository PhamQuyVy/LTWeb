<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Sửa Product</title>
<style>.error{color:#c0392b;margin:10px 0;}</style>
</head>
<body>

<h2>Sửa sản phẩm</h2>

<c:if test="${not empty error}">
    <div class="error">${error}</div>
</c:if>

<form action="<c:url value="/admin/product/update"/>" method="post" enctype="multipart/form-data" novalidate>
<input type="hidden" name="productid" value="${product.productId}">

<label>Tên sản phẩm:</label><br>
<input type="text" name="productname" value="${not empty param.productname ? param.productname : product.productName}" required maxlength="150"><br>

<label>Giá:</label><br>
<input type="number" step="0.01" min="0.01" name="price" value="${not empty param.price ? param.price : product.price}" required><br>

<label>Mô tả:</label><br>
<textarea name="description" rows="4" cols="40">${not empty param.description ? param.description : product.description}</textarea><br>

<label>Danh mục:</label><br>
<select name="cateid" required>
    <c:forEach items="${listcate}" var="c">
        <option value="${c.categoryid}" ${c.categoryid == product.category.categoryid ? 'selected' : ''}>${c.categoryname}</option>
    </c:forEach>
</select><br>

<label>Link images:</label><br>
<input type="text" name="images" value="${param.images}"><br>

<label>Upload images mới (nếu muốn đổi):</label><br>
<input type="file" name="images1" accept="image/*"><br>

<label>Status</label><br>
<input type="radio" name="status" value="1" ${product.status==1 ? 'checked' : ''}> Hoạt động<br>
<input type="radio" name="status" value="0" ${product.status!=1 ? 'checked' : ''}> Khóa

<br><br>
<input type="submit" value="Submit">

</form>

</body>
</html>