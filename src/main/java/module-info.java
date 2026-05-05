module org.zeki.aprobados {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires javafx.graphics;
    requires javafx.base;
    requires java.net.http;
    requires com.google.gson;
    requires java.xml;
    requires org.zeki.aprobados;

    opens org.zeki.aprobados to javafx.fxml;
    exports org.zeki.aprobados;
    exports org.zeki.aprobados.controller;
    opens org.zeki.aprobados.controller to javafx.fxml;
}