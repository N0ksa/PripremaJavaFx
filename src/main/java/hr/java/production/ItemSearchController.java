package hr.java.production;

import hr.java.production.constants.Constants;
import hr.java.production.enums.ValidationRegex;
import hr.java.production.exception.ValidationException;
import hr.java.production.filter.ItemFilter;
import hr.java.production.model.Category;
import hr.java.production.model.Item;
import hr.java.production.model.NamedEntity;
import hr.java.production.model.Store;
import hr.java.production.utility.DatabaseUtil;
import hr.java.production.utility.FileReaderUtil;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ItemSearchController{

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

        List<Category> categoryListString = DatabaseUtil.getCategories();


        ObservableList<Category> observableCategoryList = FXCollections.observableList(categoryListString);
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

        Category itemCategory = categoryComboBox.getValue();
        String itemName = itemNameTextField.getText();

        ItemFilter itemFilter = new ItemFilter(itemName, itemCategory);

        List<Item> filteredItemList = DatabaseUtil.getItemsByFilters(itemFilter);

        ObservableList<Item> observableItemList = FXCollections.observableList(filteredItemList);
        itemsTableView.setItems(observableItemList);
    }

    public void deleteItem(ActionEvent actionEvent) {
        Item itemToDelete = itemsTableView.getSelectionModel().getSelectedItem();

        try {
            validateInputFields();
            DatabaseUtil.deleteItem(itemToDelete);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Spremanje uspješno");
            alert.setHeaderText("Brisanje artikla je bilo uspješno!");
            alert.setContentText("Artikl: " + itemToDelete.getName() + " uspješno se obrisao!");
            alert.showAndWait();
        }
        catch (ValidationException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greška pri unosu");
            alert.setHeaderText("Provjerite ispravnost unesenih podataka");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }


    }


    private void validateInputFields() throws ValidationException {
        List<String> errors = new ArrayList<>();
        Item itemToDelete = itemsTableView.getSelectionModel().getSelectedItem();

        if (Optional.ofNullable(itemToDelete).isEmpty()){
            errors.add("Molim odaberite artikl koji želite obrisati");
        }
        else{

            if(DatabaseUtil.itemInFactory(itemToDelete, DatabaseUtil.getFactories())){
                errors.add("Artikl se koristi u jednoj ili više tvornica. Molim prvo obrišite artikl u tvornicama.");
            }

            if(DatabaseUtil.itemInStore(itemToDelete, DatabaseUtil.getStores())){
                errors.add("Artikl se koristi u jednoj ili više trgovina. Molim prvo obrišite artikl u trgovinama.");
            }
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(String.join("\n", errors));
        }


    }
}