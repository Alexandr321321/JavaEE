package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.example.javaee.User;

@WebServlet(name = "UserServlet", value = "/users")
public class UserServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        ArrayList<User> users = new ArrayList<User>();

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/golosovanie",
                    "postgres", "a53mx0z29_-"
            );
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, firstname, lastname, email, phone, status FROM public.user ORDER BY id ASC;");
            users.clear();
            while (rs.next()) {
                users.add(new User(rs.getInt("id"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getBoolean("status")
                        ));
            }
            stmt.close();
            request.setAttribute("users", users);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if("/users".equals(request.getServletPath())){
            request.getRequestDispatcher("/jspf/users.jsp").forward(request, response);
        }

        //getServletContext().getRequestDispatcher("/jspf/users.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        String INSERT_USERS_SQL = "INSERT INTO public.user" +
                "(firstname, lastname, email, phone, status) " +
                "VALUES (?, ?, ?, ?, ?);";

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/golosovanie",
                    "postgres", "a53mx0z29_-"
            );
            String firstname = request.getParameter("firstname");
            String lastname = request.getParameter("lastname");
            String phone = request.getParameter("phone");
            String email = request.getParameter("email");
            Boolean status = Boolean.valueOf(request.getParameter("status"));

            User newUser = new User(firstname, lastname, email, phone, status);

            try (PreparedStatement preparedStatement = conn.prepareStatement(INSERT_USERS_SQL)){
                preparedStatement.setString(1, newUser.getFirstName());
                preparedStatement.setString(2, newUser.getLastName());
                preparedStatement.setString(3, newUser.getEmail());
                preparedStatement.setString(4, newUser.getPhone());
                preparedStatement.setBoolean(5, newUser.getStatus());
                int result = preparedStatement.executeUpdate();
            } catch (Exception e) {
                System.out.println(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            getServletContext().getRequestDispatcher("/jspf/users.jsp").forward(request, response);
        }

        doGet(request, response);
    }
}
