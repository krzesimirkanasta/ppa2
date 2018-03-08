package sample;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;

/**
 * Created by pwilkin on 30-Nov-17.
 */
public class DaneOsobowe implements HierarchicalController<MainController> {

    public TextField imie;
    public TextField nazwisko;
    public TextField pesel;
    public TextField indeks;
    public TableView<sample.Student> tabelka;
    private sample.MainController parentController;

    public void dodaj(ActionEvent actionEvent) {
        sample.Student st = new sample.Student();
        st.setName(imie.getText());
        st.setSurname(nazwisko.getText());
        st.setPesel(pesel.getText());
        st.setIdx(indeks.getText());
        dodajDoBazy(st);
        tabelka.getItems().add(st);
    }

    private void dodajDoBazy(Student st) {
        try (Connection c = DriverManager.getConnection("jdbc:hsqldb:file:testdb", "SA", "")) {
            ResultSet res = c.getMetaData().getTables(null, null, "STUDENTS", null);
            if (!res.first()) { // nie ma tabeli STUDENTS
                c.createStatement().execute("CREATE TABLE STUDENTS " +
                    "(ID INT PRIMARY KEY IDENTITY, " +
                    "NAME VARCHAR(255)," +
                    "SURNAME VARCHAR(255)," +
                    "IDX VARCHAR(10), " +
                    "PESEL VARCHAR(11), " +
                    "GRADE DOUBLE, " +
                    "GRADE_DETAILED VARCHAR(1000))");
            }
            /* Tak nie robimy: Statement s = c.createStatement();
            s.executeQuery("INSERT INTO STUDENTS (NAME, SURNAME, IDX, PESEL, GRADE, GRADE_DETAILED) VALUES (" +
                "'" + st.getName() + "', ... itd.") */
            PreparedStatement ps = c.prepareStatement("INSERT INTO STUDENTS (NAME, SURNAME, IDX, PESEL, GRADE, GRADE_DETAILED) " +
                "VALUES (?, ?, ?, ?, ?, ?)");
            ps.setString(1, st.getName());
            ps.setString(2, st.getSurname());
            ps.setString(3, st.getIdx());
            ps.setString(4, st.getPesel());
            ps.setObject(5, st.getGrade());
            ps.setString(6, st.getGradeDetailed());
            ps.execute();
            ResultSet gk = c.createStatement().executeQuery("CALL IDENTITY()");
            gk.next();
            st.setId(gk.getInt(1));
        } catch (SQLException e) {
            Alert alert = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
        }
    }

    public void setParentController(sample.MainController parentController) {
        this.parentController = parentController;
        //tabelka.getItems().addAll(parentController.getDataContainer().getStudents());
        tabelka.setItems(parentController.getDataContainer().getStudents());
    }

    public void usunZmiany() {
        tabelka.getItems().clear();
        tabelka.getItems().addAll(parentController.getDataContainer().getStudents());
    }

    public sample.MainController getParentController() {
        return parentController;
    }

    public void initialize() {
        for (TableColumn<sample.Student, ?> studentTableColumn : tabelka.getColumns()) {
            if ("id".equals(studentTableColumn.getId())) {
                studentTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            } else if ("imie".equals(studentTableColumn.getId())) {
                studentTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            } else if ("nazwisko".equals(studentTableColumn.getId())) {
                studentTableColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
            } else if ("pesel".equals(studentTableColumn.getId())) {
                studentTableColumn.setCellValueFactory(new PropertyValueFactory<>("pesel"));
            } else if ("indeks".equals(studentTableColumn.getId())) {
                studentTableColumn.setCellValueFactory(new PropertyValueFactory<>("idx"));
            }
        }

    }

    public void zapisz(ActionEvent actionEvent) {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("Studenci");
        int row = 0;
        for (sample.Student student : tabelka.getItems()) {
            XSSFRow r = sheet.createRow(row);
            r.createCell(0).setCellValue(student.getName());
            r.createCell(1).setCellValue(student.getSurname());
            if (student.getGrade() != null) {
                r.createCell(2).setCellValue(student.getGrade());
            }
            r.createCell(3).setCellValue(student.getGradeDetailed());
            r.createCell(4).setCellValue(student.getIdx());
            r.createCell(5).setCellValue(student.getPesel());
            row++;
        }
        try (FileOutputStream fos = new FileOutputStream("data.xlsx")) {
            wb.write(fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Uwaga na serializację: https://sekurak.pl/java-vs-deserializacja-niezaufanych-danych-i-zdalne-wykonanie-kodu-czesc-i/ */
    public void wczytaj(ActionEvent actionEvent) {
        ArrayList<sample.Student> studentsList = new ArrayList<>();
        try (FileInputStream ois = new FileInputStream("data.xlsx")) {
            XSSFWorkbook wb = new XSSFWorkbook(ois);
            XSSFSheet sheet = wb.getSheet("Studenci");
            for (int i = 0; i <= sheet.getLastRowNum(); i++) {
                XSSFRow row = sheet.getRow(i);
                sample.Student student = new sample.Student();
                student.setName(row.getCell(0).getStringCellValue());
                student.setSurname(row.getCell(1).getStringCellValue());
                if (row.getCell(2) != null) {
                    student.setGrade(row.getCell(2).getNumericCellValue());
                }
                student.setGradeDetailed(row.getCell(3).getStringCellValue());
                student.setIdx(row.getCell(4).getStringCellValue());
                student.setPesel(row.getCell(5).getStringCellValue());
                studentsList.add(student);
            }
            tabelka.getItems().clear();
            tabelka.getItems().addAll(studentsList);
            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
