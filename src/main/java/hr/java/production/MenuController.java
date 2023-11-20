package hr.java.production;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class MenuController {
    public void showCategoriesScreen(){
        FXMLLoader fxmlLoader = new FXMLLoader(JavaFxApplication.class.getResource("category.fxml"));

        try {
            Scene scene = new Scene(fxmlLoader.load(), 600, 630);
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
            Scene scene = new Scene(fxmlLoader.load(), 600, 630);
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
            Scene scene = new Scene(fxmlLoader.load(), 600, 630);
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
            Scene scene = new Scene(fxmlLoader.load(), 600, 630);
            JavaFxApplication.getMainStage().setTitle("Items");
            JavaFxApplication.getMainStage().setScene(scene);
            JavaFxApplication.getMainStage().show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
