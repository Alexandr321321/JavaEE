package controller;

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


@WebServlet(name = "ChoiceGetJsonServlet", value = "/choicegetjson")
public class ChoiceGetJsonServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String SEARCH_CHOICE_SQL = "SELECT id, questionid, userid, choiceuser FROM public.choice WHERE id = ?;";

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

            String id = String.valueOf(jsonObject.get("id"));
            id = id.substring(1, id.length()-1);
            Integer idchoice = Integer.valueOf(id);

            try (PreparedStatement preparedStatement = conn.prepareStatement(SEARCH_CHOICE_SQL)){
                preparedStatement.setInt(1, idchoice);
                ResultSet result = preparedStatement.executeQuery();
                while (result.next()) {
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    JsonObject userObject = new JsonObject();
                    userObject.addProperty("id", result.getString("id"));
                    userObject.addProperty("questionid", result.getString("questionid"));
                    userObject.addProperty("userid", result.getString("userid"));
                    userObject.addProperty("choiceuser", result.getString("choiceuser"));

                    writer.print(userObject.toString());
                    writer.flush(); //flush data to file   <---
                    writer.close(); //close write
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
