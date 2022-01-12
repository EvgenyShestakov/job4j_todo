package ru.job4j.todo.servlet;

import org.hibernate.Session;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.User;
import ru.job4j.todo.store.HbnStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;

public class ItemServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp) throws IOException,
            ServletException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        boolean checkbox = (boolean) session.getAttribute("checkbox");
        Collection<Item> items;
        if (checkbox) {
            items = HbnStore.instOf().findAllItems(user);
        } else {
            items = HbnStore.instOf().findNotDoneItems(user);
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
            User user = (User) req.getSession().getAttribute("user");
            HbnStore.instOf().saveItem(new Item(description, user));
            resp.sendRedirect(req.getContextPath() + "/items.do");
        } else {
            req.getRequestDispatcher("/addtask.jsp").forward(req, resp);
        }
    }
}
