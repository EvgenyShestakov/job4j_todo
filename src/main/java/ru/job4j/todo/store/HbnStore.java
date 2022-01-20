package ru.job4j.todo.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
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

    private void txv(final Consumer<Session> command) {
        final Session session = sf.openSession();
        final Transaction tx = session.beginTransaction();
        try {
            command.accept(session);
            tx.commit();
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public Collection<Item> findAllItems(User user) {
        return tx(session -> {
            Query<Item> query = session.createQuery("select distinct i from Item i join fetch i."
                    + "categories where i.user = :userParam");
            query.setParameter("userParam", user);
            List<Item> result = query.getResultList();
            Collections.sort(result);
            return result;
        });
    }

    @Override
    public Collection<Item> findNotDoneItems(User user) {
        return tx(session -> {
            Query<Item> query = session.createQuery("select distinct i from Item i join fetch i."
                    + "categories where i.done = false and i.user = :userParam");
            query.setParameter("userParam", user);
            List<Item> result = query.getResultList();
            Collections.sort(result);
            return result;
        });
    }

    @Override
    public Collection<Category> findAllCategory() {
        return tx(session -> session.createQuery("from ru.job4j.todo.model.Category").list());
    }

    @Override
    public void saveItem(Item item, String[] catIds) {
        txv(session -> {
            for (String id : catIds) {
                Category category = session.find(Category.class, Integer.parseInt(id));
                item.addCategory(category);
            }
            session.save(item);
        });
    }

    @Override
    public void saveUser(User user) {
        txv(session -> session.save(user));
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
    public User findUserByEmail(String email) {
        return tx(session -> {
            Query<User> query = session.createQuery("from ru.job4j.todo."
                    + "model.User where email = :paramEmail");
            query.setParameter("paramEmail", email);
            return query.uniqueResult();
        });
    }

    @Override
    public void close() throws Exception {
        StandardServiceRegistryBuilder.destroy(registry);
    }
}
