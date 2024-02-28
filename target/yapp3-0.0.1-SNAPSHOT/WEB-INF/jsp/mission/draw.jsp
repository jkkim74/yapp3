<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Lucky Draw</title>
</head>
<body>
<h1>Lucky Draw</h1>
<form action="/yapp3/draw" method="get">
  <input type="submit" value="뽑기">
</form>

<input type="button" name="draw" id="draw" value="뽑기"/>

<h2>Congratulations! You've got:</h2>
<h3>${item.name}</h3>
</body>
</html>

