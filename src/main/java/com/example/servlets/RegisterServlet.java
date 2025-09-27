package com.example.servlets;

import com.example.dao.UserDAO;
import com.example.dao.DriverDAO;
import com.example.dao.RegularUserDAO;
import com.example.dao.AdminDAO;
import com.example.model.User;
import com.example.util.HashUtil;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    // SECRET ADMIN CODE – only users who know this can register as admin
    private static final String MASTER_ADMIN_CODE = "admin123";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String role = request.getParameter("role");
        request.setAttribute("role", role);
        RequestDispatcher rd = request.getRequestDispatcher("/register.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        request.setCharacterEncoding("UTF-8");
        String role = request.getParameter("role");
        if (role == null || role.isBlank()) role = "user";

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");

        // Extra fields
        String licenseNumber = request.getParameter("licenseNumber"); // for driver
        String vehicleType   = request.getParameter("vehicleType");   // optional for driver
        String adminCode     = request.getParameter("adminCode");     // for admin

        if (name == null || email == null || password == null ||
                name.isBlank() || email.isBlank() || password.isBlank()) {
            request.setAttribute("error", "Please fill all required fields.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        try {
            UserDAO userDao = new UserDAO();

            if (userDao.emailExists(email)) {
                request.setAttribute("error", "Email already registered.");
                request.getRequestDispatcher("/register.jsp").forward(request, response);
                return;
            }

            // Base user creation
            User u = new User();
            u.setName(name.trim());
            u.setEmail(email.trim().toLowerCase());
            u.setPhone(phone != null ? phone.trim() : null);
            u.setRole(role);
            u.setPasswordHash(HashUtil.hash(password));

            int newId = userDao.create(u);
            if (newId <= 0) throw new SQLException("Insert into users failed");

            // Insert into role-specific table
            switch (role.toLowerCase()) {
                case "driver" -> {
                    if (licenseNumber == null || licenseNumber.isBlank()) {
                        request.setAttribute("error", "License number is required for drivers.");
                        request.getRequestDispatcher("/register.jsp").forward(request, response);
                        return;
                    }
                    DriverDAO driverDao = new DriverDAO();
                    driverDao.createDriver(newId, licenseNumber.trim(), vehicleType);
                }
                case "admin" -> {
                    // ADMIN AUTHORIZATION CHECK
                    if (adminCode == null || !adminCode.equals(MASTER_ADMIN_CODE)) {
                        request.setAttribute("error", "Invalid admin authorization code.");
                        request.getRequestDispatcher("/register.jsp").forward(request, response);
                        return;
                    }
                    AdminDAO adminDao = new AdminDAO();
                    adminDao.createAdmin(newId, adminCode.trim());
                }
                default -> {
                    RegularUserDAO regDao = new RegularUserDAO();
                    regDao.createRegularUser(newId, null); // no default payment info
                }
            }

            // Auto-login after registration
            HttpSession session = request.getSession(true);
            session.setAttribute("userId", newId);
            session.setAttribute("userRole", role);
            response.sendRedirect(request.getContextPath() + "/profile");

        } catch (SQLException ex) {
            ex.printStackTrace();
            request.setAttribute("error", "Server error. Try again.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }

    }
}
