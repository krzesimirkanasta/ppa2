package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

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
        loadStudentsFromDatabase();
        loadGroupsFromDatabase();
    }

    private void loadGroupsFromDatabase() {
        try (Connection c = DriverManager.getConnection("jdbc:hsqldb:file:testdb", "SA", "")) {
            ResultSet res = c.getMetaData().getTables(null, null, "GROUPS", null);
            if (!res.first()) { // nie ma tabeli STUDENTS
                c.createStatement().execute("CREATE TABLE GROUPS " +
                        "(ID INT PRIMARY KEY IDENTITY, " +
                        "NAME VARCHAR(255)," +
                        "DESCRIPTION VARCHAR(1000))");
            }
            /* Tak nie robimy: Statement s = c.createStatement();
            s.executeQuery("INSERT INTO STUDENTS (NAME, SURNAME, IDX, PESEL, GRADE, GRADE_DETAILED) VALUES (" +
                "'" + st.getName() + "', ... itd.") */
            ResultSet groupResults = c.createStatement().executeQuery("SELECT * FROM GROUPS ORDER BY NAME ASC");
            while (groupResults.next()) {
                Grupa gr = new Grupa();
                gr.setId(groupResults.getInt("ID"));
                gr.setName(groupResults.getString("NAME"));
                gr.setDescription(groupResults.getString("DESCRIPTION"));
                groups.add(gr);
            }
        } catch (SQLException e) {
            Alert alert = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
        }
    }

    private void loadStudentsFromDatabase() {
        try (Connection c = DriverManager.getConnection("jdbc:hsqldb:file:testdb", "SA", "")) {
            ResultSet res = c.getMetaData().getTables(null, null, "STUDENTS", null);
            if (!res.first()) { // nie ma tabeli STUDENTS
                c.createStatement().execute("CREATE TABLE STUDENTS " +
                        "(ID INT PRIMARY KEY IDENTITY, " +
                        "NAME VARCHAR(255)," +
                        "SURNAME VARCHAR(255)," +
                        "IDX VARCHAR(10), " +
                        "PESEL VARCHAR(11))");
            }
            /* Tak nie robimy: Statement s = c.createStatement();
            s.executeQuery("INSERT INTO STUDENTS (NAME, SURNAME, IDX, PESEL, GRADE, GRADE_DETAILED) VALUES (" +
                "'" + st.getName() + "', ... itd.") */
            ResultSet studentResults = c.createStatement().executeQuery("SELECT * FROM STUDENTS ORDER BY SURNAME ASC, NAME ASC");
            while (studentResults.next()) {
                Student st = new Student();
                st.setId(studentResults.getInt("ID"));
                st.setName(studentResults.getString("NAME"));
                st.setSurname(studentResults.getString("SURNAME"));
                st.setIdx(studentResults.getString("IDX"));
                st.setPesel(studentResults.getString("PESEL"));
                students.add(st);
            }
        } catch (SQLException e) {
            Alert alert = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
        }
    }
}
