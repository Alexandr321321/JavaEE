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

@WebServlet(name = "EditQuestionServlet", value = "/editquestion")
public class EditQuestionServlet extends HttpServlet {

    String SELECT_ALL_QUESTIONS = "SELECT id, voteid, content, datevote FROM public.question ORDER BY id ASC;";
    String SELECT_ALL_VOTES = "SELECT id, title, datestart, datefinish, status FROM public.vote ORDER BY id ASC";
    String SELECT_QUESTIONS_BYID = "SELECT id, voteid, content, datevote FROM public.question WHERE id = ?;";
    String EDIT_QUESTION = "UPDATE public.question SET voteid=?, content=?, datevote=? WHERE id = ?;";
    ArrayList<Vote> votes = new ArrayList<Vote>();
    ArrayList<Question> questions = new ArrayList<Question>();
    ArrayList<Question> editquestions = new ArrayList<Question>();
    String userPath;

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
            Integer id = null; // id редактируемой должности
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
                        FindById(idVote, votes)
                ));
            }
            stmt.close();
            request.setAttribute("questions", questions);

            try (PreparedStatement preparedStatement = conn.prepareStatement(SELECT_QUESTIONS_BYID)) {
                preparedStatement.setInt(1, id);
                rs = preparedStatement.executeQuery();
                if(rs != null) {
                    editquestions.clear();
                    while (rs.next()) {
                        idVote = rs.getInt("voteId");
                        editquestions.add(new Question(rs.getInt("id"),
                                idVote,
                                rs.getString("content"),
                                rs.getDate("dateVote"),
                                FindById(idVote, votes)
                        ));
                    }
                    rs.close();
                    request.setAttribute("questionsEdit", editquestions);
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
        if("/editquestion".equals(userPath)) {
            request.getRequestDispatcher("/jspf/editquestion.jsp").forward(request, response);

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

            String strId = request.getParameter("id");
            Integer id = null;
            if(strId != null) {
                id = Integer.parseInt(strId);
            }

            String content = request.getParameter("content");
            Date dateVote = Date.valueOf(request.getParameter("dateVote"));

            String vote = request.getParameter("voteId");
            int index1 = vote.indexOf('=');
            int index2 = vote.indexOf(",");
            String r1 = vote.substring(index1+1, index2);
            Integer voteId = Integer.parseInt(r1.trim());

            PreparedStatement preparedStatement = conn.prepareStatement(EDIT_QUESTION);

            preparedStatement.setInt(1, voteId);
            preparedStatement.setString(2, content);
            preparedStatement.setDate(3, dateVote);
            preparedStatement.setInt(4, id);
            int result = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            getServletContext().getRequestDispatcher("/jspf/editquestion.jsp").forward(request, response);
        }


        doGet(request, response);
    }
}

