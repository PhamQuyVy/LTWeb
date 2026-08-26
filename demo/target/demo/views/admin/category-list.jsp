<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Category List</title>
</head>
<body>

<a href="<c:url value="/admin/category/add"/>">Add Category</a><br>
<hr>
<table border="1" width="100%">
<tr>
<th>STT</th>
<th>Images</th>
<th>Category name</th>
<th>Status</th>
<th>Action</th>
</tr>
<c:forEach items="${listcate}" var="cate" varStatus="STT">
<tr>
<td>${STT.index+1 }</td>

<c:if test="${cate.images.length() >= 5 && cate.images.substring(0,5)=='https'}">
    <c:url value="${cate.images }" var="imgUrl"></c:url>
</c:if>
<c:if test="${cate.images.length() < 5 || cate.images.substring(0,5)!='https'}">
    <c:url value="/image?fname=${cate.images }" var="imgUrl"></c:url>
</c:if>

<td><img height="150" width="200" src="${imgUrl}" /></td>
<td>${cate.categoryname }</td>
<td>
    <c:if test="${cate.status==1 }">
        Hoạt động
    </c:if>
    <c:if test="${cate.status!=1 }">
        Khóa
    </c:if>
</td>
<td><a href="<c:url value='/admin/category/edit?id=${cate.categoryid }'/>">Sửa</a>
| <a href="<c:url value='/admin/category/delete?id=${cate.categoryid }'/>">Xóa</a>
</td>
</tr>
</c:forEach>
</table>

</body>
</html>
