package controller;

import com.example.javaee.Choice;
import com.example.javaee.Question;
import com.example.javaee.User;
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

@WebServlet(name = "ChoiceJsonServletTest", value = "/choicesjsontest")
public class ChoiceJsonServletTest extends HttpServlet {

    // Поиск вопроса по id
    private Vote FindByIdVote(Integer id, ArrayList<Vote> votes) {
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

    // Поиск вопроса по id
    private Question FindByIdQuestion(Integer id, ArrayList<Question> questions) {
        if(questions != null) {
            for(Question r: questions) {
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

    // Поиск голоса по id
    private User FindByIdUsers(Integer id, ArrayList<User> users) {
        if(users != null) {
            for(User r: users) {
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
        ArrayList<Question> questions = new ArrayList<Question>();
        ArrayList<User> users = new ArrayList<User>();
        ArrayList<Choice> choices = new ArrayList<Choice>();
        ArrayList<Vote> votes = new ArrayList<Vote>();
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

            //Загрузка всех вопросов
            Integer idVote;
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT id, voteid, content, datevote FROM public.question;");
            questions.clear();
            while (rs.next()) {
                idVote = rs.getInt("voteId");
                questions.add(new Question(rs.getInt("id"),
                        idVote,
                        rs.getString("content"),
                        rs.getDate("dateVote"),
                        FindByIdVote(idVote, votes)
                ));
            }
            stmt.close();
            request.setAttribute("questions", questions);

            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT id, fio, password, email, phone, status FROM public.user ORDER BY id ASC");
            users.clear();
            while (rs.next()) {
                users.add(new User(rs.getInt("id"),
                        rs.getString("fio"),
                        rs.getString("password"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getBoolean("status")
                ));
            }
            stmt.close();
            request.setAttribute("users", users);

            Integer idQuestion;
            Integer idUser;
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT id, questionid, userid, choiceuser FROM public.choice;");
            choices.clear();
            JsonObjectBuilder jsonObject = Json.createObjectBuilder();
            JsonArrayBuilder jsonArray = Json.createArrayBuilder();
            while (rs.next()) {
                idQuestion = rs.getInt("questionId");
                idUser = rs.getInt("userId");
                choices.add(new Choice(rs.getInt("id"),
                        idQuestion,
                        idUser,
                        rs.getInt("choiceUser"),
                        FindByIdQuestion(idQuestion, questions),
                        FindByIdUsers(idUser, users)
                ));
                jsonArray.add(Json.createObjectBuilder()
                        .add("id", rs.getInt("id"))
                        .add("questionid", rs.getInt("questionId"))
                        .add("userid", rs.getInt("userId"))
                        .add("choiceuser", rs.getInt("choiceUser"))
                        .add("questtioncontent", FindByIdQuestion(idQuestion, questions).getContent())
                        .add("userfio", FindByIdUsers(idUser, users).getFio()));
            }
            stmt.close();
            request.setAttribute("choices", choices);
           // request.setAttribute("choicesjson", jsonArray.build());

            PrintWriter writer = response.getWriter();

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            writer.print(jsonObject.add("choices", jsonArray).build());
            writer.flush(); //flush data to file   <---
            writer.close(); //close write

        } catch (SQLException e) {
            e.printStackTrace();
        }
        //getServletContext().getRequestDispatcher("/jspf/choices.jsp").forward(request, response);
    }
}
