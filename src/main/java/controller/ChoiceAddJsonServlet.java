package controller;

import com.example.javaee.Choice;
import com.example.javaee.Question;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;


@WebServlet(name = "ChoiceAddJsonServlet", value = "/choiceaddjson")
public class ChoiceAddJsonServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String INSERT_CHOICE_SQL = "INSERT INTO public.choice (questionid, userid, choiceuser) VALUES (?, ?, ?);";
        String SELECT_USERID_SQL = "SELECT userid FROM public.userid WHERE id=1;";

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

            String idQuestionString = String.valueOf(jsonObject.get("questionid"));
            Integer questionId = Integer.valueOf(idQuestionString.substring(1, idQuestionString.length()-1));

            String idChoiceUserString = String.valueOf(jsonObject.get("choiceuser"));
            Integer choiceUser = Integer.valueOf(idChoiceUserString.substring(1, idChoiceUserString.length()-1));

           // Choice newChoice = new Choice(questionId, userid, choiceUser);

            try (PreparedStatement preparedStatement = conn.prepareStatement(INSERT_CHOICE_SQL)) {
                preparedStatement.setInt(1, questionId);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(SELECT_USERID_SQL);
                while (rs.next()) {
                    Integer userid = rs.getInt("userid");
                    preparedStatement.setInt(2, userid);
                }
                preparedStatement.setInt(3, choiceUser);
                int result = preparedStatement.executeUpdate();
            } catch (Exception e) {
                System.out.println(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            //getServletContext().getRequestDispatcher("/jspf/users.jsp").forward(request, response);
        }

        response.setContentType("text/html");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        writer.print(jsonObject.toString());
        //writer.print(str);
        writer.flush(); //flush data to file   <---
        writer.close(); //close write

    }
}
