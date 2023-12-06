package hr.java.production;

import hr.java.production.model.Address;
import hr.java.production.model.Category;
import hr.java.production.model.Item;
import hr.java.production.utility.FileReaderUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;

import java.util.List;

public class AddNewFactoryController {

    @FXML
    private TextField factoryNameTextField;
    @FXML
    private ListView factoryItemsListView;
    @FXML
    private ComboBox factoryAddressComboBox;


    private static List<Category> categories;

    public void initialize(){

        categories = FileReaderUtil.getCategoriesFromFile();

        factoryItemsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        List<Item> items = FileReaderUtil.getItemsFromFile(categories);
        ObservableList<Item> observableItemsList = FXCollections.observableList(items);
        factoryItemsListView.setItems(observableItemsList);

        ObservableList<Address> observableAddressesList = FXCollections.observableList(FileReaderUtil.getAdressesFromFile());
        factoryAddressComboBox.setItems(observableAddressesList);


    }

    public void saveFactory(ActionEvent actionEvent) {

    }

    public void resetFactoryAdding(ActionEvent actionEvent) {
        factoryNameTextField.setText("");
        factoryItemsListView.getSelectionModel().clearSelection();
        factoryAddressComboBox.setValue(null);

    }
}
