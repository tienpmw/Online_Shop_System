/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.common;

import static configs.Security.SIZE_PAGE_CART_LIST;
import dal.CartDBContext;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Cart;
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

        //Get List Cart 
        ArrayList<Cart> lstCart = cartDBContext.getCartByIndexAndUserId(0, 0, search, userID);
        
        //Calculator Last page
        int sizeOfList = lstCart.size();
        int lastPage = sizeOfList / SIZE_PAGE_CART_LIST;
        if (sizeOfList % SIZE_PAGE_CART_LIST != 0) {
            lastPage++;
        }
        
        //Set List Cart by current index
        lstCart = cartDBContext.getCartByIndexAndUserId(index, SIZE_PAGE_CART_LIST, search, userID);

        //Send result after search
        request.setAttribute("search", search);
        //Send Last page of cart list
        request.setAttribute("lastPage", lastPage);
        //Send cart List
        request.setAttribute("carts", lstCart);
        //Send index 
        request.setAttribute("index", index);
        
        
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
        processRequest(request, response);
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
