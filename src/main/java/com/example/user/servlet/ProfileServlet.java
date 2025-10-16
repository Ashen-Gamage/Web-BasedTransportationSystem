package com.example.user.servlet;

import com.example.user.model.User;
import com.example.user.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ProfileServlet.class.getName());
    private final UserService userService = new UserService();
    private static final Pattern SL_PHONE = Pattern.compile("^07\\d{8}$");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User sessionUser = session != null ? (User) session.getAttribute("user") : null;

        if (sessionUser == null) {
            response.sendRedirect(request.getContextPath() + "/jsp/user/login.jsp");
            return;
        }

        try {
            User freshUser = userService.findUserByEmail(sessionUser.getEmail());
            if (freshUser != null) {
                request.setAttribute("user", freshUser);
            } else {
                String msg = "User not found for email=" + sessionUser.getEmail();
                LOGGER.warning(msg);
                request.setAttribute("error", msg);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to load profile for email=" + sessionUser.getEmail(), e);
            request.setAttribute("error", "Failed to load profile: " + e.getMessage());
        }

        request.getRequestDispatcher("/jsp/user/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User sessionUser = session != null ? (User) session.getAttribute("user") : null;

        if (sessionUser == null) {
            response.sendRedirect(request.getContextPath() + "/jsp/user/login.jsp");
            return;
        }

        String action = request.getParameter("action");

        if ("delete".equalsIgnoreCase(action)) {
            // DELETE profile
            try {
                userService.deleteProfileByEmail(sessionUser.getEmail());
                session.invalidate();
                response.sendRedirect(request.getContextPath() + "/jsp/user/login.jsp?msg=deleted");
            } catch (SQLException e) {
                request.setAttribute("error", "Failed to delete profile: " + e.getMessage());
                request.getRequestDispatcher("/jsp/user/profile.jsp").forward(request, response);
            }
            return;
        }

        // UPDATE profile
        String username = request.getParameter("username");
        String phone = request.getParameter("phone");
        String addressLine = request.getParameter("address_line");
        String city = request.getParameter("city");
        String postalCode = request.getParameter("postal_code");

        if (username == null || username.trim().isEmpty()) {
            request.setAttribute("error", "Username cannot be empty.");
            request.getRequestDispatcher("/jsp/user/profile.jsp").forward(request, response);
            return;
        }

        if (phone != null && !phone.trim().isEmpty()) {
            String normalized = phone.trim();
            if (!SL_PHONE.matcher(normalized).matches()) {
                request.setAttribute("error", "Phone must be a Sri Lanka number of 10 digits starting with 07.");
                request.getRequestDispatcher("/jsp/user/profile.jsp").forward(request, response);
                return;
            }
            phone = normalized;
        } else {
            phone = null;
        }

        try {
            User toUpdate = userService.findUserByEmail(sessionUser.getEmail());
            if (toUpdate == null) {
                String msg = "User not found for email=" + sessionUser.getEmail();
                LOGGER.warning(msg);
                request.setAttribute("error", msg);
                request.getRequestDispatcher("/jsp/user/profile.jsp").forward(request, response);
                return;
            }

            toUpdate.setUsername(username.trim());
            toUpdate.setPhone(phone);
            toUpdate.setAddressLine(addressLine != null ? addressLine.trim() : null);
            toUpdate.setCity(city != null ? city.trim() : null);
            toUpdate.setPostalCode(postalCode != null ? postalCode.trim() : null);

            userService.updateUserProfile(toUpdate);

            // Refresh session user
            User refreshedUser = userService.findUserByEmail(sessionUser.getEmail());
            session.setAttribute("user", refreshedUser);
            request.setAttribute("message", "Profile updated successfully.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to update profile for email=" + sessionUser.getEmail(), e);
            request.setAttribute("error", "Failed to update profile: " + e.getMessage());
        }

        request.getRequestDispatcher("/jsp/user/profile.jsp").forward(request, response);
    }
}
