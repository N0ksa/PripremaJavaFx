package hr.java.production;

import hr.java.production.constants.Constants;
import hr.java.production.model.Category;
import hr.java.production.model.Item;
import hr.java.production.utility.FileReaderUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class ItemSearchController {

    @FXML
    private TextField itemNameTextField;
    @FXML
    private ComboBox<Category> categoryComboBox;
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

    }
    public void itemSearch(){
        List<Category> categoryList = FileReaderUtil.getCategoriesFromFile();
        List<Item> itemList = FileReaderUtil.getItemsFromFile(categoryList);

        ObservableList<Item> observableItemList = FXCollections.observableList(itemList);
        itemsTableView.setItems(observableItemList);
    }
}