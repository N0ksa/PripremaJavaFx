package hr.java.production;

import hr.java.production.model.Factory;
import hr.java.production.model.Item;
import hr.java.production.model.Store;
import hr.java.production.utility.DatabaseUtil;
import hr.java.production.utility.FileReaderUtil;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;

import java.util.List;
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
        //List<Item> itemList = FileReaderUtil.getItemsFromFile(FileReaderUtil.getCategoriesFromFile());
        //List<Store> storeList = FileReaderUtil.getStoresFromFile(itemList);

        List<Store> storeList = DatabaseUtil.getStores();


        String factoryName = storeNameTextField.getText();

        List<Store> filteredFactoryList = storeList.stream()
                .filter(store -> store.getName().contains(factoryName))
                .collect(Collectors.toList());

        ObservableList<Store> observableFactoryList = FXCollections.observableList(filteredFactoryList);
        storesTableView.setItems(observableFactoryList);
    }
}
