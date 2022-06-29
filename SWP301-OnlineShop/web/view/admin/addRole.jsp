<%-- 
    Document   : addNewUser
    Created on : May 26, 2022, 8:50:53 AM
    Author     : Admin
--%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>  
        <meta charset="UTF-8">
        <title>Admin | Add new role</title>
        <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>
        <meta name="description" content="Developed By M Abdur Rokib Promy">
        <meta name="keywords" content="Admin, Bootstrap 3, Template, Theme, Responsive">
        <!-- bootstrap 3.0.2 -->
        <link href="../../assets/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
        <!-- font Awesome -->
        <link href="../../assets/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
        <!-- Theme style -->
        <link href="../../assets/css/style.css" rel="stylesheet" type="text/css" />
        <script src="https://cdn.jsdelivr.net/npm/jquery@3.6.0/dist/jquery.slim.min.js"></script>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css" integrity="sha512-KfkfwYDsLkIlwQp6LFnl8zNdLGxu9YAA1QvwINks4PhcElQSvqcyVLLD9aMhXd13uQjoXtEKNosOWaZqXgel0g==" crossorigin="anonymous" referrerpolicy="no-referrer" />
        <!--css-->
        <link href="../../assets/css/admin/addNewUser.css" rel="stylesheet" type="text/css"/>
    </head>
    <body class="skin-black">
        <!-- header logo: style can be found in header.less -->
        <jsp:include page="../admin-layout/header.jsp"></jsp:include>
            <div class="wrapper row-offcanvas row-offcanvas-left">
                <!-- Left side column. contains the logo and sidebar -->
            <jsp:include page="../admin-layout/sideBar.jsp"></jsp:include>
                <!-- Right side. contains main content -->
                <aside class="right-side">
                    <!-- Main content -->
                    <section class="content">
                        <div id="content">
                            <h2 class="text-center" style="font-weight: 700; text-align: center">Add new role</h2>
                        <c:if test="${requestScope.message != null && requestScope.error == false}">
                            <h4 class="text-center" style="font-weight: 700; text-align: left; color: #0000ff">${requestScope.message}</h4>
                        </c:if>
                        <c:if test="${requestScope.message != null && requestScope.error == true}">
                            <h4 class="text-center" style="font-weight: 700; text-align: left; color: #ff0000">${requestScope.message}</h4>
                        </c:if>
                        <form id="basic-form" action="addRole" method="post" class="d-flex justify-content-center" >
                            <p>
                                <label for="roleName">Enter role name<span class="text-danger">*</span></label>
                                <input type="text" id="roleName" name="roleName" required>
                            </p>
                            <p style="font-size: 20px; font-weight: 700; text-align: center">List of features</p>
                            <p>
                                <label for="adminFeatutes">Admin</label><br>
                                <c:forEach items="${requestScope.adminFeatures}" var="a">
                                    <input type="checkbox" id="roleID" name="roleID" value="${a.id}">
                                    <label for="role">${a.name}</label><br>
                                </c:forEach>
                            </p>
                            <p>
                                <label for="marketingFeatutes">Marketing</label><br>
                                <c:forEach items="${requestScope.marketingFeatures}" var="a">
                                    <input type="checkbox" id="roleID" name="roleID" value="${a.id}">
                                    <label for="role">${a.name}</label><br>
                                </c:forEach>
                            </p>
                            <p>
                                <label for="salesFeatures">Sale</label><br>
                                <c:forEach items="${requestScope.SalesFeatures}" var="a">
                                    <input type="checkbox" id="roleID" name="roleID" value="${a.id}">
                                    <label for="role">${a.name}</label><br>
                                </c:forEach>
                            </p>
                            <input class="submit" type="submit" value="ADD" class="">
                        </form>
                    </div>
                </section>
            </aside>
        </div>

        <!--javascrip-->
        <script src="../../assets/js/admin/addNewUser.js"></script>
        <script src="../../assets/js/admin/addNewUser.js"></script>
        <script src="../../assets/js/admin/main.js" type="text/javascript"></script>
        <!-- jQuery 2.0.2 -->
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/2.0.2/jquery.min.js"></script>
        <script src="../../js/jquery.min.js" type="text/javascript"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.19.0/jquery.validate.min.js"></script>
        <!-- jQuery UI 1.10.3 -->
        <script src="../../assets/js/jquery-ui-1.10.3.min.js" type="text/javascript"></script>
        <!-- Bootstrap -->
        <script src="../../assets/js/bootstrap.min.js" type="text/javascript"></script>
        <!-- Director App -->
        <script src="../../assets/js/Director/app.js" type="text/javascript"></script>
    </body>
</html>
