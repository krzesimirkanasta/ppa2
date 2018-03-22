package sample;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;

/**
 * Created by pwilkin on 30-Nov-17.
 */
public class DataContainer {

    private final Configuration config;
    private final SessionFactory sessionFactory;
    protected ObservableList<Student> students;
    protected ObservableList<Grupa> groups;

    public ObservableList<Student> getStudents() {
        return students;
    }

    public ObservableList<Grupa> getGroups() {
        return groups;
    }

    public DataContainer() {
        students = FXCollections.observableArrayList();
        groups = FXCollections.observableArrayList();
        config = new Configuration().configure("hibernate.cfg.xml");
        sessionFactory = config.buildSessionFactory();
        loadStudentsFromDatabase();
        loadGroupsFromDatabase();
    }

    private void loadGroupsFromDatabase() {
        try (Session ses = sessionFactory.openSession()) {
            ses.beginTransaction();
            Query<Grupa> query = ses.createQuery("from Grupa", Grupa.class);
            groups.addAll(query.list());
            ses.getTransaction().commit();
        } catch (HibernateException e) {
            Alert alert = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
        }
    }

    private void loadStudentsFromDatabase() {
        try (Session ses = sessionFactory.openSession()) {
            ses.beginTransaction();
            Query<Student> query = ses.createQuery("from Student", Student.class);
            students.addAll(query.list());
            ses.getTransaction().commit();
        } catch (HibernateException e) {
            Alert alert = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
        }
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
