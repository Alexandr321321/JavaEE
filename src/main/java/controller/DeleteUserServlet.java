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

@WebServlet(name = "DeleteUserServlet", value = "/deleteuser")
public class DeleteUserServlet extends HttpServlet {

    String SELECT_ALL_USERS = "SELECT id, firstname, lastname, email, phone, status FROM public.user ORDER BY id ASC;";
    String SELECT_USER_BYID = "SELECT id, firstname, lastname, email, phone, status FROM public.user WHERE id = ?;";
    String DELETE_USER = "DELETE FROM public.user WHERE id = ?;";
    ArrayList<User> users = new ArrayList<User>();
    ArrayList<User> deleteuser = new ArrayList<User>();
    String userPath;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

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

            String strId = request.getParameter("id");
            Integer id = null; // id редактируемой должности
            if(strId != null) {
                id = Integer.parseInt(strId);
            }
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SELECT_ALL_USERS);
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
            rs.close();
            request.setAttribute("users", users);


            try (PreparedStatement preparedStatement = conn.prepareStatement(SELECT_USER_BYID)) {
                preparedStatement.setInt(1, id);
                rs = preparedStatement.executeQuery();
                if(rs != null) {
                    deleteuser.clear();
                    while (rs.next()) {
                        deleteuser.add(new User(rs.getInt("id"),
                                rs.getString("firstName"),
                                rs.getString("lastName"),
                                rs.getString("phone"),
                                rs.getString("email"),
                                rs.getBoolean("status")
                        ));
                    }
                    rs.close();
                    request.setAttribute("usersDelete", deleteuser);
                }
                else
                {
                    System.out.println("Ошибка загрузки user");
                }
            } catch (Exception e) {
                System.out.println(e);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        userPath = request.getServletPath();
        if("/deleteuser".equals(userPath)) {
            request.getRequestDispatcher("/jspf/deleteuser.jsp").forward(request, response);

        }
        //getServletContext().getRequestDispatcher("/jspf/users.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

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


            Integer id = Integer.parseInt(request.getParameter("id"));
            try (PreparedStatement preparedStatement =
                         conn.prepareStatement(DELETE_USER)) {
                preparedStatement.setInt(1, id);
                int result = preparedStatement.executeUpdate();
            } catch (Exception e) {
                System.out.println(e);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            getServletContext().getRequestDispatcher("/jspf/deleteuser.jsp").forward(request, response);
        }

        doGet(request, response);
    }
}
