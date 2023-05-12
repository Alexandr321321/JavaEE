package controller;

import com.example.javaee.User;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.example.javaee.Vote;

@WebServlet(name = "VoteServlet", value = "/votes")
public class VoteServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
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

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if("/votes".equals(request.getServletPath())){
            request.getRequestDispatcher("/jspf/votes.jsp").forward(request, response);
        }
        //getServletContext().getRequestDispatcher("/jspf/votes.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");

        String INSERT_VOTES_SQL = "INSERT INTO public.vote(" +
                "title, datestart, datefinish, status)" +
                "VALUES (?, ?, ?, ?);";

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
            String title = request.getParameter("title");
            Date dateStart = Date.valueOf(request.getParameter("dateStart"));
            Date dateFinish = Date.valueOf(request.getParameter("dateFinish"));
            Boolean status = Boolean.valueOf(request.getParameter("status"));

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
            getServletContext().getRequestDispatcher("/jspf/votes.jsp").forward(request, response);
        }

        doGet(request, response);
    }
}
