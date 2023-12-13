package hr.java.production;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class MenuController {

    public void showMainScreen(){
        FXMLLoader fxmlLoader = new FXMLLoader(JavaFxApplication.class.getResource("mainScreen.fxml"));
        try{
            Scene scene = new Scene(fxmlLoader.load());
            JavaFxApplication.getMainStage().setTitle("Priprema 7");
            JavaFxApplication.getMainStage().setScene(scene);
            JavaFxApplication.getMainStage().show();


        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
    public void showCategoriesScreen(){
        FXMLLoader fxmlLoader = new FXMLLoader(JavaFxApplication.class.getResource("category.fxml"));

        try {
            Scene scene = new Scene(fxmlLoader.load());
            JavaFxApplication.getMainStage().setTitle("Categories");
            JavaFxApplication.getMainStage().setScene(scene);
            JavaFxApplication.getMainStage().show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void showFactoriesScreen(){
        FXMLLoader fxmlLoader = new FXMLLoader(JavaFxApplication.class.getResource("factory.fxml"));

        try {
            Scene scene = new Scene(fxmlLoader.load());
            JavaFxApplication.getMainStage().setTitle("Factories");
            JavaFxApplication.getMainStage().setScene(scene);
            JavaFxApplication.getMainStage().show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void showStoresScreen(){
        FXMLLoader fxmlLoader = new FXMLLoader(JavaFxApplication.class.getResource("store.fxml"));

        try {
            Scene scene = new Scene(fxmlLoader.load());
            JavaFxApplication.getMainStage().setTitle("Stores");
            JavaFxApplication.getMainStage().setScene(scene);
            JavaFxApplication.getMainStage().show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void showItemsScreen(){
        FXMLLoader fxmlLoader = new FXMLLoader(JavaFxApplication.class.getResource("items.fxml"));

        try {
            Scene scene = new Scene(fxmlLoader.load());
            JavaFxApplication.getMainStage().setTitle("Items");
            JavaFxApplication.getMainStage().setScene(scene);
            JavaFxApplication.getMainStage().show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    public void showAddNewItem(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(JavaFxApplication.class.getResource("addNewItem.fxml"));

        try {
            Scene scene = new Scene(fxmlLoader.load());
            JavaFxApplication.getMainStage().setTitle("Add Item");
            JavaFxApplication.getMainStage().setScene(scene);
            JavaFxApplication.getMainStage().show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void showAddNewCategory(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(JavaFxApplication.class.getResource("addNewCategory.fxml"));

        try {
            Scene scene = new Scene(fxmlLoader.load());
            JavaFxApplication.getMainStage().setTitle("Add Category");
            JavaFxApplication.getMainStage().setScene(scene);
            JavaFxApplication.getMainStage().show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showAddNewStoresScreen(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(JavaFxApplication.class.getResource("addNewStore.fxml"));

        try {
            Scene scene = new Scene(fxmlLoader.load());
            JavaFxApplication.getMainStage().setTitle("Add Category");
            JavaFxApplication.getMainStage().setScene(scene);
            JavaFxApplication.getMainStage().show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showAddNewFactoriesScreen(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(JavaFxApplication.class.getResource("addNewFactory.fxml"));

        try {
            Scene scene = new Scene(fxmlLoader.load());
            JavaFxApplication.getMainStage().setTitle("Add Factory");
            JavaFxApplication.getMainStage().setScene(scene);
            JavaFxApplication.getMainStage().show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showAddNewAddress(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(JavaFxApplication.class.getResource("addNewAddress.fxml"));

        try {
            Scene scene = new Scene(fxmlLoader.load());
            JavaFxApplication.getMainStage().setTitle("Add Address");
            JavaFxApplication.getMainStage().setScene(scene);
            JavaFxApplication.getMainStage().show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
