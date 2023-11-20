package hr.java.production;

import hr.java.production.model.Category;
import hr.java.production.model.Item;
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

public class CategorySearchController {

    @FXML
    private TextField categoryNameTextField;
    @FXML
    private TableView<Category> categoriesTableView;

    @FXML
    private TableColumn<Category, String> categoryNameTableColumn;
    @FXML
    private TableColumn<Category, String> categoryDescriptionTableColumn;


    public void initialize() {
        categoryNameTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Category, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Category, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getName());
            }
        });

        categoryDescriptionTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Category, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Category, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getDescription());
            }
        });

    }


    public void categorySearch(){
        List<Category> categoryList = FileReaderUtil.getCategoriesFromFile();

        String categoryName = categoryNameTextField.getText();

        List<Category> filteredCategoryList = categoryList.stream()
                .filter(category -> category.getName().contains(categoryName))
                .collect(Collectors.toList());

        ObservableList<Category> observableCategoryList = FXCollections.observableList(filteredCategoryList);
        categoriesTableView.setItems(observableCategoryList);
    }

}
