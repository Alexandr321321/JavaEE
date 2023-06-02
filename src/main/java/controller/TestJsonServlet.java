package controller;

import com.example.javaee.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;

@WebServlet(name = "TestJsonServlet", value = "/testjson")
public class TestJsonServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        ArrayList<User> users = new ArrayList<User>();
        ArrayList<User> user = new ArrayList<User>();

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
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, firstname, lastname, email, phone, status FROM public.user ORDER BY id ASC;");
            users.clear();
            //JsonObjectBuilder jsonObject = Json.createObjectBuilder();
            //JsonArrayBuilder jsonArray = Json.createArrayBuilder();
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
            //request.setAttribute("usersjson", jsonArray.build());

            //Writer writer = new FileWriter("../opt/tomcatt/webapps/JavaEE-1.0-SNAPSHOT/WEB-INF/classes/users.json");
            PrintWriter writer = response.getWriter();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String usersjson = gson.toJson(users.get(0));

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            writer.print(usersjson);
            writer.flush(); //flush data to file   <---
            writer.close(); //close write

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
