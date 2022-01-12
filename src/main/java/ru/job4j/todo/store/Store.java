package ru.job4j.todo.store;

import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.User;

import java.util.Collection;

public interface Store {
    Collection<Item> findAllItems(User user);

    Collection<Item> findNotDoneItems(User user);

    void saveItem(Item item);

    void saveUser(User user);

    boolean updateItem(int id, boolean done);

    User findUserByEmail(String email);
}
