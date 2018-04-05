package sample;

import org.hibernate.HibernateException;
import org.hibernate.Session;

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
    //public ComboBox<Grupa> grupa;
    private sample.MainController parentController;

    public void dodaj(ActionEvent actionEvent) {
        sample.Student st = new sample.Student();
        st.setName(imie.getText());
        st.setSurname(nazwisko.getText());
        st.setPesel(pesel.getText());
        st.setIdx(indeks.getText());
        //st.setGroup(grupa.getValue());
        dodajDoBazy(st);
        tabelka.getItems().add(st);
    }

    private void dodajDoBazy(Student st) {
        try (Session ses = parentController.getDataContainer().getSessionFactory().openSession()) {
            ses.beginTransaction();
            ses.persist(st);
            ses.getTransaction().commit();
        } catch (HibernateException e) {
            Alert alert = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
        }
    }

    public void setParentController(sample.MainController parentController) {
        this.parentController = parentController;
        //tabelka.getItems().addAll(parentController.getDataContainer().getStudents());
        tabelka.setItems(parentController.getDataContainer().getStudents());
        /*grupa.getItems().addAll(getParentController().getDataContainer().getGroups());
        grupa.setCellFactory(lv -> {
            return new ListCell<Grupa>() {
                @Override
                protected void updateItem(Grupa item, boolean empty) {
                    super.updateItem(item, empty);
                    //setText(empty ? "" : item.getName());
                    Rectangle rect = new Rectangle(200, 10, item == null ? null : new LinearGradient(0, 0, 1, 0,
                        true, CycleMethod.REPEAT,
                        new Stop(0.0, Color.rgb(item.getName().charAt(0) * 7 % 255, item.getName().charAt(0) * 11 % 255,
                            item.getName().charAt(0) * 13 % 255)),
                        new Stop(1.0, Color.rgb(item.getName().charAt(1) * 7 % 255, item.getName().charAt(1) * 11 % 255,
                                item.getName().charAt(1) * 13 % 255))));
                    setGraphic(rect);
                }
            };
        });*/
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
            } /*else if ("grupa".equals(studentTableColumn.getId())) {
                studentTableColumn.setCellValueFactory(new PropertyValueFactory<>("group"));
            }*/
        }
    }

}
