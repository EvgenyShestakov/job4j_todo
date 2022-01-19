package ru.job4j.todo.servlet;

import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.User;
import ru.job4j.todo.store.HbnStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

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
                          HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        String description = req.getParameter("name");
        String[] catIds = req.getParameterValues("catIds");
        if (!description.isEmpty() && catIds != null && catIds.length > 0) {
            User user = (User) req.getSession().getAttribute("user");
            Collection<Category> categories = HbnStore.instOf().findAllCategory();
            Item item = new Item(description, user);
            for (Category category : categories) {
                for (String catId: catIds) {
                    if (category.getId() == Integer.parseInt(catId)) {
                        item.addCategory(category);
                    }
                }
            }
            HbnStore.instOf().saveItem(item);
            resp.sendRedirect(req.getContextPath() + "/items.do");
        } else {
            resp.sendRedirect(req.getContextPath() + "/add.do");
        }
    }
}
