package org.zeki.aprobados.service;

import lombok.Getter;
import lombok.Setter;
import org.zeki.aprobados.controller.TopicController;
import org.zeki.aprobados.model.test.Test;
import org.zeki.aprobados.model.test.Topic;

import java.util.List;

@Getter
@Setter
public class TopicService {

    private TopicController topicController;

    public TopicService() {
        topicController = new TopicController();
    }

    public ResultService getIdModule(String topicName) {
        // GET SELECTED ID TOPIC
        int idModule = topicController.getModuleId(topicName);
        if (idModule == -1) {
            return new ResultService("Id del módulo no encontrado", false);
        }
        return new ResultService("Test recibidos ok", true, idModule);
    }

    public void setListTest(List<Test>tests, int idTopic){
        List<Topic> topicTest = topicController.getTopics();
        topicTest.forEach(topic -> topic.setTestList(idTopic, tests));
    }
}
