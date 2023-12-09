package hr.java.production;

import hr.java.production.constants.Constants;
import hr.java.production.model.Category;
import hr.java.production.model.Item;
import hr.java.production.utility.DatabaseUtil;
import hr.java.production.utility.FileReaderUtil;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ItemSearchController{

    @FXML
    private TextField itemNameTextField;
    @FXML
    private ComboBox<String> categoryComboBox;
    @FXML
    private TableView<Item> itemsTableView;

    @FXML
    private TableColumn<Item, String> itemNameTableColumn;
    @FXML
    private TableColumn<Item, String> itemCategoryTableColumn;
    @FXML
    private TableColumn<Item, String> itemWidthTableColumn;
    @FXML
    private TableColumn<Item, String> itemHeightTableColumn;
    @FXML
    private TableColumn<Item, String> itemLengthTableColumn;
    @FXML
    private TableColumn<Item, String> itemProductionCostTableColumn;
    @FXML
    private TableColumn<Item, String> itemSellingPriceTableColumn;


    public void initialize(){

        List<String> categoryList = FileReaderUtil.getCategoriesFromFile()
                .stream().map(category -> category.getName())
                .collect(Collectors.toList());

        ObservableList<String> observableCategoryList = FXCollections.observableList(categoryList);
        categoryComboBox.setItems(observableCategoryList);

        itemNameTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Item,String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Item, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getName());
            }
        });

        itemCategoryTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Item,String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Item, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getCategory().getName());
            }
        });

        itemWidthTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Item,String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Item, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getWidth().toString());
            }
        });

        itemHeightTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Item,String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Item, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getHeight().toString());
            }
        });

        itemLengthTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Item,String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Item, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getLength().toString());
            }
        });

        itemProductionCostTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Item,String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Item, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getProductionCost().toString());
            }
        });

        itemSellingPriceTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Item,String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Item, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getSellingPrice().toString());
            }
        });
    }

    public void itemSearch(){
        //List<Category> categoryList = FileReaderUtil.getCategoriesFromFile();
        //List<Item> itemList = FileReaderUtil.getItemsFromFile(categoryList);

        List<Item> itemList = DatabaseUtil.getItems();

        String itemCategoryText = categoryComboBox.getValue();
        String itemName = itemNameTextField.getText();

        List<Item> filtereditemList = itemList.stream().filter(item -> item.getName().contains(itemName)).collect(Collectors.toList());
        if(itemCategoryText != null){
            filtereditemList = filtereditemList.stream().filter(item -> item.getCategory().getName().equalsIgnoreCase(itemCategoryText)).collect(Collectors.toList());
        }



        ObservableList<Item> observableItemList = FXCollections.observableList(filtereditemList);
        itemsTableView.setItems(observableItemList);
    }
}