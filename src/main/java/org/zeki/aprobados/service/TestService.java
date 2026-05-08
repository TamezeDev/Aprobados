package org.zeki.aprobados.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.zeki.aprobados.exception.SupabaseConnectionException;
import org.zeki.aprobados.model.test.Answer;
import org.zeki.aprobados.model.test.Question;
import org.zeki.aprobados.model.test.Test;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class TestService extends SupabaseClient {

    public ResultService getTestById(int idTest) {
        // GET ALL DATA FROM A TEST BY ID
        try {
            String url = RPC_URL + "get_preguntas_test";
            // CREATE JSON
            JsonObject object = new JsonObject();
            object.addProperty("p_id_test", idTest);

            HttpResponse<String> response = super.postJson(url, object.toString());
            if (response.statusCode() == 200) {
                // GET DATA
                String body = response.body();
                JsonArray jsonArray = JsonParser.parseString(body).getAsJsonArray();
                List<Question> questions = parseJsonTest(jsonArray);
                Test test = new Test();
                test.setIdTest(idTest);
                test.setQuestions(questions);
                return  new ResultService("Test recibido ok", true, test);
            }
            return new ResultService("Error al recibir los datos del test seleccionado", false);

        } catch (Exception e) {
            throw new SupabaseConnectionException("ERROR CONECTANDO CON EL SERVIDOR");
        }
    }

    // ----------- PRIVATE METHODS -------------

    private List<Question> parseJsonTest(JsonArray jsonArray) {

        List<Question> questions = new ArrayList<>();

        jsonArray.forEach(item -> {
            JsonObject object = item.getAsJsonObject();
            JsonArray jsonOptions = object.getAsJsonArray("opciones");

            List<Answer> answers = new ArrayList<>();

            jsonOptions.forEach(option -> {
                JsonObject answerJson = option.getAsJsonObject();
                int idAnswer = answerJson.get("id_respuesta").getAsInt();
                String text = answerJson.get("texto_opcion").getAsString();
                boolean right = answerJson.get("correcta").getAsBoolean();
                int order = answerJson.get("orden").getAsInt();

                Answer answer = new Answer(idAnswer, text, right, order);
                answers.add(answer);
            });

            int idQuestion = object.get("id_pregunta").getAsInt();
            String text = object.get("nombre_pregunta").getAsString();
            String explain = object.get("explicacion").getAsString();

            Question question = new Question(idQuestion, text, explain, answers);
            questions.add(question);
        });
        return questions;
    }
}
