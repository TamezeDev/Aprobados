module org.zeki.aprobados {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;

    opens org.zeki.aprobados to javafx.fxml;
    exports org.zeki.aprobados;
    exports org.zeki.aprobados.controller;
    opens org.zeki.aprobados.controller to javafx.fxml;
}