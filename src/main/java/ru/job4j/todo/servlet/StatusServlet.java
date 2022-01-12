package ru.job4j.todo.servlet;
import ru.job4j.todo.store.HbnStore;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class StatusServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp) throws IOException {
        String checkbox = req.getParameter("checkbox");
        if (checkbox != null) {
            req.getSession().setAttribute("checkbox", true);
        } else {
            req.getSession().setAttribute("checkbox", false);
        }
        resp.sendRedirect(req.getContextPath() + "/items.do");
    }

    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");
        String done = req.getParameter("done");
        HbnStore.instOf().updateItem(Integer.parseInt(id), Boolean.parseBoolean(done));
        resp.sendRedirect(req.getContextPath() + "/items.do");
    }
}
