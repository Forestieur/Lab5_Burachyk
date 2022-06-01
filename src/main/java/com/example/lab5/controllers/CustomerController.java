package com.example.lab5.controllers;



import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



@WebServlet(name = "CustomerController", urlPatterns = "/processcustomer")
public class CustomerController extends HttpServlet {
    @Override
    protected void doPost(
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestCustomer customer = RequestCustomer.fromRequestParameters(request);
        customer.setAsRequestAttributes(request);
        List<String> violations = customer.validate();
        if (!violations.isEmpty()) {
            request.setAttribute("violations", violations);
        }
        String url = determineUrl(violations);
        request.getRequestDispatcher(url).forward(request, response);
    }
    private String determineUrl(List<String> violations) {
        if (!violations.isEmpty()) {
            return "/";
        } else {
            return "/WEB-INF/views/customerinfo.jsp";
        }
    }
    private static class RequestCustomer {
        private final String firstName;
        private final String lastName;
        private final String email;
        private RequestCustomer(String firstName, String lastName, String email) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
        }
        public static RequestCustomer fromRequestParameters(
                HttpServletRequest request) {
            return new RequestCustomer(
                    request.getParameter("firstname"),
                    request.getParameter("lastname"),
                    request.getParameter("email"));
        }
        public void setAsRequestAttributes(HttpServletRequest request) {
            request.setAttribute("firstname", firstName);
            request.setAttribute("lastname", lastName);
            request.setAttribute("email", email);
        }
        private boolean sValidate(String name){
            return name.isEmpty() || name.matches(".*[0-9].*");
        }

        private  boolean eValidate(String email){
            return !email.contains("@") || !email.contains(".");
        }

        public List<String> validate() {
            List<String> violations = new ArrayList<String>();
            if (sValidate(firstName)) {
                violations.add("Please enter correct name");
            }
            if (sValidate(lastName)) {
                violations.add("Please enter correct surname");
            }
            if (eValidate(email)) {
                violations.add("Please enter correct Email");
            }
            return violations;
        }
    }

}