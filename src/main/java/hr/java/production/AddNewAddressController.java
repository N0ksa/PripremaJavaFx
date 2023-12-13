package hr.java.production;

import hr.java.production.enums.City;
import hr.java.production.exception.ValidationException;
import hr.java.production.model.Address;
import hr.java.production.model.Category;
import hr.java.production.model.Discount;
import hr.java.production.model.Item;
import hr.java.production.utility.DatabaseUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AddNewAddressController {


    @FXML
    private TextField streetNameTextField;
    @FXML
    private TextField houseNumberTextField;
    @FXML
    private ChoiceBox <City> cityChoiceBox;


    public void initialize(){
        List<City> cities = List.of(City.values());
        ObservableList<City> observableCityList = FXCollections.observableList(cities);
        cityChoiceBox.setItems(observableCityList);
    }


    public void saveAddress(ActionEvent actionEvent) {
        List<Address> addresses = new ArrayList<>();

        try {
            validateInputFields();

            String streetName = streetNameTextField.getText();
            String houseNumber = houseNumberTextField.getText();
            City city = cityChoiceBox.getSelectionModel().getSelectedItem();
            Address newAddress = new Address.AdressBuilder(city)
                    .setHouseNumber(houseNumber)
                    .setStreet(streetName)
                    .setId(0L)
                    .build();

            addresses.add(newAddress);
            DatabaseUtil.saveAddresses(addresses);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Spremanje uspješno");
            alert.setHeaderText("Spremanje nove adrese je bilo uspješno!");
            alert.setContentText("Adresa: " + newAddress.toString() + " uspješno se spremila!");
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


        if (streetNameTextField.getText().isEmpty()) {
            errors.add("Unesite naziv ulice");
        }

        if(cityChoiceBox.getValue() == null){
            errors.add("Grad mora biti odabran!");
        }

        if (houseNumberTextField.getText().isEmpty()) {
            errors.add("Unesite kućni broj");
        } else {
            try {

                int houseNumber = Integer.parseInt(houseNumberTextField.getText());
                if (houseNumber <= 0) {
                    errors.add("Kućni broj mora biti pozitivan broj");
                }
            } catch (NumberFormatException e) {
                errors.add("Kućni broj mora biti cijeli broj");
            }
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(String.join("\n", errors));
        }
    }


}
