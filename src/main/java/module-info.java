module hr.java.production {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires java.sql;


    opens hr.java.production to javafx.fxml;
    exports hr.java.production;
}