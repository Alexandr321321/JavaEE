package controller;

import com.example.javaee.Question;
import com.example.javaee.Vote;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;


@WebServlet(name = "QuestionAddJsonServlet", value = "/questionaddjson")
public class QuestionAddJsonServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String INSERT_QUESTION_SQL = "INSERT INTO public.question(" +
                "voteid, content, datevote)" +
                "VALUES (?, ?, ?);";

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

            String idString = String.valueOf(jsonObject.get("voteid"));
            Integer voteId = Integer.valueOf(idString.substring(1, idString.length()-1));

            String content = String.valueOf(jsonObject.get("content"));
            content = content.substring(1, content.length()-1);

            String dateVoteString = String.valueOf(jsonObject.get("datevote"));
            dateVoteString = dateVoteString.substring(1, dateVoteString.length()-1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
            LocalDate dateVoteLocal = LocalDate.parse(dateVoteString, formatter);
            Date dateVote = java.sql.Date.valueOf(dateVoteLocal);

            Question newQuestion = new Question(voteId, content, dateVote);

            try (PreparedStatement preparedStatement = conn.prepareStatement(INSERT_QUESTION_SQL)){
                preparedStatement.setInt(1, voteId);
                preparedStatement.setString(2, newQuestion.getContent());
                preparedStatement.setDate(3, newQuestion.getDateVote());
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
