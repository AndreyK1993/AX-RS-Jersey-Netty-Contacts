package app.repository.impl;

import app.config.HibernateUtil;
import app.domain.product.Product;
import app.repository.AppRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductRepository implements AppRepository<Product> {

    private static final Logger LOGGER =
            Logger.getLogger(ProductRepository.class.getName());

    @Override
    public void create(Product product) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Транзакція стартує
            transaction = session.beginTransaction();
            String hql = "INSERT INTO Product (productName, measure, quota, price) " +
                    "VALUES (:productName, :measure, :quota, :price)";
            MutationQuery query = session.createMutationQuery(hql);
            query.setParameter("productName", product.getProductName());
            query.setParameter("measure", product.getMeasure());
            query.setParameter("quota", product.getQuota());
            query.setParameter("price", product.getPrice());
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
    public Optional<List<Product>> fetchAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction;
            // Транзакція стартує
            transaction = session.beginTransaction();
            List<Product> list =
                    session.createQuery("FROM Product", Product.class).list();
            // Транзакція виконується
            transaction.commit();
            // Повертаємо Optional-контейнер з колецією даних
            return Optional.of(list);
        } catch (Exception e) {
            // Якщо помилка повертаємо порожній Optional-контейнер
            return Optional.empty();
        }
    }

    @Override
    public Optional<Product> fetchById(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Транзакція стартує
            transaction = session.beginTransaction();
            Query<Product> query = session.createQuery("FROM Product WHERE id = :id", Product.class);
            query.setParameter("id", id);
            query.setMaxResults(1);
            Product product = query.uniqueResult();
            // Транзакція виконується
            transaction.commit();
            // Повертаємо Optional-контейнер з об'єктом
            return Optional.of(product);
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
    public void update(Long id, Product product) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Транзакция стартует
            transaction = session.beginTransaction();
            String hql = "UPDATE Product SET productName = :productName," +
                    " measure = :measure, quota = :quota, price = :price" +
                    " WHERE id = :id";
            MutationQuery query = session.createMutationQuery(hql);
            query.setParameter("productName", product.getProductName());
            query.setParameter("measure", product.getMeasure());
            query.setParameter("quota", product.getQuota());
            query.setParameter("price", product.getPrice());
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
            String hql = "DELETE FROM Product WHERE id = :id";
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

    // ---- Utils ----------------------

    public boolean isIdExists(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Перевірка наявності об'єкту за певним id
            Product product = new Product();
            product.setId(id);
            product = session.get(Product.class, product.getId());

            if (product != null) {
                Query<Product> query = session.createQuery("FROM Product", Product.class);
                query.setMaxResults(1);
                query.getResultList();
            }
            return product != null;
        }
    }

    public Optional<Product> getLastProduct() {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Транзакція стартує
            transaction = session.beginTransaction();
            Query<Product> query = session.createQuery("FROM Product ORDER BY id DESC", Product.class);
            query.setMaxResults(1);
            Product product = query.uniqueResult();
            // Транзакція виконується
            transaction.commit();
            return Optional.of(product);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.log(Level.WARNING, e.getMessage(), e);
            return Optional.empty();
        }
    }
}
