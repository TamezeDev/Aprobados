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
    private int topicSelected;
    private  int yearSelected;

    public TopicService() {
        topicController = new TopicController();
        topicSelected = -1;
    }

    public void resetTopicSelected() {
        topicSelected = -1;
    }

    public boolean topicIsSelected() {
        return topicSelected != -1;
    }

    public ResultService getIdModule(String topicName) {
        // GET SELECTED ID TOPIC
        int idModule = topicController.getModuleId(topicName);
        if (idModule == -1) {
            return new ResultService("Id del módulo no encontrado", false);
        }
        topicSelected = idModule;
        return new ResultService("Test recibidos ok", true, idModule);
    }

    public void setListTest(List<Test> tests, int idTopic) {
        // SET ALL TEST FROM SELECTED TOPIC
        List<Topic> topicTest = topicController.getTopics();
        topicTest.forEach(topic -> topic.setTestList(idTopic, tests));
    }

    public ResultService getIdSelectedTest(String name) {
        // GET TOPIC SELECTED
        Topic topic = topicController.getTopicById(topicSelected);
        if (topic == null) return new ResultService("No se pudo obtener el id deo módulo", false);
        // GET ID FROM SELECTED TEST
        int idTest = topic.getIdTestByName(name);
        if (idTest == -1) return new ResultService("Error obteniendo id test", false);
        return new ResultService(true, idTest);
    }
}
