package ru.job4j.todo.servlet;

import ru.job4j.todo.model.Category;
import ru.job4j.todo.store.HbnStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

public class AddTaskServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp) throws IOException, ServletException {
        Collection<Category> categories = HbnStore.instOf().findAllCategory();
        req.setAttribute("categories", categories);
        req.getRequestDispatcher("/addtask.jsp").forward(req, resp);
    }
}
