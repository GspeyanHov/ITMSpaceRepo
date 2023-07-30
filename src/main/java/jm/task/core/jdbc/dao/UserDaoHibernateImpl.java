package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.tool.hbm2ddl.SchemaExport;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static org.hibernate.tool.schema.TargetType.DATABASE;

public class UserDaoHibernateImpl implements UserDao {
    private static final SessionFactory SESSION = Util.getSessionFactory();
    public UserDaoHibernateImpl() {

    }
    @Override
    public void createUsersTable() {
        try (Session openedSession = SESSION.openSession()) {
            openedSession.beginTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void dropUsersTable() {
        try (Session openedSession = SESSION.openSession()) {
            StandardServiceRegistry registry = openedSession.getSessionFactory().getSessionFactoryOptions().getServiceRegistry();
            Metadata metadata = new MetadataSources(registry)
                    .addAnnotatedClass(User.class)
                    .buildMetadata();
            SchemaExport schema = new SchemaExport();
            schema.drop(EnumSet.of(DATABASE), metadata);
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void saveUser(String name, String lastName, byte age) {
        if (name == null || lastName == null || (age < 0 || age > 90)) {
            throw new RuntimeException("Bad credentials, please check! ");
        }
        try (Session openedSession = SESSION.openSession()) {
            User user = new User();
            user.setName(name);
            user.setLastName(lastName);
            user.setAge(age);
            openedSession.save(user);
            openedSession.beginTransaction().commit();
            System.out.println("User с именем " + name + " добавлен в базу данных");
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void removeUserById(long id) {
        try (Session openedSession = SESSION.openSession()) {
            User user = openedSession.get(User.class, id);
            if (id <= 0 || user == null) {
                throw new RuntimeException("id must be bigger than 0! ");
            }
            openedSession.delete(user);
            openedSession.beginTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }
    @Override
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        try (Session openedSession = SESSION.openSession()) {
            CriteriaQuery<User> query = openedSession.getCriteriaBuilder().createQuery(User.class);
            Root<User> root = query.from(User.class);
            query.select(root);
            userList = openedSession.createQuery(query).getResultList();
            openedSession.beginTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return userList;
    }
    @Override
    public void cleanUsersTable() {
        try (Session openedSession = SESSION.openSession()) {
            List<User> users = openedSession.createQuery("FROM User ", User.class).list();
            if (users.isEmpty()) {
                throw new RuntimeException("Nothing to delete!, Table has already been empty ");
            }
            for (User user : users) {
                openedSession.delete(user);
            }
            openedSession.beginTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }
}
