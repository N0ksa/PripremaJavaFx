package hr.java.production;

import hr.java.production.exception.ValidationException;
import hr.java.production.filter.StoreFilter;
import hr.java.production.model.Factory;
import hr.java.production.model.Item;
import hr.java.production.model.Store;
import hr.java.production.utility.DatabaseUtil;
import hr.java.production.utility.FileReaderUtil;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StoreSearchController {

    @FXML
    private TextField storeNameTextField;
    @FXML
    private TableView<Store> storesTableView;

    @FXML
    private TableColumn<Store, String> storeNameTableColumn;
    @FXML
    private TableColumn<Store, String> storeWebAddressTableColumn;
    @FXML
    private TableColumn<Store, String> storeItemsTableColumn;


    public void initialize() {
        storeNameTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Store, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Store, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getName());
            }
        });

        storeWebAddressTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Store, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Store, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getWebAddress());
            }
        });

        storeItemsTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Store, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Store, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getItems()
                        .stream()
                        .map(item -> item.getName())
                        .collect(Collectors.toList()).toString());
            }
        });

    }


    public void storeSearch(){
        String factoryName = storeNameTextField.getText();

        StoreFilter storeFilter = new StoreFilter(factoryName);
        List <Store> filteredFactoryList = DatabaseUtil.getStoresByFilters(storeFilter);

        ObservableList<Store> observableFactoryList = FXCollections.observableList(filteredFactoryList);
        storesTableView.setItems(observableFactoryList);
    }

    public void deleteStore(ActionEvent actionEvent) {
        Store storeToDelete = storesTableView.getSelectionModel().getSelectedItem();

        try {
            validateInputFields();

            DatabaseUtil.deleteItemsFromStore(storeToDelete, storeToDelete.getItems().stream().toList());
            DatabaseUtil.deleteStore(storeToDelete);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Spremanje uspješno");
            alert.setHeaderText("Brisanje trgovine je bilo uspješno!");
            alert.setContentText("Trgovina " + storeToDelete.getName() + ", uspješno se obrisala!");
            alert.showAndWait();

        } catch (ValidationException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greška pri unosu");
            alert.setHeaderText("Provjerite ispravnost unesenih podataka");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }
    }

    private void validateInputFields() throws ValidationException {
        List<String> errors = new ArrayList<>();
        Store factoryToDelete = storesTableView.getSelectionModel().getSelectedItem();
        if (Optional.ofNullable(factoryToDelete).isEmpty()){
            errors.add("Molimo odaberite trgovinu koju želite obrisati");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(String.join("\n", errors));
        }


    }

}

