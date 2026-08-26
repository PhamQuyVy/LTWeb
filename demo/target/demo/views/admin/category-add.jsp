<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Thêm Category</title>
</head>
<body>

<h2>Thêm danh mục</h2>

<form action="<c:url value="/admin/category/insert"/>" method="post" enctype="multipart/form-data">

<label for="fname">Category name:</label><br>
<input type="text" id="categoryname" name="categoryname"><br>

<label for="lname">Link images:</label><br>
<input type="text" id="images" name="images"><br>

<label for="lname">Upload images:</label><br>
<input type="file" id="images1" name="images1"><br>

<label for="html">Status</label><br>
<input type="radio" id="ston" name="status" value="1" checked>
<label for="css">Hoạt động</label><br>
<input type="radio" id="stoff" name="status" value="0">
<label for="javascript">Khóa</label>

<br><br>
<input type="submit" value="Submit">

</form>

</body>
</html>
