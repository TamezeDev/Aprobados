package org.zeki.aprobados.controller;

import lombok.Getter;
import lombok.Setter;
import org.zeki.aprobados.model.test.Topic;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TopicController {

    private List<Topic> topics;

    public TopicController() {
        topics = new ArrayList<>();
    }

    public int getModuleId(String nameTopic) {
        // GET ID FROM SELECTED TOPIC
        return topics.stream().filter(topic -> topic.getNameTopic().equals(nameTopic)).findFirst().map(Topic::getIdTopic).orElse(-1);
    }

}


