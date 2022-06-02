/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.marketing;

import dal.CustomerDBContext;
import filter.BaseAuthController;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.User;

/**
 *
 * @author Hoang Quang
 */
public class CustomerListControler extends BaseAuthController {

    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        int userRole = 4;
        CustomerDBContext customerDBContext = new CustomerDBContext();
        //check search and sort by
        String searchBy = request.getParameter("searchBy");
        String statusBy = request.getParameter("statusBy");
        String sortBy = request.getParameter("sortBy");
        //ceheck if user dont choice to search anything
        if (searchBy == null || searchBy.length() == 0) {
            searchBy = "";
        }
        request.setAttribute("searchBy", searchBy);
        //status user choice
        if (statusBy == null || statusBy.length() == 0 || statusBy.equals("-1")) {
            statusBy = "";
        }
        request.setAttribute("statusBy", statusBy);
        //check sortBy
        if (sortBy == null || sortBy.length() == 0 || sortBy.equals("-1")) {
            sortBy = "";
        }
        request.setAttribute("sortBy", sortBy);
        //search by..., status by
        //check choice to sort or not
        if (sortBy.isEmpty()) {
        //get list of customer
        ArrayList<User> listCustomer = customerDBContext.getListCustomer(userRole, searchBy, statusBy);
        request.setAttribute("listCustomer", listCustomer);
        } else {
            //get list of customer
            ArrayList<User> listCustomer2 = customerDBContext.getListCustomer(userRole, searchBy, statusBy);
            ArrayList<User> listCustomerSortBy = customerDBContext.getListCustomerSortBy(listCustomer2, sortBy);
            request.setAttribute("listCustomer", listCustomerSortBy);
        }
        //pass to jsp
        request.getRequestDispatcher("/view/marketing/customerList.jsp").forward(request, response);
    }

    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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