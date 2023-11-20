package hr.java.production;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {
    public void showCategoriesScreen(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("category.fxml"));

        try {
            Scene scene = new Scene(fxmlLoader.load(), 600, 630);
            HelloApplication.getMainStage().setTitle("Categories");
            HelloApplication.getMainStage().setScene(scene);
            HelloApplication.getMainStage().show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void showFactoriesScreen(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("factory.fxml"));

        try {
            Scene scene = new Scene(fxmlLoader.load(), 600, 630);
            HelloApplication.getMainStage().setTitle("Factories");
            HelloApplication.getMainStage().setScene(scene);
            HelloApplication.getMainStage().show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void showStoresScreen(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("store.fxml"));

        try {
            Scene scene = new Scene(fxmlLoader.load(), 600, 630);
            HelloApplication.getMainStage().setTitle("Stores");
            HelloApplication.getMainStage().setScene(scene);
            HelloApplication.getMainStage().show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
