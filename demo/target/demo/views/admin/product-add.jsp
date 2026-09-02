<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Thêm Product</title>
</head>
<body>

<h2>Thêm sản phẩm</h2>

<form action="<c:url value="/admin/product/insert"/>" method="post" enctype="multipart/form-data">

<label>Tên sản phẩm:</label><br>
<input type="text" name="productname"><br>

<label>Giá:</label><br>
<input type="number" step="0.01" name="price"><br>

<label>Mô tả:</label><br>
<textarea name="description" rows="4" cols="40"></textarea><br>

<label>Danh mục:</label><br>
<select name="cateid">
    <c:forEach items="${listcate}" var="c">
        <option value="${c.categoryid}">${c.categoryname}</option>
    </c:forEach>
</select><br>

<label>Link images:</label><br>
<input type="text" name="images"><br>

<label>Upload images:</label><br>
<input type="file" name="images1"><br>

<label>Status</label><br>
<input type="radio" name="status" value="1" checked> Hoạt động<br>
<input type="radio" name="status" value="0"> Khóa

<br><br>
<input type="submit" value="Submit">

</form>

</body>
</html>