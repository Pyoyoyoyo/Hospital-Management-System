module com.main.hospitalmanagementsys {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;

    opens com.main.hospitalmanagementsys.model to javafx.base;
    opens com.main.hospitalmanagementsys to javafx.fxml;
    exports com.main.hospitalmanagementsys;
    exports com.main.hospitalmanagementsys.controllers;
    opens com.main.hospitalmanagementsys.controllers to javafx.fxml;
}