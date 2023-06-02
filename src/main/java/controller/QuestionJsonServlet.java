package controller;

import com.example.javaee.Question;
import com.example.javaee.Vote;
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
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;

@WebServlet(name = "QuestionJsonServlet", value = "/questionsjson")
public class QuestionJsonServlet extends HttpServlet {

    // Поиск голоса по id
    private Vote FindById(Integer id, ArrayList<Vote> votes) {
        if(votes != null) {
            for(Vote r: votes) {
                if((r.getId()).equals(id)) {
                    return r;
                }
            }
        }
        else {
            return null;
        }
        return null;
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        ArrayList<Vote> votes = new ArrayList<Vote>();
        ArrayList<Question> questions = new ArrayList<Question>();
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
            //Загруска всех голосов
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, title, datestart, datefinish, status FROM public.vote ORDER BY id ASC");
            votes.clear();
            while (rs.next()) {
                votes.add(new Vote(rs.getInt("id"),
                        rs.getString("title"),
                        rs.getDate("dateStart"),
                        rs.getDate("dateFinish"),
                        rs.getBoolean("status")
                ));
            }

            stmt.close();
            request.setAttribute("votes", votes);
            //Загрузка всех вопросов
            Integer id;
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT id, voteid, content, datevote FROM public.question ORDER BY id ASC;");
            questions.clear();
           // JsonObjectBuilder jsonObject = Json.createObjectBuilder();
           // JsonArrayBuilder jsonArray = Json.createArrayBuilder();
            while (rs.next()) {
                id = rs.getInt("voteId");
                questions.add(new Question(rs.getInt("id"),
                        id,
                        rs.getString("content"),
                        rs.getDate("dateVote"),
                        FindById(id, votes)
                ));
                //jsonObject.add("id", rs.getInt("id"));
               // jsonObject.add("voteId", rs.getInt("voteId"));
               // jsonObject.add("content", rs.getString("content"));
               // jsonObject.add("dateVote", String.valueOf(rs.getDate("dateVote")));
               // jsonArray.add(jsonObject);
            }
            stmt.close();
            request.setAttribute("questions", questions);
           // request.setAttribute("questionsjson", jsonArray.build());

            PrintWriter writer = response.getWriter();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String questionsjson = gson.toJson(questions);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            writer.print(questionsjson);
            writer.flush(); //flush data to file   <---
            writer.close(); //close write

        } catch (SQLException e) {
            e.printStackTrace();
        }

        //getServletContext().getRequestDispatcher("/jspf/questions.jsp").forward(request, response);
    }
}
