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
import java.sql.*;


@WebServlet(name = "UserAuthorizationJsonServlet", value = "/userauthorizationjson")
public class UserAuthorizationJsonServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String SEARCH_USER_SQL = "SELECT id, fio, password, email, phone, status FROM public.user WHERE email = ? AND password = ?;";
        String EDIT_USERID_SQL = "UPDATE public.userid SET userid=? WHERE id = 1;";

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

            String password = String.valueOf(jsonObject.get("password"));
            password = password.substring(1, password.length()-1);

            String email = String.valueOf(jsonObject.get("email"));
            email = email.substring(1, email.length()-1);

            try (PreparedStatement preparedStatement = conn.prepareStatement(SEARCH_USER_SQL)){
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, password);
                ResultSet result = preparedStatement.executeQuery();
                while (result.next()) {
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    JsonObject userObject = new JsonObject();
                    userObject.addProperty("find", true);
                    userObject.addProperty("status", result.getBoolean("status"));
                    userObject.addProperty("id", result.getString("id"));
                    Integer userid = result.getInt("id");
                    writer.print(userObject.toString());
                    writer.flush(); //flush data to file   <---
                    writer.close(); //close write
                    try (PreparedStatement preparedStatementId = conn.prepareStatement(EDIT_USERID_SQL)) {
                        preparedStatementId.setInt(1, userid);
                        preparedStatementId.executeUpdate();
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                JsonObject userObject = new JsonObject();
                userObject.addProperty("find", false);
                writer.print(userObject.toString());
                writer.flush(); //flush data to file   <---
                writer.close(); //close write
            }
        } catch (SQLException e) {
            e.printStackTrace();
            //getServletContext().getRequestDispatcher("/jspf/users.jsp").forward(request, response);
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JsonObject userObject = new JsonObject();
        userObject.addProperty("find", false);
        writer.print(userObject.toString());
        writer.flush(); //flush data to file   <---
        writer.close(); //close write

//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        JsonObject userObject = new JsonObject();
//        userObject.addProperty("status", "найден");
//        writer.print(userObject.toString());
//        writer.flush(); //flush data to file   <---
//        writer.close(); //close write

    }
}
