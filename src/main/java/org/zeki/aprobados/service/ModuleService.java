package org.zeki.aprobados.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.zeki.aprobados.exception.SupabaseConnectionException;
import org.zeki.aprobados.model.test.Topic;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class ModuleService extends SupabaseClient {

    public ResultService getModules() {
        // GET ALL TOPIC FROM DB
        try {
            String url = RPC_URL + "get_modulos";
            HttpResponse<String> response = super.getJson(url);

            if (response.statusCode() == 200) {

                JsonArray jsonArray = JsonParser.parseString(response.body()).getAsJsonArray();
                List<Topic> topics = getTopics(jsonArray);
                return new ResultService("Módulos disponibles cargados", true, topics);
            }
            return new ResultService("Error obteniendo módulos de test", false, new ArrayList<>());

        } catch (Exception e) {
            throw new SupabaseConnectionException("ERROR CONECTANDO CON EL SERVIDOR");
        }
    }

    // ----------- PRIVATE METHODS -------------

    private static List<Topic> getTopics(JsonArray jsonArray) {

        List<Topic> topics = new ArrayList<>();
        // SET TOPICS IN LIST
        jsonArray.forEach(item -> {
            JsonObject jsonTopic = item.getAsJsonObject();
            int idTopic = jsonTopic.get("id_modulo").getAsInt();
            String nameTopic = jsonTopic.get("nombre_modulo").getAsString();

            Topic topic = new Topic(idTopic, nameTopic);
            topics.add(topic);
        });
        return topics;
    }

}
