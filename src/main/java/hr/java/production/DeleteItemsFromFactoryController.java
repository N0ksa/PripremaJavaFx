package hr.java.production;

import hr.java.production.exception.ValidationException;
import hr.java.production.model.Factory;
import hr.java.production.model.Item;
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

public class DeleteItemsFromFactoryController {

    @FXML
    private ComboBox<Factory> factoriesComboBox;
    @FXML
    private ListView<Item> itemsListView;

    List<Factory> factoryList;

    public void initialize(){
        itemsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        factoryList = DatabaseUtil.getFactories();
        ObservableList<Factory> factoryObservableList = FXCollections.observableList(factoryList);
        factoriesComboBox.setItems(factoryObservableList);
    }


    public void deleteItemsFromFactory(ActionEvent actionEvent) {

        try {
            validateInputFields();

            List<Item> itemsToAdd = itemsListView.getSelectionModel().getSelectedItems();
            DatabaseUtil.deleteItemsFromFactory(factoriesComboBox.getValue(), itemsToAdd);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Spremanje uspješno");
            alert.setHeaderText("Brisanje artikala iz tvornice je bilo uspješno!");

            StringBuilder contentText = new StringBuilder("Iz tvornice ")
                    .append(factoriesComboBox.getValue().getName())
                    .append(" obrisali su se sljedeći artikli:\n");

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

        factoriesComboBox.setValue(null);
        itemsListView.setItems(null);
    }

    private void reset() {
        factoriesComboBox.setValue(null);
        itemsListView.setItems(null);
    }

    public void showAvailableItems(ActionEvent actionEvent) {

        if (factoriesComboBox.getValue() != null){
            List<Item> availableItems = DatabaseUtil.getItemsForFactory(factoriesComboBox.getValue().getId(), DatabaseUtil.getItems())
                    .stream()
                    .toList();

            ObservableList<Item> observableListOfAvailableItems = FXCollections.observableList(availableItems);
            itemsListView.setItems(observableListOfAvailableItems);

        }


    }


    private void validateInputFields() throws ValidationException {
        List<String> errors = new ArrayList<>();

        if (Optional.ofNullable(factoriesComboBox.getValue()).isEmpty()){
            errors.add("Odaberite tvornicu");
        }

        if(itemsListView.getSelectionModel().getSelectedItems().isEmpty()){
            errors.add("Odaberite barem jedan artikl koji želite obrisati");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(String.join("\n", errors));
        }


    }
}
