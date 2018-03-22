package sample;

import org.hibernate.HibernateException;
import org.hibernate.Session;

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
        try (Session ses = parentController.getDataContainer().getSessionFactory().openSession()) {
            ses.beginTransaction();
            ses.persist(gr);
            ses.getTransaction().commit();
        } catch (HibernateException e) {
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
                    updateDatabaseValue(editedGroup);
                });
            } else if ("opis".equals(groupTableColumn.getId())) {
                TableColumn<Grupa, String> opisColumn = (TableColumn<Grupa, String>) groupTableColumn;
                opisColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
                opisColumn.setCellFactory(TextFieldTableCell.forTableColumn());
                opisColumn.setOnEditCommit((val) -> {
                    Grupa editedGroup = val.getTableView().getItems().get(val.getTablePosition().getRow());
                    editedGroup.setDescription(val.getNewValue());
                    updateDatabaseValue(editedGroup);
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
        try (Session ses = parentController.getDataContainer().getSessionFactory().openSession()) {
            ses.beginTransaction();
            ses.delete(p);
            ses.getTransaction().commit();
        } catch (HibernateException e) {
            Alert alert = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
        }
    }

    private void updateDatabaseValue(Grupa editedGroup) {
        try (Session ses = parentController.getDataContainer().getSessionFactory().openSession()) {
            ses.beginTransaction();
            ses.merge(editedGroup);
            ses.getTransaction().commit();
        } catch (HibernateException e) {
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
