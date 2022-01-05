package ru.job4j.todo.store;

import ru.job4j.todo.model.Item;

import java.util.Collection;

public interface Store {
    Collection<Item> findAllItems();

    Collection<Item> findNotDoneItems();

    void saveItem(Item item);

    boolean updateItem(int id, boolean done);

    Item findItemById(int id);
}
