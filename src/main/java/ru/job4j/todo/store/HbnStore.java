package ru.job4j.todo.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import ru.job4j.todo.model.Item;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class HbnStore implements Store, AutoCloseable {
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

    private <T> T tx(final Function<Session, T> command) {
        final Session session = sf.openSession();
        final Transaction tx = session.beginTransaction();
        try {
            T rsl = command.apply(session);
            tx.commit();
            return rsl;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public Collection<Item> findAllItems() {
        List<Item> result = tx(session -> session.createQuery("from ru.job4j."
                + "todo.model.Item").getResultList());
        Collections.sort(result);
        return result;
    }

    @Override
    public Collection<Item> findNotDoneItems() {
        return tx(session -> session.createQuery("from ru.job4j.todo.model.Item where "
                + "done = false").getResultList());
    }

    @Override
    public void saveItem(Item item) {
        tx(session -> session.save(item));
    }

    @Override
    public boolean updateItem(int id, boolean done) {
        return tx(session -> {
            Query<Item> query =  session.createQuery("update ru.job4j.todo."
                + "model." + "Item set done = :doneParam where id = :idParam");
            query.setParameter("doneParam", !done);
            query.setParameter("idParam", id);
            return query.executeUpdate() > 0;
        });
    }

    @Override
    public Item findItemById(int id) {
        return tx(session -> session.get(Item.class, id));
    }

    @Override
    public void close() throws Exception {
        StandardServiceRegistryBuilder.destroy(registry);
    }
}
