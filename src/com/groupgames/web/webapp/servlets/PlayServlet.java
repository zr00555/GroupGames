package com.groupgames.web.webapp.servlets;

import com.groupgames.web.core.Player;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "PlayServlet", urlPatterns = "/game/play")
public class PlayServlet extends ServletTemplate {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession playerSession = request.getSession();
        PrintWriter out = response.getWriter();
        String gameCode = request.getParameter("gameCode");
        String username = request.getParameter("username");

        if(gameCode.length() != 0 && username.length() != 0) {
            out.print("Code: " + gameCode + "\nUsername: " + username);
            Player player = new Player(playerSession.getId(), username, gameCode);
            System.out.println("PLayer session ID: " + player.getUserID());
        } else {
            out.print("Enter both values");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}