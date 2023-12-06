package hr.java.production;

import hr.java.production.model.Item;
import hr.java.production.utility.FileReaderUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;

import java.util.List;

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

    }

    public void resetStoreAdding(ActionEvent actionEvent) {
        storeNameTextField.setText("");
        storeItemsListView.getSelectionModel().clearSelection();
        storeWebAddressTextField.setText("");

    }

}
