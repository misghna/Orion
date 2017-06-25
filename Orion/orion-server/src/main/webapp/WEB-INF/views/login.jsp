<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Login page</title>
		<!--  <link href="<c:url value='/static/css/bootstrap.css' />"  rel="stylesheet"></link> -->
		<link href="<c:url value='/static/login.css' />" rel="stylesheet"></link>
		
		<style>
			#backgra{
				position: fixed;
			    top: 0;
			    left: 0;
			    width: 100%;
			    height: 100%;
			    background-image: url('/static/frontPage.jpg');
			    background-repeat: no-repeat;
			    background-attachment: fixed;
			    background-size: 100%;
			    opacity: 0.1;
			    filter:alpha(opacity=20);
			}
		</style>
	</head>

	<body>
	<!-- 	<div id="backgra"></div> -->
			<c:url var="loginUrl" value="/login" />
			<form id="login" action="${loginUrl}" method="post" class="form-horizontal">
			    <h1>Log In</h1>
			    <fieldset id="inputs">
			        <input id="email" id="username" name="username" type="text" placeholder="Email" autofocus required>   
			        <input id="password" id="password" name="password" type="password" placeholder="Password" required>
			    </fieldset>
			    <span class="text-danger">
				    <c:if test="${param.error != null}">
						<div class="alert alert-danger">
							  <p>Invalid username and password.</p> 
<%-- 							<%     
								String errorString = (String)request.getAttribute("error");  
								out.println("<p>" + errorString + "</p>");  
							%> --%>
						</div>
					</c:if>
					<c:if test="${param.logout != null}">
						<div class="alert alert-success">
							<p>You have been logged out successfully.</p>
						</div>
					</c:if>
			    </span>
			    <fieldset id="actions">
			        <input type="submit" id="submit" value="Log in">
			        <a href="/open/passrenew">Forgot your password?</a>
			        <a href="/open/register">Register</a>
			    </fieldset>													
			</form>
	</body>
</html>

<div id="footerImg">
    <img src="images/footer.jpg"/>
</div>

