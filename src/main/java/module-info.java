module org.zeki.aprobados {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires javafx.graphics;
    requires javafx.base;
    requires java.net.http;
    requires com.google.gson;
    requires java.xml;

    opens org.zeki.aprobados to javafx.fxml;
    exports org.zeki.aprobados;
    exports org.zeki.aprobados.controller;
    opens org.zeki.aprobados.controller to javafx.fxml;
    exports org.zeki.aprobados.app;
    opens org.zeki.aprobados.app to javafx.fxml;
    exports org.zeki.aprobados.controller.scene;
    opens org.zeki.aprobados.controller.scene to javafx.fxml;
}