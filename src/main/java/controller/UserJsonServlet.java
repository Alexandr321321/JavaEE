package controller;

import com.example.javaee.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;

@WebServlet(name = "UserJsonServlet", value = "/usersjson")
public class UserJsonServlet extends HttpServlet {

    private static FileWriter file;


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
            ResultSet rs = stmt.executeQuery("SELECT id, fio, password, email, phone, status FROM public.user ORDER BY id ASC;");
            users.clear();
            //JsonObjectBuilder jsonObject = Json.createObjectBuilder();
            //JsonArrayBuilder jsonArray = Json.createArrayBuilder();
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
            //request.setAttribute("usersjson", jsonArray.build());

            //Writer writer = new FileWriter("../opt/tomcatt/webapps/JavaEE-1.0-SNAPSHOT/WEB-INF/classes/users.json");
            PrintWriter writer = response.getWriter();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String usersjson = gson.toJson(users);

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
