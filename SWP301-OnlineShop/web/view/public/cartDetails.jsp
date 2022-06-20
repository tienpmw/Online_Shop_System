<!DOCTYPE html>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib  prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <title>Cart Details</title>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="../../assets/public/css/bootstrap.min.css" rel="stylesheet">
        <link rel="shortcut icon" type="image/x-icon" href="assets/img/lg.png" />
        <link href="../../assets/public/css/font-awesome.min.css" rel="stylesheet">
        <link href="../../assets/public/css/prettyPhoto.css" rel="stylesheet">
        <link href="../../assets/public/css/price-range.css" rel="stylesheet">
        <link href="../../assets/public/css/animate.css" rel="stylesheet">
        <link href="../../assets/public/css/main.css" rel="stylesheet">
        <link href="../../assets/public/css/responsive.css" rel="stylesheet">
        <link href="../../assets/public/css/style.css" rel="stylesheet">


    </head>
    <!--/head-->

    <body>
        <jsp:include page="../home-template/headerProductlist.jsp"/>
        <section id="cart_items">
            <div class="container">
                <div class="row flex-justify">
                    <div class="col-sm-3 box-shadow height-fit-content border-radius-2" >
                        <div class="left-side"> <!-- left-sidebar -->
                            <h2 class="title text-center " style="border-bottom: solid 2px; margin-top: 10px;">Category</h2>
                            <form action="productlist" method="get">
                                <div class="panel-group category-products" id="accordian"><!--category-products-->
                                    <div class="panel panel-default">
                                        <div class="panel-heading">

                                            <div class="search_box">
                                                <!--<input id="search-box" type="text" placeholder="Search..." name="searchBy" value="${requestScope.searchBy}">-->
                                                <input type="text" name="searchBy" value="${requestScope.searchBy}"  placeholder="Search"/>
                                            </div>
                                        </div>
                                        <div class="panel-heading">
                                            <h4 class="panel-title">
                                                <a href="productlist">
                                                    <span class="badge pull-right"></span>
                                                    All Category
                                                </a>
                                            </h4>
                                        </div>
                                    </div>
                                    <c:forEach items="${requestScope.listCategorys}" var="list">
                                        <c:if test="${ not empty list.listSubCategory }"> <!-- check empty of list subcategory with that category -->
                                            <div class="panel panel-default">
                                                <div class="panel-heading">
                                                    <h4 class="panel-title">

                                                        <a data-toggle="collapse" data-parent="#accordian" href="#${list.id}">
                                                            <span class="badge pull-right"><i class="fa fa-plus"></i></span>
                                                                ${list.name}
                                                        </a>
                                                    </h4>
                                                </div>
                                                <div id="${list.id}" class="panel-collapse collapse">
                                                    <div class="panel-body">
                                                        <ul>
                                                            <c:forEach items="${list.listSubCategory}" var="listSub">
                                                                <li><a href="productlist?subCategory=${listSub.id}&searchBy=${searchBy}">${listSub.name} </a></li> 
                                                                </c:forEach>
                                                        </ul>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:if>
                                    </c:forEach>
                                </div><!--/category-products-->    
                            </form>


                            <div class="panel-group category-products" id="accordian"><!-- 2 least product -->
                                <h2 class="title text-center" style="border-bottom: solid 2px;">Latest Product</h2>
                                <%--<c:set var="leat" value="" />--%>
                                <c:if test="${requestScope.leastProduct != null}">
                                    <c:forEach items="${requestScope.leastProduct}" var="leastProduct">
                                        <div class="product-image-wrapper">
                                            <div class="single-products">
                                                <div class="productinfo text-center">
                                                    <a href="productdetails?productID=${leastProduct.id}">
                                                        <img src="${leastProduct.thumbnail}" alt="" />
                                                    </a>
                                                    <h2 class="break-down-line">${leastProduct.name}</h2>
                                                    <p class="break-down-line">${leastProduct.description}</p>
                                                    <p>
                                                        <span class="text-line-through">
                                                            <fmt:formatNumber  maxFractionDigits = "3" type = "currency" value = "${leastProduct.price}"/>
                                                        </span>
                                                        <span class="text-danger">
                                                            <fmt:formatNumber  maxFractionDigits = "3" type = "currency" value = "${leastProduct.priceDiscount}"/>
                                                        </span>
                                                    </p>
                                                    <a href="productdetails?productID=${leastProduct.id}" class="btn btn-default add-to-cart">More Information</a>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </c:if>
                            </div><!-- end two least product --> 

                        </div>                     
                    </div>
                    <!--PRODUCT LIST-->


                    <div class="col-sm-9 padding-right">
                        <div class="features_items">
                            <!--features_items-->
                            <h2 class="title text-center" style="border-bottom: solid 2px; margin-top: 10px">Cart</h2>
                            <!--Show product-->
                            <div class="table-responsive cart_info">
                                <table class="table table-condensed">
                                    <thead>
                                        <tr class="cart_menu">
                                            <td><input type="checkbox" name="all" id="checkall"></td>
                                            <td class="image">Item</td>
                                            <td class="description"></td>
                                            <td class="price">Price</td>
                                            <td class="quantity">Quantity</td>
                                            <td>Total</td>
                                        </tr>
                                    </thead>
                                    <tbody>

                                        <c:forEach var="i" items="${carts}">
                                            <tr>

                                                <td class="cart_description"><div id="calculator"><input type="checkbox" value="${i.quantity * (i.product).getPriceDiscount()}" on class="cb-element">
                                                        <input type="checkbox" value="${(i.product).id}" class="cb-element" style="opacity:0; position:absolute; left:9999px;"></div></td>
                                        <input type="hidden"  value=""/>

                                        <td class="cart_product" style="width: 150px">
                                            <a href=""><img src="${(i.product).thumbnail}" alt="" width="100px" height="auto"></a>
                                        </td>
                                        <td class="cart_description">
                                            <h4><a href="">${(i.product).name}</a></h4>
                                            <p>${(i.product).description}</p>
                                        </td>
                                        <td class="cart_price">

                                            <span class="text-line-through">
                                                <fmt:formatNumber type = "number" value = "${(i.product).price}"/>
                                            </span>
                                            <span class="text-danger">
                                                <fmt:formatNumber type = "number" value = "${(i.product).getPriceDiscount()}"/>
                                            </span>
                                        </td>
                                        <td class="cart_quantity">
                                            <div class="cart_quantity_button">
                                                <a class="cart_quantity_up" href=""> + </a>
                                                <input class="cart_quantity_input" type="text" name="quantity" value="${i.quantity}" autocomplete="off" size="2">
                                                <a class="cart_quantity_down" href=""> - </a>
                                            </div>
                                        </td>
                                        <td class="cart_total">
                                            <p class="cart_total_price"><fmt:formatNumber type="number" value="${i.quantity * (i.product).getPriceDiscount()}"/></p>

                                        </td>
                                        <td class="cart_delete">
                                            <a class="cart_quantity_delete" href="#"><i class="fa fa-times"></i></a>
                                        </td>
                                        </tr>
                                    </c:forEach>

                                    </tbody>
                                </table>
                            </div>
                            <section id="do_action">
                                <div class="container">

                                    <div class="row">
                                        <div class="col-sm-9">
                                            <div class="total_area">
                                                <ul>
                                                    <li>Cart Sub Total <span>$59</span></li>

                                                    <li style="display: flex"><div >Total</div> <div class=total style="margin-left: auto"><span id="total">0</span></div></li>
                                                </ul>

                                                <a class="btn btn-default check_out" href="">Check Out</a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </section>
                            <h1><b>OPPS!</b> There are no Products in the Cart!</h1>

                        </div>
                    </div>


                    <!--END PRODCUT LIST-->
                </div>
                <div class="pagging">
                    <ul class="pagination pull-right">
                        <c:if test="${requestScope.totalpage > 1}">
                            <li><a href="productlist?page=1&searchBy=${searchBy}&subCategory=${listSub.id}">Frist</a></li>
                            </c:if>
                            <c:forEach begin="1" end="${requestScope.totalpage}" var="page">
                            <li class="${pageindex == page ? "active =" : ""}" ><a href="productlist?page=${page}&searchBy=${searchBy}&subCategory=${listSub.id}">${page}</a></li>    
                            </c:forEach>
                            <c:if test="${requestScope.totalpage > 1}">
                            <li><a href="productlist?page=${requestScope.totalpage}&searchBy=${searchBy}&subCategory=${listSub.id}">Last</a></li>
                            </c:if>
                    </ul>
                </div>

            </div>
        </section> <!--/#cart_items-->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>


        <jsp:include page="../home-template/footer.jsp"/>



        <script src="../../assets/js/cart/script.js"></script>
        <script src="../../assets/public/js/jquery.js"></script>
        <script src="../../assets/public/js/bootstrap.min.js"></script>
        <script src="../../assets/public/js/jquery.scrollUp.min.js"></script>
        <script src="../../assets/public/js/price-range.js"></script>
        <script src="../../assets/public/js/jquery.prettyPhoto.js"></script>
        <script src="../../assets/public/js/main.js"></script>
        <script src="../../assets/js/home/home.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.19.4/jquery.validate.min.js" integrity="sha512-FOhq9HThdn7ltbK8abmGn60A/EMtEzIzv1rvuh+DqzJtSGq8BRdEN0U+j0iKEIffiw/yEtVuladk6rsG4X6Uqg==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
    </body>

</html>