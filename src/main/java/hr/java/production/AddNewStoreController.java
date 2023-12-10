package hr.java.production;

import hr.java.production.enums.ValidationRegex;
import hr.java.production.exception.ValidationException;
import hr.java.production.model.Address;
import hr.java.production.model.Factory;
import hr.java.production.model.Item;
import hr.java.production.model.Store;
import hr.java.production.utility.DatabaseUtil;
import hr.java.production.utility.FileReaderUtil;
import hr.java.production.utility.FileWriterUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static hr.java.production.utility.FileReaderUtil.*;

public class AddNewStoreController {

    @FXML
    private TextField storeNameTextField;
    @FXML
    private TextField storeWebAddressTextField;
    @FXML
    private ListView storeItemsListView;

    public void initialize(){
        storeItemsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        List<Item> items = FileReaderUtil.getItemsFromFile(FileReaderUtil.getCategoriesFromFile());
        ObservableList<Item> observableItemsList = FXCollections.observableList(items);
        storeItemsListView.setItems(observableItemsList);
    }


    public void saveStore(ActionEvent actionEvent) {

        try{

            validateInputFields();
            List <Store> stores = DatabaseUtil.getStores();

            Long storeId = FileWriterUtil.getNextStoreId();
            String storeName = storeNameTextField.getText();
            String storeWebAddress = storeWebAddressTextField.getText();

            Set<Item> storeItems = new HashSet<>(storeItemsListView.getSelectionModel().getSelectedItems());

            Store newStore = new Store(storeId, storeName, storeWebAddress, storeItems);

            stores.add(newStore);

            FileWriterUtil.saveStoresToFile(stores);


            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Spremanje uspješno");
            alert.setHeaderText("Spremanje nove trgovine je bilo uspješno!");
            alert.setContentText("Tgovina " + newStore.getName() + " uspješno se spremila!");
            alert.showAndWait();

        }catch (ValidationException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greška pri unosu");
            alert.setHeaderText("Provjerite ispravnost unesenih podataka");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }



    }

    public void resetStoreAdding(ActionEvent actionEvent) {
        storeNameTextField.setText("");
        storeItemsListView.getSelectionModel().clearSelection();
        storeWebAddressTextField.setText("");

    }


    private void validateInputFields() throws ValidationException {
        List<String> errors = new ArrayList<>();

        if (storeNameTextField.getText().isEmpty()) {
            errors.add("Unesite naziv trgovine");
        }
        if (storeWebAddressTextField.getText().isEmpty()) {
            errors.add("Unesite web adresu trgovine");
        }

        Pattern pattern = Pattern.compile(ValidationRegex.VALID_WEB_ADDRESS.getRegex());
        Matcher matcher;
        matcher = pattern.matcher(storeWebAddressTextField.getText());
        if (!matcher.matches() && !storeWebAddressTextField.getText().isEmpty()){
           errors.add("Molimo unesite ispravan format web-adrese - (www.[A-Za-z0-9].[A-Za-z]+)");
        }

        if (storeItemsListView.getSelectionModel().getSelectedItems().isEmpty()){
            errors.add("Odaberite barem jedan artikl");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(String.join("\n", errors));
        }


    }

}
