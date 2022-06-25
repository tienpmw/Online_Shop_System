/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.home;

import dal.CartDBContext;
import dal.OrderDBContext;
import dal.ProductCategoryDBContext;
import dal.ProductDBContext;
import dal.ProductListDBContext;
import dal.UserDBContext;
import filter.BaseAuthController;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Category;
import model.Product;
import model.User;

/**
 *
 * @author DELL
 */
@WebServlet(name = "CartCompletionController", urlPatterns = {"/cartCompletion"})
public class CartCompletionController extends BaseAuthController {

    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ProductCategoryDBContext productCategoryDBContext = new ProductCategoryDBContext();
        ProductListDBContext productDB = new ProductListDBContext();

        //get Parameter value
        String[] listIdProductCart_raw = request.getParameterValues("id");
        String raw_subCategory = request.getParameter("subCategory");

        //get list subcategory
        ArrayList<Category> listCategorys = productCategoryDBContext.getAllCategory();

        //check subCategory
        if (raw_subCategory == null || raw_subCategory.length() == 0 || raw_subCategory.equals("-1")) {
            raw_subCategory = "0";
        }
        int subCategory = Integer.parseInt(raw_subCategory);
        int[] listIdProduct = new int[listIdProductCart_raw.length];
        for (int i = 0; i < listIdProductCart_raw.length; i++) {
            listIdProduct[i] = Integer.parseInt(listIdProductCart_raw[i].trim());
        }
        ArrayList<Product> leastProduct = productDB.getListLeastProduct();
        User user = (User) request.getSession().getAttribute("user");
        ArrayList<Product> listProduct = productDB.getListProductById(listIdProduct, user.getId());
        long total = totalPrice(listProduct);
        request.setAttribute("listCategorys", listCategorys);
        request.setAttribute("subCategory", subCategory);
        request.setAttribute("listProduct", listProduct);
        request.setAttribute("leastProduct", leastProduct);
        request.setAttribute("total", total);
        request.getRequestDispatcher("view/public/CartCompletion.jsp").forward(request, response);
    }

    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");

            // get info bank
            String nameBank = getServletContext().getInitParameter("NameOfBank");
            String ownerAccount = getServletContext().getInitParameter("OwnerAccount");
            String accNumber = getServletContext().getInitParameter("AccountNumber");
            System.out.println(nameBank + " " + ownerAccount + " " + accNumber);

            // get ship info
            String shipFullName = request.getParameter("txtFullname").trim();
            String shipPhone = request.getParameter("txtPhone").trim();
            String shipAddress = request.getParameter("txtAddress").trim();
            String shipNote = request.getParameter("txtNote").trim();
//            String shipEmail = request.getParameter("txtEmail").trim();
            User user = (User) request.getSession().getAttribute("user");

            // get method payment
            String payment = request.getParameter("payment").trim();

            /*
            * isPayment:
            * 0: Payment on delivery
            * 1: Payment by bank
             */
            int idPayment = (payment.equalsIgnoreCase("delivery")) ? 0 : 1;
            // get product
            String[] idProducts_raw = request.getParameterValues("pr-id");
            String[] priceProducts_raw = request.getParameterValues("pr-price");
            String[] discountProducts_raw = request.getParameterValues("pr-discount");
            String[] quantityProducts_raw = request.getParameterValues("pr-quantity");

            Product[] productsOrder = new Product[idProducts_raw.length];
            for (int i = 0; i < productsOrder.length; i++) {
                Product pro = new Product();
                pro.setId(Integer.parseInt(idProducts_raw[i].trim()));
                pro.setPrice(Long.parseLong(priceProducts_raw[i].trim()));
                pro.setDiscount(Integer.parseInt(discountProducts_raw[i].trim()));
                pro.setQuantity(Integer.parseInt(quantityProducts_raw[i].trim()));

                productsOrder[i] = pro;
            }
            // total money
            long total = Long.parseLong(request.getParameter("total"));
            
            // get last seller receive order
            
            ProductDBContext productDB = new ProductDBContext();
            OrderDBContext orderDB = new OrderDBContext();
            CartDBContext cartDB = new CartDBContext();
            UserDBContext userDB = new UserDBContext();
            
            int numberSeller = userDB.countNumberSeller();
            // get id of last seller receive order and index
            int lastSellerReceiveOrder = userDB.getLastSellerReceiveOrder();
            int indexSellerReceiveOrder = userDB.getindexSellerReceiveOrder(lastSellerReceiveOrder);
            
            // get next seller receive order
            int indexNextSellerReceiveOrder = 0;
            if(numberSeller == indexSellerReceiveOrder) {
                indexNextSellerReceiveOrder = 1;
            } else {
                indexNextSellerReceiveOrder = indexSellerReceiveOrder + 1;
            }
            int idNextSeller = userDB.getIdNextSeller(indexNextSellerReceiveOrder);
            
            int idCart = cartDB.getIdCartOfCustomer(user.getId());
            int idOrder = orderDB.addOrder(productsOrder, total, user.getId(), user.getEmail(), shipFullName, shipAddress, shipPhone, shipNote, idPayment, idNextSeller);
            if (idOrder > 0) {
                productDB.updateQuantityProductAvailable(productsOrder);
                cartDB.deleteProductOrdered(productsOrder, idCart);
                // send mail
                
                System.out.println("Success");
            } else {
                System.out.println("error");
            }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private long totalPrice(ArrayList<Product> listProduct) {
        long sum = 0;
        for (Product product : listProduct) {
            sum += product.getPrice() * product.getQuantity();
        }
        return sum;
    }

}
