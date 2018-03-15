package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;

/**
 * Created by pwilkin on 30-Nov-17.
 */
public class Grupy implements HierarchicalController<MainController> {

    public TextField nazwa;
    public TextField opis;
    public TableView<Grupa> tabelka;
    private MainController parentController;

    public void dodaj(ActionEvent actionEvent) {
        Grupa st = new Grupa();
        st.setName(nazwa.getText());
        st.setDescription(opis.getText());
        dodajDoBazy(st);
        tabelka.getItems().add(st);
    }

    private void dodajDoBazy(Grupa gr) {
        try (Connection c = DriverManager.getConnection("jdbc:hsqldb:file:testdb", "SA", "")) {
            PreparedStatement ps = c.prepareStatement("INSERT INTO GROUPS (NAME, DESCRIPTION) " +
                    "VALUES (?, ?)");
            ps.setString(1, gr.getName());
            ps.setString(2, gr.getDescription());
            ps.execute();
            ResultSet gk = c.createStatement().executeQuery("CALL IDENTITY()");
            gk.next();
            gr.setId(gk.getInt(1));
        } catch (SQLException e) {
            Alert alert = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
        }
    }

    public void setParentController(MainController parentController) {
        this.parentController = parentController;
        //tabelka.getItems().addAll(parentController.getDataContainer().getStudents());
        tabelka.setEditable(true);
        tabelka.setItems(parentController.getDataContainer().getGroups());
    }

    public MainController getParentController() {
        return parentController;
    }

    public void initialize() {
        for (TableColumn<Grupa, ?> groupTableColumn : tabelka.getColumns()) {
            if ("nazwa".equals(groupTableColumn.getId())) {
                TableColumn<Grupa, String> nazwaColumn = (TableColumn<Grupa, String>) groupTableColumn;
                nazwaColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                nazwaColumn.setCellFactory(TextFieldTableCell.forTableColumn());
                nazwaColumn.setOnEditCommit((val) -> {
                    Grupa editedGroup = val.getTableView().getItems().get(val.getTablePosition().getRow());
                    editedGroup.setName(val.getNewValue());
                    updateDatabaseValue(editedGroup, "NAME", val.getNewValue());
                });
            } else if ("opis".equals(groupTableColumn.getId())) {
                TableColumn<Grupa, String> opisColumn = (TableColumn<Grupa, String>) groupTableColumn;
                opisColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
                opisColumn.setCellFactory(TextFieldTableCell.forTableColumn());
                opisColumn.setOnEditCommit((val) -> {
                    Grupa editedGroup = val.getTableView().getItems().get(val.getTablePosition().getRow());
                    editedGroup.setDescription(val.getNewValue());
                    updateDatabaseValue(editedGroup, "DESCRIPTION", val.getNewValue());
                });
            } else if ("usun".equals(groupTableColumn.getId())) {
                TableColumn<Grupa, Button> usunColumn = (TableColumn<Grupa, Button>) groupTableColumn;
                usunColumn.setCellFactory(ActionButtonTableCell.<Grupa>forTableColumn("Usuń", (Grupa p) -> {
                    tabelka.getItems().remove(p);
                    deleteDatabaseValue(p);
                    return p;
                }));

            }
        }

    }

    private void deleteDatabaseValue(Grupa p) {
        try (Connection c = DriverManager.getConnection("jdbc:hsqldb:file:testdb", "SA", "")) {
            PreparedStatement ps = c.prepareStatement("DELETE FROM GROUPS WHERE ID = ?");
            ps.setInt(1, p.getId());
            ps.execute();
        } catch (SQLException e) {
            Alert alert = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
        }
    }

    private void updateDatabaseValue(Grupa editedGroup, String column, String newValue) {
        try (Connection c = DriverManager.getConnection("jdbc:hsqldb:file:testdb", "SA", "")) {
            PreparedStatement ps = c.prepareStatement("UPDATE GROUPS SET " + column + " = ? WHERE ID = ?");
            ps.setString(1, newValue);
            ps.setInt(2, editedGroup.getId());
            ps.execute();
        } catch (SQLException e) {
            Alert alert = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
        }
    }

    public void dodajJesliEnter(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            dodaj(new ActionEvent(keyEvent.getSource(), keyEvent.getTarget()));
        }
    }
}
