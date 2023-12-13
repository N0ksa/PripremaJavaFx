package hr.java.production;

import hr.java.production.filter.FactoryFilter;
import hr.java.production.model.Category;
import hr.java.production.model.Factory;
import hr.java.production.model.Item;
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

public class FactorySearchController {

    @FXML
    private TextField factoryNameTextField;
    @FXML
    private TableView<Factory> factoriesTableView;

    @FXML
    private TableColumn<Factory, String> factoryNameTableColumn;
    @FXML
    private TableColumn<Factory, String> factoryAddressTableColumn;
    @FXML
    private TableColumn<Factory, String> factoryItemsTableColumn;


    public void initialize() {
        factoryNameTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Factory, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Factory, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getName());
            }
        });

        factoryAddressTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Factory, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Factory, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getAdress().toString());
            }
        });

        factoryItemsTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Factory, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Factory, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getItems()
                        .stream()
                        .map(item -> item.getName())
                        .collect(Collectors.toList()).toString());
            }
        });

    }


    public void factorySearch(){


        String factoryName = factoryNameTextField.getText();
        FactoryFilter factoryFilter = new FactoryFilter(factoryName);

        List <Factory> filteredFactoryList = DatabaseUtil.getFactoriesByFilters(factoryFilter);

        ObservableList<Factory> observableFactoryList = FXCollections.observableList(filteredFactoryList);
        factoriesTableView.setItems(observableFactoryList);
    }



}
