package ru.job4j.todo.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import ru.job4j.todo.model.Item;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class HbnStore implements Store, AutoCloseable {
    private static final Store INST = new HbnStore();
    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    private HbnStore() {

    }

    private static final class Lazy {
        private static final Store INST = new HbnStore();
    }

    public static Store instOf() {
        return Lazy.INST;
    }

    @Override
    public Collection<Item> findAllItems() {
        Session session = sf.openSession();
        session.beginTransaction();
        List<Item> result = session.createQuery("from ru.job4j.todo.model.Item").getResultList();
        Collections.sort(result);
        session.getTransaction().commit();
        session.close();
        return result;
    }

    @Override
    public Collection<Item> findNotDoneItems() {
        Session session = sf.openSession();
        session.beginTransaction();
        List<Item> result = session.createQuery("from ru.job4j.todo.model.Item where "
                + "done = false").getResultList();
        Collections.sort(result);
        session.getTransaction().commit();
        session.close();
        return result;
    }

    @Override
    public void saveItem(Item item) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.save(item);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public boolean updateItem(int id, boolean done) {
        Session session = sf.openSession();
        session.beginTransaction();
        Query<Item> query = session.createQuery("update ru.job4j.todo.model."
                + "Item set done = :doneParam where id = :idParam");
        query.setParameter("doneParam", !done);
        query.setParameter("idParam", id);
        boolean flag = query.executeUpdate() > 0;
        session.getTransaction().commit();
        session.close();
        return flag;
    }

    @Override
    public Item findItemById(int id) {
        Session session = sf.openSession();
        session.beginTransaction();
        Item result = session.get(Item.class, id);
        session.getTransaction().commit();
        session.close();
        return result;
    }

    @Override
    public void close() throws Exception {
        StandardServiceRegistryBuilder.destroy(registry);
    }

}
