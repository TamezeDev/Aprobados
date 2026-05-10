module org.zeki.aprobados {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires javafx.graphics;
    requires javafx.base;
    requires java.net.http;
    requires com.google.gson;
    requires java.xml;
    requires java.desktop;

    exports org.zeki.aprobados.model.user;
    exports org.zeki.aprobados.model.test;
    exports org.zeki.aprobados.model.syllabus;
    exports org.zeki.aprobados.controller;
    exports org.zeki.aprobados.service;
    exports org.zeki.aprobados;
    exports org.zeki.aprobados.app;
    exports org.zeki.aprobados.controller.scene;

    opens org.zeki.aprobados to javafx.fxml;
    opens org.zeki.aprobados.controller to javafx.fxml;
    opens org.zeki.aprobados.app to javafx.fxml;
    opens org.zeki.aprobados.controller.scene to javafx.fxml;
}