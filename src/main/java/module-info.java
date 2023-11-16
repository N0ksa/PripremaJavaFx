module hr.java.production {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;


    opens hr.java.production to javafx.fxml;
    exports hr.java.production;
}