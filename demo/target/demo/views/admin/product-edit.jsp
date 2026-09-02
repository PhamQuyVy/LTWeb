<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Sửa Product</title>
</head>
<body>

<h2>Sửa sản phẩm</h2>

<form action="<c:url value="/admin/product/update"/>" method="post" enctype="multipart/form-data">
<input type="hidden" name="productid" value="${product.productId}">

<label>Tên sản phẩm:</label><br>
<input type="text" name="productname" value="${product.productName}"><br>

<label>Giá:</label><br>
<input type="number" step="0.01" name="price" value="${product.price}"><br>

<label>Mô tả:</label><br>
<textarea name="description" rows="4" cols="40">${product.description}</textarea><br>

<label>Danh mục:</label><br>
<select name="cateid">
    <c:forEach items="${listcate}" var="c">
        <option value="${c.categoryid}" ${c.categoryid == product.category.categoryid ? 'selected' : ''}>${c.categoryname}</option>
    </c:forEach>
</select><br>

<label>Link images:</label><br>
<input type="text" name="images"><br>

<label>Upload images mới (nếu muốn đổi):</label><br>
<input type="file" name="images1"><br>

<label>Status</label><br>
<input type="radio" name="status" value="1" ${product.status==1 ? 'checked' : ''}> Hoạt động<br>
<input type="radio" name="status" value="0" ${product.status!=1 ? 'checked' : ''}> Khóa

<br><br>
<input type="submit" value="Submit">

</form>

</body>
</html>