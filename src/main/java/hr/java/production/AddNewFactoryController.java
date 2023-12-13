package hr.java.production;

import hr.java.production.enums.ValidationRegex;
import hr.java.production.exception.ValidationException;
import hr.java.production.model.Address;
import hr.java.production.model.Category;
import hr.java.production.model.Factory;
import hr.java.production.model.Item;
import hr.java.production.utility.DatabaseUtil;
import hr.java.production.utility.FileReaderUtil;
import hr.java.production.utility.FileWriterUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static hr.java.production.utility.FileReaderUtil.*;

public class AddNewFactoryController {

    @FXML
    private TextField factoryNameTextField;
    @FXML
    private ListView <Item> factoryItemsListView;
    @FXML
    private ComboBox <Address> factoryAddressComboBox;


    private static List<Category> categories;

    public void initialize(){

        categories = DatabaseUtil.getCategories();

        factoryItemsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        List<Item> items = DatabaseUtil.getItems();
        ObservableList<Item> observableItemsList = FXCollections.observableList(items);
        factoryItemsListView.setItems(observableItemsList);

        ObservableList<Address> observableAddressesList = FXCollections.observableList(DatabaseUtil.getAddresses());
        factoryAddressComboBox.setItems(observableAddressesList);


    }

    public void saveFactory(ActionEvent actionEvent) {

        try{

            validateInputFields();

            List<Factory> factories = new ArrayList<>();

            Long factoryId = FileWriterUtil.getNextFactoryId();
            String factoryName = factoryNameTextField.getText();
            Address factoryAddress = factoryAddressComboBox.getSelectionModel().getSelectedItem();
            Set<Item> factoryItems = new HashSet<>(factoryItemsListView.getSelectionModel().getSelectedItems());

            Factory newFactory = new Factory(factoryId, factoryName, factoryAddress, factoryItems);

            factories.add(newFactory);

            DatabaseUtil.saveFactories(factories);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Spremanje uspješno");
            alert.setHeaderText("Spremanje nove tvornice je bilo uspješno!");
            alert.setContentText("Tvornica " + newFactory.getName() + " uspješno se spremila!");
            alert.showAndWait();

        }  catch (ValidationException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greška pri unosu");
            alert.setHeaderText("Provjerite ispravnost unesenih podataka");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }

    }

    public void resetFactoryAdding(ActionEvent actionEvent) {
        factoryNameTextField.setText("");
        factoryItemsListView.getSelectionModel().clearSelection();
        factoryAddressComboBox.setValue(null);

    }


    private void validateInputFields() throws ValidationException {
        List<String> errors = new ArrayList<>();

        if (factoryNameTextField.getText().isEmpty()) {
            errors.add("Unesite naziv tvornice");
        }
        if (factoryAddressComboBox.getSelectionModel().isEmpty()) {
            errors.add("Odaberite adresu tvornice");
        }

        if (factoryItemsListView.getSelectionModel().getSelectedItems().isEmpty()){
            errors.add("Odaberite barem jedan artikl");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(String.join("\n", errors));
        }


    }
}
