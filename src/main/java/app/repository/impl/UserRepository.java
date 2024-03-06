package app.repository.impl;

import app.config.HibernateUtil;
import app.domain.user.User;
import app.repository.AppRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserRepository implements AppRepository<User> {

    private static final Logger LOGGER =
            Logger.getLogger(UserRepository.class.getName());

    @Override
    public void create(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Транзакція стартує
            transaction = session.beginTransaction();
            String hql = "INSERT INTO User (firstName, lastName, email) " +
                    "VALUES (:firstName, :lastName, :email)";
            MutationQuery query = session.createMutationQuery(hql);
            query.setParameter("firstName", user.getFirstName());
            query.setParameter("lastName", user.getLastName());
            query.setParameter("email", user.getEmail());
            query.executeUpdate();
            // Транзакція виконується
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.log(Level.WARNING, e.getMessage(), e);
        }
    }

    @Override
    public Optional<List<User>> fetchAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction;
            // Транзакція стартує
            transaction = session.beginTransaction();
            List<User> list =
                    session.createQuery("FROM User", User.class).list();
            // Транзакція виконується
            transaction.commit();
            // Повертаємо Optional-контейнер з колецією даних
            return Optional.of(list);
        } catch (Exception e) {
            // Якщо помилка повертаємо порожній Optional-контейнер
            return Optional.empty();
        }
    }

    // ---- Path Param ----------------------

    @Override
    public Optional<User> fetchById(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Транзакція стартує
            transaction = session.beginTransaction();
            Query<User> query = session.createQuery("FROM User WHERE id = :id", User.class);
            query.setParameter("id", id);
            query.setMaxResults(1);
            User user = query.uniqueResult();
            // Транзакція виконується
            transaction.commit();
            // Повертаємо Optional-контейнер з об'єктом
            return Optional.of(user);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.log(Level.WARNING, e.getMessage(), e);
            // Якщо помилка або такого об'єкту немає в БД,
            // повертаємо порожній Optional-контейнер
            return Optional.empty();
        }
    }

    @Override
    public void update(Long id, User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Транзакция стартует
            transaction = session.beginTransaction();
            String hql = "UPDATE User SET firstName = :firstName," +
                    " lastName = :lastName, email = :email" +
                    " WHERE id = :id";
            MutationQuery query = session.createMutationQuery(hql);
            query.setParameter("firstName", user.getFirstName());
            query.setParameter("lastName", user.getLastName());
            query.setParameter("email", user.getEmail());
            query.setParameter("id", id);
            query.executeUpdate();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.log(Level.WARNING, e.getMessage(), e);
        }
    }

    @Override
    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Транзакція стартує
            transaction = session.beginTransaction();
            String hql = "DELETE FROM User WHERE id = :id";
            MutationQuery query = session.createMutationQuery(hql);
            query.setParameter("id", id);
            query.executeUpdate();
            // Транзакція виконується
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.log(Level.WARNING, e.getMessage(), e);
        }
    }


    // ---- Query Params ----------------------

    public Optional<List<User>> fetchByFirstName(String firstName) {
        String hql = "FROM User WHERE firstName = :firstName";
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction;
            // Транзакція стартує
            transaction = session.beginTransaction();
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("firstName", firstName);
            List<User> list = query.list();
            // Транзакція виконується
            transaction.commit();
            return Optional.of(list);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<List<User>> fetchByLastName(String lastName) {
        String hql = "FROM User WHERE lastName = :lastName";
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction;
            // Транзакція стартує
            transaction = session.beginTransaction();
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("lastName", lastName);
            List<User> list = query.list();
            // Транзакція виконується
            transaction.commit();
            return Optional.of(list);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<List<User>> fetchAllOrderBy(String orderBy) {
        String hql = "FROM User ORDER BY " + orderBy;
        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction;
            // Транзакція стартує
            transaction = session.beginTransaction();
            List<User> list =
                    session.createQuery(hql, User.class)
                            .list();
            // Транзакція виконується
            transaction.commit();
            return Optional.of(list);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<List<User>> fetchByLastNameOrderBy(String lastName, String orderBy) {
        String hql = "FROM User WHERE lastName = :lastName ORDER BY " + orderBy;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction;
            // Транзакція стартує
            transaction = session.beginTransaction();
            List<User> list = session.createQuery(hql, User.class)
                    .setParameter("lastName", lastName)
                    .list();
            // Транзакція виконується
            transaction.commit();
            return Optional.of(list);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<List<User>> fetchBetweenIds(Integer from, Integer to) {
        String hql = "FROM User u WHERE u.id BETWEEN :from AND :to";
        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction;
            // Транзакція стартує
            transaction = session.beginTransaction();
            List<User> list =
                    session.createQuery(hql, User.class)
                            .setParameter("from", from)
                            .setParameter("to", to)
                            .list();

            // Транзакція виконується
            transaction.commit();
            return Optional.of(list);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<List<User>> fetchLastNameIn(String lastName1, String lastName2) {
        String hql = "FROM User u WHERE u.lastName IN (:lastName1, :lastName2)";
        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction;
            // Транзакція стартує
            transaction = session.beginTransaction();
            List<User> list =
                    session.createQuery(hql, User.class)
                            .setParameter("lastName1", lastName1)
                            .setParameter("lastName2", lastName2)
                            .list();
            // Транзакція виконується
            transaction.commit();
            return Optional.of(list);
        } catch (Exception e) {
            return Optional.empty();
        }
    }


    // ---- Utils ----------------------

    public boolean isIdExists(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Перевірка наявності об'єкту за певним id
            User user = new User();
            user.setId(id);
            user = session.get(User.class, user.getId());

            if (user != null) {
                Query<User> query = session.createQuery("FROM User", User.class);
                query.setMaxResults(1);
                query.getResultList();
            }
            return user != null;
        }
    }

    public Optional<User> getLastUser() {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Транзакція стартує
            transaction = session.beginTransaction();
            Query<User> query = session.createQuery("FROM User ORDER BY id DESC", User.class);
            query.setMaxResults(1);
            User user = query.uniqueResult();
            // Транзакція виконується
            transaction.commit();
            return Optional.of(user);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.log(Level.WARNING, e.getMessage(), e);
            return Optional.empty();
        }
    }
}
