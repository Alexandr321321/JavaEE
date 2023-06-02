package controller;

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

@WebServlet(name = "VoteJsonServlet", value = "/votesjson")
public class VoteJsonServlet extends HttpServlet {
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
                    "jdbc:postgresql://localhost:5432/postgres",
                    "postgres", "9i%OqhnIZTVN"
            );
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, title, datestart, datefinish, status FROM public.vote ORDER BY id ASC");
            votes.clear();
           // JsonObjectBuilder jsonObject = Json.createObjectBuilder();
            //JsonArrayBuilder jsonArray = Json.createArrayBuilder();
            while (rs.next()) {
                votes.add(new Vote(rs.getInt("id"),
                        rs.getString("title"),
                        rs.getDate("dateStart"),
                        rs.getDate("dateFinish"),
                        rs.getBoolean("status")
                ));
               // jsonObject.add("id", rs.getInt("id"));
               // jsonObject.add("title", rs.getString("title"));
               // jsonObject.add("dateStart", String.valueOf(rs.getDate("dateStart")));
               // jsonObject.add("dateFinish", String.valueOf(rs.getDate("dateFinish")));
                //jsonObject.add("status", rs.getBoolean("status"));
                //jsonArray.add(jsonObject);
            }
            stmt.close();
            request.setAttribute("votes", votes);
            //request.setAttribute("votesjson", jsonArray.build());

            PrintWriter writer = response.getWriter();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String votesjson = gson.toJson(votes);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            writer.print(votesjson);
            writer.flush(); //flush data to file   <---
            writer.close(); //close write

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
