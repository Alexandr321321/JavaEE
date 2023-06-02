package controller;

import com.example.javaee.User;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


@WebServlet(name = "UserEditJsonServlet", value = "/usereditjson")
public class UserEditJsonServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String EDIT_USER = "UPDATE public.user SET fio=?, password=?, email=?, phone=?, status=? WHERE id = ?;";

        StringBuilder jsonBuff = new StringBuilder();
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        String line = null;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                jsonBuff.append(line);
        } catch (Exception e) { writer.print(e); }
        //jsonBuff.deleteCharAt(jsonBuff.lastIndexOf("]"));
       // jsonBuff.deleteCharAt(jsonBuff.lastIndexOf("["));

        String str = jsonBuff.toString();

        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(str).getAsJsonObject();

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

            String idString = String.valueOf(jsonObject.get("id"));
            Integer id = Integer.valueOf(idString.substring(1, idString.length()-1));

            String fio = String.valueOf(jsonObject.get("fio"));
            fio = fio.substring(1, fio.length()-1);

            String password = String.valueOf(jsonObject.get("password"));
            password = password.substring(1, password.length()-1);

            String email = String.valueOf(jsonObject.get("email"));
            email = email.substring(1, email.length()-1);

            String phone = String.valueOf(jsonObject.get("phone"));
            phone = phone.substring(1, phone.length()-1);

            Boolean status = Boolean.valueOf(String.valueOf(jsonObject.get("status")));

            User newUser = new User(id, fio, password, email, phone, status);

            try (PreparedStatement preparedStatement = conn.prepareStatement(EDIT_USER)){
                preparedStatement.setString(1, newUser.getFio());
                preparedStatement.setString(2, newUser.getPassword());
                preparedStatement.setString(3, newUser.getEmail());
                preparedStatement.setString(4, newUser.getPhone());
                preparedStatement.setBoolean(5, newUser.getStatus());
                preparedStatement.setInt(6, newUser.getId());
                int result = preparedStatement.executeUpdate();
            } catch (Exception e) {
                System.out.println(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            //getServletContext().getRequestDispatcher("/jspf/users.jsp").forward(request, response);
        }

        //response.setContentType("text/html");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        writer.print(jsonObject.toString());
        //writer.print(str);
        writer.flush(); //flush data to file   <---
        writer.close(); //close write

    }
}
