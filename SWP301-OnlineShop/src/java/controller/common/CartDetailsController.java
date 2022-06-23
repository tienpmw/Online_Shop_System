/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.common;

import static configs.Security.SIZE_PAGE_CART_LIST;
import dal.CartDBContext;
import dal.ProductCategoryDBContext;
import dal.ProductDBContext;
import dal.ProductListDBContext;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Cart;
import model.Category;
import model.Product;
import model.User;

/**
 *
 * @author Admin
 */
public class CartDetailsController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.getRequestDispatcher("view/public/cartDetails.jsp").forward(request, response);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //Declare and initialize the initial value
        ProductCategoryDBContext productCategoryDBContext = new ProductCategoryDBContext();
        ProductListDBContext productListDBContext = new ProductListDBContext();
        CartDBContext cartDBContext = new CartDBContext();
        String search = "";
        int index = 1;
        HttpSession session = request.getSession();

        //Get user login from session
        User user = (User) session.getAttribute("user");
        int userID = user.getId();

        //Get data from input search
        if (request.getParameter("txtSearch") != null) {
            search = request.getParameter("txtSearch").trim();
        }

        //Get current page from view
        if (request.getParameter("index") != null) {
            index = Integer.parseInt(request.getParameter("index"));
        }

        //Get list subcategory
        ArrayList<Category> listCategorys = productCategoryDBContext.getAllCategory();

        //get least post
        ArrayList<Product> leastProduct = productListDBContext.getListLeastProduct();

        //Get List Cart 
        Cart cart = cartDBContext.getCartByIndexAndUserId(0, 0, search, userID);

        System.out.println(cart.getId());
        //Calculator Last page
        int sizeOfList = cart.getCart_Products().size();
        int lastPage = sizeOfList / SIZE_PAGE_CART_LIST;
        if (sizeOfList % SIZE_PAGE_CART_LIST != 0) {
            lastPage++;
        }

        //Set List Cart by current index
        cart = cartDBContext.getCartByIndexAndUserId(index, SIZE_PAGE_CART_LIST, search, userID);

        System.out.println(cart.getId());
        //Send result after search
        request.setAttribute("search", search);
        //Send Last page of cart list
        request.setAttribute("lastPage", lastPage);
        //Send cart List
        request.setAttribute("carts", cart.getCart_Products());
        //Send index 
        request.setAttribute("index", index);

        request.setAttribute("cartId", cart.getId());

        request.setAttribute("listCategorys", listCategorys);

        request.setAttribute("leastProduct", leastProduct);
        
        

        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        CartDBContext cartDb = new CartDBContext();
        PrintWriter out = response.getWriter();
        int pid = Integer.parseInt(request.getParameter("pid"));
        int isUp = Integer.parseInt(request.getParameter("isUp"));
        int cid = Integer.parseInt(request.getParameter("cartId"));
        int currentQuantity = cartDb.getCartProductByCidAndPid(cid, pid).getQuantity();
        if (isUp == 1) {
            currentQuantity += 1;
        } else {
            currentQuantity -= 1;
        }

        cartDb.setQuantityCartProduct(pid, cid, currentQuantity);
        out.println("<input disabled=\"\" class=\"cart_quantity_input\" type=\"text\" value=\"" + currentQuantity + "\" autocomplete=\"off\" size=\"2\">");
        
      

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
