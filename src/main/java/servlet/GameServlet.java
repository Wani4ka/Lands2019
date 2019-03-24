package servlet;

import launch.Main;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "GameServlet", urlPatterns = {"/game"})
public class GameServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect("/");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getParameter("name") == null) {
            resp.sendRedirect("/");
            return;
        }
        resp.setContentType("text/html");
        final PrintWriter out = resp.getWriter();
        try (BufferedReader r = new BufferedReader(new FileReader(Main.getContextFile(req, "game.html")))) {
            r.lines().map(i -> i.replace("$name$", req.getParameter("name"))).forEach(out::println);
        }
        resp.setStatus(200);
    }
}
