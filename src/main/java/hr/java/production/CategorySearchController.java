package hr.java.production;

import hr.java.production.exception.ValidationException;
import hr.java.production.filter.CategoryFilter;
import hr.java.production.model.Category;
import hr.java.production.model.Item;
import hr.java.production.model.Store;
import hr.java.production.utility.DatabaseUtil;
import hr.java.production.utility.FileReaderUtil;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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



        String categoryName = categoryNameTextField.getText();
        CategoryFilter categoryFilter = new CategoryFilter(categoryName);

        List<Category> filteredCategoryList = DatabaseUtil.getCategoriesByFilters(categoryFilter);

        ObservableList<Category> observableCategoryList = FXCollections.observableList(filteredCategoryList);
        categoriesTableView.setItems(observableCategoryList);
    }

    public void deleteCategory(ActionEvent actionEvent) {
        Category categoryToDelete = categoriesTableView.getSelectionModel().getSelectedItem();

        try {
            validateInputFields();

            DatabaseUtil.deleteCategory(categoryToDelete);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Spremanje uspješno");
            alert.setHeaderText("Brisanje kategorije je bilo uspješno!");
            alert.setContentText("Kategorija " + categoryToDelete.getName() + ", uspješno se obrisala!");
            alert.showAndWait();

        } catch (ValidationException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greška pri unosu");
            alert.setHeaderText("Provjerite ispravnost unesenih podataka");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }
    }

    private void validateInputFields() throws ValidationException {
        List<String> errors = new ArrayList<>();
        Category categoryToDelete = categoriesTableView.getSelectionModel().getSelectedItem();

        if (Optional.ofNullable(categoryToDelete).isEmpty()) {
            errors.add("Molimo odaberite kategoriju koju želite obrisati");
        }else{

            if (DatabaseUtil.categoryInUse(categoryToDelete, DatabaseUtil.getItems())) {
                errors.add("Kategorija se koristi kod jednog ili više artikla. Molim prvo obrisati artikle koji koriste kategoriju.");

            }
        }


        if (!errors.isEmpty()) {
            throw new ValidationException(String.join("\n", errors));
        }
    }

}
