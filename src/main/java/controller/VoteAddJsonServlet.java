package controller;

import com.example.javaee.User;
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


@WebServlet(name = "VoteAddJsonServlet", value = "/voteaddjson")
public class VoteAddJsonServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String INSERT_VOTES_SQL = "INSERT INTO public.vote(" +
                "title, datestart, datefinish, status)" +
                "VALUES (?, ?, ?, ?);";

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


            String title = String.valueOf(jsonObject.get("title"));
            title = title.substring(1, title.length()-1);

            String dateStartString = String.valueOf(jsonObject.get("datestart"));
            dateStartString = dateStartString.substring(1, dateStartString.length()-1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
            LocalDate dateStartLocal = LocalDate.parse(dateStartString, formatter);
            Date dateStart = java.sql.Date.valueOf(dateStartLocal);

            String dateFinishString = String.valueOf(jsonObject.get("datefinish"));
            dateFinishString = dateFinishString.substring(1, dateFinishString.length()-1);
            LocalDate dateFinishLocal = LocalDate.parse(dateFinishString, formatter);
            Date dateFinish = java.sql.Date.valueOf(dateFinishLocal);

            Boolean status = Boolean.valueOf(String.valueOf(jsonObject.get("status")));


            Vote newVote = new Vote(title, dateStart, dateFinish, status);

            try (PreparedStatement preparedStatement = conn.prepareStatement(INSERT_VOTES_SQL)){
                preparedStatement.setString(1, newVote.getTitle());
                preparedStatement.setDate(2, newVote.getDateStart());
                preparedStatement.setDate(3, newVote.getDateFinish());
                preparedStatement.setBoolean(4, newVote.getStatus());
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
