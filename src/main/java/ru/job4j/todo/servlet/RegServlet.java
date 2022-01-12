package ru.job4j.todo.servlet;

import ru.job4j.todo.model.User;
import ru.job4j.todo.store.HbnStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp) throws IOException, ServletException {
        req.getRequestDispatcher("reg.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp) throws IOException, ServletException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        if (HbnStore.instOf().findUserByEmail(email) != null) {
            req.setAttribute("error", "Пользователь с таким email уже существует");
            req.getRequestDispatcher("reg.jsp").forward(req, resp);
        } else {
            HbnStore.instOf().saveUser(new User(name, email, password));
            req.setAttribute("fine", "Пользователь зарегистрирован");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }
}
