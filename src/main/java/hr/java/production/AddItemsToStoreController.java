package hr.java.production;

import hr.java.production.exception.ValidationException;
import hr.java.production.model.Factory;
import hr.java.production.model.Item;
import hr.java.production.model.Store;
import hr.java.production.utility.DatabaseUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AddItemsToStoreController {

    @FXML
    private ListView<Item> itemsListView;
    @FXML
    private ComboBox<Store> storesComboBox;

    private List<Store> storesList;

    public void initialize(){
        itemsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        storesList = DatabaseUtil.getStores();
        ObservableList<Store> storesObservableList = FXCollections.observableList(storesList);
        storesComboBox.setItems(storesObservableList);
    }


    public void addItemsToStore(ActionEvent actionEvent) {

        try {
            validateInputFields();

            List<Item> itemsToAdd = itemsListView.getSelectionModel().getSelectedItems();
            DatabaseUtil.addItemsToStore(storesComboBox.getValue(), itemsToAdd);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Spremanje uspješno");
            alert.setHeaderText("Spremanje novih artikala u tvornicu je bilo uspješno!");

            StringBuilder contentText = new StringBuilder("U trgovinu ")
                    .append(storesComboBox.getValue().getName())
                    .append(" spremili su se sljedeći artikli:\n");

            for (Item item : itemsToAdd) {
                contentText.append("- ").append(item.getName()).append("\n");
            }

            alert.setContentText(contentText.toString());

            reset();
            alert.showAndWait();


        }catch (ValidationException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greška pri unosu");
            alert.setHeaderText("Provjerite ispravnost unesenih podataka");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }

    }

    public void reset(ActionEvent actionEvent) {

        storesComboBox.setValue(null);
        itemsListView.setItems(null);
    }

    private void reset() {
        storesComboBox.setValue(null);
        itemsListView.setItems(null);
    }

    public void showAvailableItems(ActionEvent actionEvent) {

        if (storesComboBox.getValue() != null){
            List<Item> availableItems = DatabaseUtil.getItemsNotInStore(storesComboBox.getValue().getId(), DatabaseUtil.getItems())
                    .stream()
                    .toList();

            ObservableList<Item> observableListOfAvailableItems = FXCollections.observableList(availableItems);
            itemsListView.setItems(observableListOfAvailableItems);

        }


    }


    private void validateInputFields() throws ValidationException {
        List<String> errors = new ArrayList<>();

        if (Optional.ofNullable(storesComboBox.getValue()).isEmpty()){
            errors.add("Odaberite trgovinu");
        }

        if(itemsListView.getSelectionModel().getSelectedItems().isEmpty()){
            errors.add("Odaberite barem jedan artikl koji želite dodati");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(String.join("\n", errors));
        }


    }
}
