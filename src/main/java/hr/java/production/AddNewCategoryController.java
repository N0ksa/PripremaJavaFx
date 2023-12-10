package hr.java.production;

import hr.java.production.exception.ValidationException;
import hr.java.production.model.Category;
import hr.java.production.utility.DatabaseUtil;
import hr.java.production.utility.FileReaderUtil;
import hr.java.production.utility.FileWriterUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

public class AddNewCategoryController {

    @FXML
    private TextArea categoryDescriptionTextArea;
    @FXML
    private TextField categoryNameTextField;

    public void saveCategory(ActionEvent actionEvent) {
        try{
            validateInputFields();

            Long categoryId = FileWriterUtil.getNextCategoryId();
            String categoryName = categoryNameTextField.getText();
            String categoryDescription = categoryDescriptionTextArea.getText();

            Category newCategory = new Category(categoryId,categoryName,categoryDescription);

            List<Category> categories = DatabaseUtil.getCategories();
            categories.add(newCategory);
            FileWriterUtil.saveCategoriesToFile(categories);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Spremanje uspješno");
            alert.setHeaderText("Spremanje novog artikla je bilo uspješno!");
            alert.setContentText("Kategorija " + newCategory.getName().toLowerCase() + " uspješno se spremila!");
            alert.showAndWait();

        }catch (ValidationException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greška pri unosu");
            alert.setHeaderText("Provjerite ispravnost unesenih podataka");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }

    }

    public void resetCategoryAdding(ActionEvent actionEvent) {
        categoryNameTextField.setText("");
        categoryDescriptionTextArea.setText("");
    }


    private void validateInputFields() throws ValidationException {
        List<String> errors = new ArrayList<>();

        if (categoryNameTextField.getText().isEmpty()) {
            errors.add("Unesite naziv kategorije");
        }
        if (categoryDescriptionTextArea.getText().isEmpty()) {
            errors.add("Unesite opis kategorije");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(String.join("\n", errors));
        }


    }

}
