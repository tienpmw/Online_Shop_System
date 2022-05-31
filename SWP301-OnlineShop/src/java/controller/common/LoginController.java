/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.common;

import configs.Security;
import dal.UserDBContext;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.User;

/**
 *
 * @author Admin
 */
public class LoginController extends HttpServlet {

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
        request.getRequestDispatcher("view/public/login.jsp").forward(request, response);
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
        Cookie[] cookies = null;
        cookies = request.getCookies();
        String email = null, pass = null;
        if (cookies != null) {
            for (Cookie cooky : cookies) {
                if (cooky.getName().equals("emailCookie")) {
                    email = cooky.getValue();
                }
                if (cooky.getName().equals("passCookie")) {
                    pass = cooky.getValue();
                }

                if (email != null && pass != null) {
                    request.setAttribute("email", email);
                    request.setAttribute("password", pass);
                    request.setAttribute("checkbox", "checked");
                    break;
                }
            }
        }
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
        String email = "", password = "", check = null, page = "";
        User user = null;
        UserDBContext userDb = new UserDBContext();
        HttpSession session = request.getSession();
        check = request.getParameter("cboSigned");
        email = request.getParameter("txtEmail");
        password = request.getParameter("txtPassword");
        user = userDb.getUserByEmailAndPassword(email, password);

        if (user != null && user.isStatus() == true) {
            session.setAttribute("user", user);
            Cookie emailCookie = new Cookie("emailCookie", email);
            Cookie passCookie = new Cookie("passCookie", password);
            if (check != null) {
                emailCookie.setMaxAge(Security.MAXIMUM_AGE);
            } else {
                emailCookie.setMaxAge(0);
            }
            response.addCookie(emailCookie);
            response.addCookie(passCookie);

            //Sendirect
            if (user.getRole().getId() == Security.ADMIN_ROLL_ID) {
                page = "home";

            } else if (user.getRole().getId() == Security.MAKETING_ROLL_ID) {
                page = "home";

            } else if (user.getRole().getId() == Security.SALE_ROLL_ID) {
                page = "home";
            } else if (user.getRole().getId() == Security.CUSTOMER_ROLL_ID) {
                page = "home";
            }
            if (page != "") {
                response.sendRedirect(page);
            } else {
                request.setAttribute("messFalse", "Email or password is incorrect. Please try again!");
                processRequest(request, response);
            }
        } else {
            request.setAttribute("messFalse", "Email or password is incorrect. Please try again!");
             processRequest(request, response);
        }

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
