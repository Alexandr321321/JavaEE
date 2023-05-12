package controller;

import com.example.javaee.Question;
import com.example.javaee.User;
import com.example.javaee.Vote;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

@WebServlet(name = "QuestionServlet", value = "/questions")
public class QuestionServlet extends HttpServlet {

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

            stmt.close();
            request.setAttribute("votes", votes);
            //Загрузка всех вопросов
            Integer id;
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT id, voteid, content, datevote FROM public.question ORDER BY id ASC;");
            questions.clear();
            while (rs.next()) {
                id = rs.getInt("voteId");
                questions.add(new Question(rs.getInt("id"),
                        id,
                        rs.getString("content"),
                        rs.getDate("dateVote"),
                        FindById(id, votes)
                ));
            }
            stmt.close();
            request.setAttribute("questions", questions);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if("/questions".equals(request.getServletPath())){
            request.getRequestDispatcher("/jspf/questions.jsp").forward(request, response);
        }

        //getServletContext().getRequestDispatcher("/jspf/questions.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");

        String INSERT_QUESTION_SQL = "INSERT INTO public.question(" +
                "voteid, content, datevote)" +
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
            String content = request.getParameter("content");
            Date dateVote = Date.valueOf(request.getParameter("dateVote"));
            String vote = request.getParameter("voteId");
            int index1 = vote.indexOf('=');
            int index2 = vote.indexOf(",");
            String r1 = vote.substring(index1+1, index2);
            Integer voteId = Integer.parseInt(r1.trim());

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
            getServletContext().getRequestDispatcher("/jspf/questions.jsp").forward(request, response);
        }


        doGet(request, response);
    }
}
