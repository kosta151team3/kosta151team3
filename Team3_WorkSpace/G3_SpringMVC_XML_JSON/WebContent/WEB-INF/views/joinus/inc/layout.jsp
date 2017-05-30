<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><!-- title 영역 --> <tiles:getAsString name="title" />
</title>
<!-- <link href="join.css" type="text/css" rel="stylesheet" /> -->
<!-- join.css 가져오기 -->
<link href='<tiles:getAsString name = "css"/>' type="text/css"
	rel="stylesheet" />
</head>
<body>
	<!-- Header 영역 -->
	<tiles:insertAttribute name="header" />
	<!-- Visual 영역 -->
	<tiles:insertAttribute name="visual" />
	<div id="main">
		<div class="top-wrapper clear">
			<!-- Content (실제 내용) -->
			<!-- Aside (Navi) 영역 -->
			<tiles:insertAttribute name="content" />
			<tiles:insertAttribute name="aside" />
		</div>
	</div>
	<!-- Footer 영역 -->
	<tiles:insertAttribute name="footer" />
</body>
</html>