package controller;

import com.example.javaee.Choice;
import com.example.javaee.Question;
import com.example.javaee.User;
import com.example.javaee.Vote;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

@WebServlet(name = "ChoiceServlet", value = "/choices")
public class ChoiceServlet extends HttpServlet {

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
                    "jdbc:postgresql://localhost:5432/golosovanie",
                    "postgres", "a53mx0z29_-"
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
            rs = stmt.executeQuery("SELECT id, firstname, lastname, email, phone, status FROM public.user ORDER BY id ASC");
            users.clear();
            while (rs.next()) {
                users.add(new User(rs.getInt("id"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
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
            }
            stmt.close();
            request.setAttribute("choices", choices);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if("/choices".equals(request.getServletPath())){
            request.getRequestDispatcher("/jspf/choices.jsp").forward(request, response);
        }

        //getServletContext().getRequestDispatcher("/jspf/choices.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");

        String INSERT_CHOICE_SQL = "INSERT INTO public.choice(" +
                "questionid, userid, choiceuser)" +
                "VALUES (?, ?, ?);";

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/golosovanie",
                    "postgres", "a53mx0z29_-"
            );

            Integer choiceUser = Integer.valueOf(request.getParameter("choiceUser"));

            String question = request.getParameter("questionId");
            int index1 = question.indexOf('=');
            int index2 = question.indexOf(",");
            String r1 = question.substring(index1+1, index2);
            Integer questionId = Integer.parseInt(r1.trim());

            String user = request.getParameter("userId");
            int index3 = user.indexOf('=');
            int index4 = user.indexOf(",");
            String r2 = user.substring(index3+1, index4);
            Integer userId = Integer.parseInt(r2.trim());

            Choice newQuestion = new Choice(questionId, userId, choiceUser);

            try (PreparedStatement preparedStatement = conn.prepareStatement(INSERT_CHOICE_SQL)){
                preparedStatement.setInt(1, questionId);
                preparedStatement.setInt(2, userId);
                preparedStatement.setInt(3, newQuestion.getChoiceUser());
                int result = preparedStatement.executeUpdate();
            } catch (Exception e) {
                System.out.println(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            getServletContext().getRequestDispatcher("/jspf/choices.jsp").forward(request, response);
        }

        doGet(request, response);
    }
}
