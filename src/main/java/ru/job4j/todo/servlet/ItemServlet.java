package ru.job4j.todo.servlet;

import ru.job4j.todo.model.Item;
import ru.job4j.todo.store.HbnStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

public class ItemServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp) throws IOException,
            ServletException {
        boolean checkbox = (boolean) req.getSession().getAttribute("checkbox");
        Collection<Item> items;
        if (checkbox) {
            items = HbnStore.instOf().findAllItems();
        } else {
            items = HbnStore.instOf().findNotDoneItems();
        }
        req.setAttribute("items", items);
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp) throws IOException,
            ServletException {
        req.setCharacterEncoding("UTF-8");
        String description = req.getParameter("name");
        if (!description.isEmpty()) {
            HbnStore.instOf().saveItem(new Item(description));
            resp.sendRedirect(req.getContextPath() + "/items");
        } else {
            req.getRequestDispatcher("/addtask.jsp").forward(req, resp);
        }
    }
}
