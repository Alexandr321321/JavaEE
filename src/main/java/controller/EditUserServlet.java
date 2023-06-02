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

@WebServlet(name = "EditUserServlet", value = "/edituser")
public class EditUserServlet extends HttpServlet {

    String SELECT_ALL_USERS = "SELECT id, fio, password, email, phone, status FROM public.user ORDER BY id ASC;";
    String SELECT_USER_BYID = "SELECT id, fio, password, email, phone, status FROM public.user WHERE id = ?;";
    String EDIT_USER = "UPDATE public.user SET fio=?, password=?, email=?, phone=?, status=? WHERE id = ?;";
    ArrayList<User> users = new ArrayList<User>();
    ArrayList<User> editusers = new ArrayList<User>();
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
                    "jdbc:postgresql://localhost:5432/postgres",
                    "postgres", "9i%OqhnIZTVN"
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
                        rs.getString("fio"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getBoolean("status")
                ));
            }
            stmt.close();
            request.setAttribute("users", users);


            try (PreparedStatement preparedStatement = conn.prepareStatement(SELECT_USER_BYID)) {
                preparedStatement.setInt(1, id);
                rs = preparedStatement.executeQuery();
                if(rs != null) {
                    editusers.clear();
                    while (rs.next()) {
                        editusers.add(new User(rs.getInt("id"),
                                rs.getString("fio"),
                                rs.getString("password"),
                                rs.getString("email"),
                                rs.getString("phone"),
                                rs.getBoolean("status")
                        ));
                    }
                    rs.close();
                    request.setAttribute("usersEdit", editusers);
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
        if("/edituser".equals(userPath)) {
            request.getRequestDispatcher("/jspf/edituser.jsp").forward(request, response);

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
                    "jdbc:postgresql://localhost:5432/postgres",
                    "postgres", "9i%OqhnIZTVN"
            );


            String strId = request.getParameter("id");
            Integer id = null;
            if(strId != null) {
                id = Integer.valueOf(strId);
            }

            String fio = request.getParameter("fio");
            String password = request.getParameter("password");
            String phone = request.getParameter("phone");
            String email = request.getParameter("email");
            Boolean status = Boolean.valueOf(request.getParameter("status"));

            try (PreparedStatement preparedStatement = conn.prepareStatement(EDIT_USER)){
                preparedStatement.setString(1, fio);
                preparedStatement.setString(2, password);
                preparedStatement.setString(3, email);
                preparedStatement.setString(4, phone);
                preparedStatement.setBoolean(5, status);
                preparedStatement.setInt(6, id);
                int result = preparedStatement.executeUpdate();
            } catch (Exception e) {
                System.out.println(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            getServletContext().getRequestDispatcher("/jspf/edituser.jsp").forward(request, response);
        }

        doGet(request, response);
    }
}
