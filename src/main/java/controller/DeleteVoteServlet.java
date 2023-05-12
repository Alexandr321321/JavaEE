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

@WebServlet(name = "DeleteVoteServlet", value = "/deletevote")
public class DeleteVoteServlet extends HttpServlet {

    String SELECT_ALL_VOTES = "SELECT id, title, datestart, datefinish, status FROM public.vote ORDER BY id ASC;";
    String SELECT_VOTES_BYID = "SELECT id, title, datestart, datefinish, status FROM public.vote WHERE id = ?;";
    String DELETE_VOTE = "DELETE FROM public.vote WHERE id = ?;";
    ArrayList<Vote> votes = new ArrayList<Vote>();
    ArrayList<Vote> deletevotes = new ArrayList<Vote>();
    String userPath;

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

            String strId = request.getParameter("id");
            Integer id = null; // id редактируемой должности
            if(strId != null) {
                id = Integer.parseInt(strId);
            }
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
            stmt.close();
            request.setAttribute("votes", votes);

            try (PreparedStatement preparedStatement = conn.prepareStatement(SELECT_VOTES_BYID)) {
                preparedStatement.setInt(1, id);
                rs = preparedStatement.executeQuery();
                if(rs != null) {
                    deletevotes.clear();
                    while (rs.next()) {
                        deletevotes.add(new Vote(rs.getInt("id"),
                                rs.getString("title"),
                                rs.getDate("dateStart"),
                                rs.getDate("dateFinish"),
                                rs.getBoolean("status")
                        ));
                    }
                    rs.close();
                    request.setAttribute("votesDelete", deletevotes);
                }
                else
                {
                    System.out.println("Ошибка загрузки vote");
                }
            } catch (Exception e) {
                System.out.println(e);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        userPath = request.getServletPath();
        if("/deletevote".equals(userPath)) {
            request.getRequestDispatcher("/jspf/deletevote.jsp").forward(request, response);

        }
        //getServletContext().getRequestDispatcher("/jspf/votes.jsp").forward(request, response);
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

            Integer id = Integer.parseInt(request.getParameter("id"));
            try (PreparedStatement preparedStatement =
                         conn.prepareStatement(DELETE_VOTE)) {
                preparedStatement.setInt(1, id);
                int result = preparedStatement.executeUpdate();
            } catch (Exception e) {
                System.out.println(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            getServletContext().getRequestDispatcher("/jspf/deletevote.jsp").forward(request, response);
        }
        doGet(request, response);
    }
}

