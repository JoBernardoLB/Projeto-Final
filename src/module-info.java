module projeto.final {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    
    opens app.ui to javafx.fxml;
    opens app.model to com.google.gson;
    exports app.ui;
    exports app.model;
    exports app.service;
}