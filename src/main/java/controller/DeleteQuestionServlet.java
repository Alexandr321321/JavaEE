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

@WebServlet(name = "DeleteQuestionServlet", value = "/deletequestion")
public class DeleteQuestionServlet extends HttpServlet {

    String SELECT_ALL_QUESTIONS = "SELECT id, voteid, content, datevote FROM public.question ORDER BY id ASC;";
    String SELECT_ALL_VOTES = "SELECT id, title, datestart, datefinish, status FROM public.vote ORDER BY id ASC";
    String SELECT_QUESTIONS_BYID = "SELECT id, voteid, content, datevote FROM public.question WHERE id = ?;";
    String DELETE_QUESTION = "DELETE FROM public.question WHERE id = ?;";
    ArrayList<Vote> votes = new ArrayList<Vote>();
    ArrayList<Question> questions = new ArrayList<Question>();
    ArrayList<Question> deletequestion = new ArrayList<Question>();
    String userPath;

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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

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

            String strId = request.getParameter("id");
            Integer id = null; // id удаляемого вопроса
            if(strId != null) {
                id = Integer.parseInt(strId);
            }

            //Загруска всех вопросов
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SELECT_ALL_VOTES);
            votes.clear();
            while (rs.next()) {
                votes.add(new Vote(rs.getInt("id"),
                        rs.getString("title"),
                        rs.getDate("dateStart"),
                        rs.getDate("dateFinish"),
                        rs.getBoolean("status")
                ));
            }
            rs.close();
            request.setAttribute("votes", votes);

            //Загрузка всех вопросов
            Integer idVote;
            stmt = conn.createStatement();
            rs = stmt.executeQuery(SELECT_ALL_QUESTIONS);
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

            try (PreparedStatement preparedStatement = conn.prepareStatement(SELECT_QUESTIONS_BYID)) {
                preparedStatement.setInt(1, id);
                rs = preparedStatement.executeQuery();
                if(rs != null) {
                    deletequestion.clear();
                    while (rs.next()) {
                        idVote = rs.getInt("voteId");
                        deletequestion.add(new Question(rs.getInt("id"),
                                idVote,
                                rs.getString("content"),
                                rs.getDate("dateVote"),
                                FindByIdVote(idVote, votes)
                        ));
                    }
                    rs.close();
                    request.setAttribute("questionsDelete", deletequestion);
                }
                else
                {
                    System.out.println("Ошибка загрузки question");
                }
            } catch (Exception e) {
                System.out.println(e);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        userPath = request.getServletPath();
        if("/deletequestion".equals(userPath)) {
            request.getRequestDispatcher("/jspf/deletequestion.jsp").forward(request, response);

        }

        //getServletContext().getRequestDispatcher("/jspf/questions.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");

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

            String strId = request.getParameter("id");
            Integer id = null;
            if(strId != null) {
                id = Integer.parseInt(strId);
            }

            try (PreparedStatement preparedStatement =
                         conn.prepareStatement(DELETE_QUESTION)) {
                preparedStatement.setInt(1, id);
                int result = preparedStatement.executeUpdate();
            } catch (Exception e) {
                System.out.println(e);
            }


        } catch (SQLException e) {
            e.printStackTrace();
            getServletContext().getRequestDispatcher("/jspf/deletequestion.jsp").forward(request, response);
        }

        doGet(request, response);
    }
}


