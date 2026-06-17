package com.sujalkamanna.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sujalkamanna.dao.LoginDAO;
import com.sujalkamanna.dao.LoginDAOImpl;
import com.sujalkamanna.model.Login;

public class LoginController extends HttpServlet {

private static final long serialVersionUID = 1L;

private LoginDAO loginDAO;

public LoginController() {
    loginDAO = new LoginDAOImpl();
}

@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    HttpSession session = request.getSession();

    Login login = new Login();
    login.setEmail(request.getParameter("email"));
    login.setPassword(request.getParameter("password"));

    String result = loginDAO.loginCheck(login);

    if ("true".equals(result)) {

        session.setAttribute("email", login.getEmail());

        response.sendRedirect("EmployeeController?action=LIST");

    } else if ("false".equals(result)) {

        response.sendRedirect("index.jsp?status=false");

    } else {

        response.sendRedirect("index.jsp?status=error");
    }
}

}
