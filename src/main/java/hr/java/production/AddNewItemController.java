package hr.java.production;

import hr.java.production.exception.ValidationException;
import hr.java.production.model.Category;
import hr.java.production.model.Discount;
import hr.java.production.model.Item;
import hr.java.production.utility.FileReaderUtil;
import hr.java.production.utility.FileWriterUtil;
import hr.java.production.utility.SafeInput;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AddNewItemController {
    @FXML
    private TextField itemNameTextField;
    @FXML
    private TextField itemWidthTextField;
    @FXML
    private TextField itemHeightTextField;
    @FXML
    private TextField itemLengthTextField;
    @FXML
    private TextField itemProductionCostTextField;
    @FXML
    private TextField itemSellingPriceTextField;
    @FXML
    private TextField itemDiscountTextField;
    @FXML
    private ComboBox<Category> itemCategoryComboBox;

    private static List<Category> categories;


    public void initialize(){
        categories =  FileReaderUtil.getCategoriesFromFile();
        ObservableList<Category> observableCategoriesList = FXCollections.observableList(categories);

        itemCategoryComboBox.setItems(observableCategoriesList);

    }



    public void saveItem(ActionEvent actionEvent) {
        try {
            validateInputFields();

            Long itemId = FileWriterUtil.getNextItemId();
            String itemName = itemNameTextField.getText();
            Category itemCategory = itemCategoryComboBox.getValue();
            BigDecimal itemWidth = new BigDecimal(itemWidthTextField.getText());
            BigDecimal itemHeight = new BigDecimal(itemHeightTextField.getText());
            BigDecimal itemLength = new BigDecimal(itemLengthTextField.getText());
            BigDecimal itemProductionCost = new BigDecimal(itemProductionCostTextField.getText());
            BigDecimal itemSellingPrice = new BigDecimal(itemSellingPriceTextField.getText());
            Integer itemDiscount = Integer.parseInt(itemDiscountTextField.getText());

            Item newItem = new Item(itemId, itemName, itemCategory, itemWidth, itemHeight, itemLength, itemProductionCost,
                    itemSellingPrice, new Discount(itemDiscount));


            List<Item> items = FileReaderUtil.getItemsFromFile(categories);


            items.add(newItem);


            FileWriterUtil.saveItemsToFile(items);


            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Spremanje uspješno");
            alert.setHeaderText("Spremanje novog artikla je bilo uspješno!");
            alert.setContentText("Artikl " + newItem.getName().toLowerCase() + " - kategorija: " + newItem.getCategory().getName().toLowerCase() + " uspješno se spremio!");
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


        if (itemNameTextField.getText().isEmpty()) {
            errors.add("Unesite naziv artikla");
        }

        if(itemCategoryComboBox.getValue() == null){
            errors.add("Kategorija mora biti označena!");
        }

        validatePositiveDecimalField(itemWidthTextField, "Širina", errors);
        validatePositiveDecimalField(itemHeightTextField, "Visina", errors);
        validatePositiveDecimalField(itemLengthTextField, "Dužina", errors);
        validatePositiveDecimalField(itemProductionCostTextField, "Trošak proizvodnje", errors);
        validatePositiveDecimalField(itemSellingPriceTextField, "Prodajna cijena", errors);
        validateIntegerRangeField(itemDiscountTextField, "Popust", 0, 100, errors);

        if (!errors.isEmpty()) {
            throw new ValidationException(String.join("\n", errors));
        }
    }

    private void validatePositiveDecimalField(TextField field, String fieldName, List<String> errors) {
        try {
            BigDecimal value = new BigDecimal(field.getText());
            if (value.compareTo(BigDecimal.ZERO) <= 0) {
                errors.add(fieldName + " mora biti pozitivan decimalni broj");
            }
        } catch (NumberFormatException ex) {
            errors.add(fieldName + " mora biti decimalni broj");
        }
    }


    private void validateIntegerRangeField(TextField field, String fieldName, int min, int max, List<String> errors) {
        try {
            int value = Integer.parseInt(field.getText());
            if (value < min || value > max) {
                errors.add(fieldName + " mora biti cijeli broj između " + min + " i " + max);
            }
        } catch (NumberFormatException ex) {
            errors.add(fieldName + " mora biti cijeli broj");
        }
    }
}




