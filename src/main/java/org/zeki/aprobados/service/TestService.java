package org.zeki.aprobados.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.zeki.aprobados.exception.SupabaseConnectionException;
import org.zeki.aprobados.model.test.Answer;
import org.zeki.aprobados.model.test.Question;
import org.zeki.aprobados.model.test.Test;
import org.zeki.aprobados.model.user.AnswerTest;
import org.zeki.aprobados.model.user.StudentTest;

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
                test.setReviewed(false);
                return new ResultService("Test recibido ok", true, test);
            }
            return new ResultService("Error al recibir los datos del test seleccionado", false);

        } catch (Exception e) {
            throw new SupabaseConnectionException("ERROR CONECTANDO CON EL SERVIDOR");
        }
    }

    public ResultService sendTestToDB(StudentTest studentTest, String jwt) {
        // SENT TO DB TEST FINISHED AND FAIL QUESTIONS
        try {
            String url = RPC_URL + "guardar_resultado_test";
            JsonObject body = parseJsonTestFinished(studentTest);

            HttpResponse<String> response = super.postJson(url, body.toString(), jwt);
            JsonObject result = JsonParser.parseString(response.body()).getAsJsonObject();

            if (result.get("ok").getAsBoolean()) {
                return new ResultService("Resultado guardado correctamente", true);
            }
            return new ResultService("Error guardando el resultado", false);

        } catch (Exception e) {
            throw new SupabaseConnectionException("ERROR CONECTANDO CON EL SERVIDOR");
        }
    }

    public ResultService getFailQuestions(String jwt) {
        try {
            String url = RPC_URL + "get_preguntas_falladas";

            HttpResponse<String> response = super.getJson(url, jwt);

            if (response.statusCode() == 200) {
                // GET DATA
                String body = response.body();
                JsonArray jsonArray = JsonParser.parseString(body).getAsJsonArray();
                List<Question> questions = parseJsonTest(jsonArray);
                Test test = new Test();
                test.setQuestions(questions);
                test.setReviewed(true);
                return new ResultService("Test recibido ok", true, test);
            }
            return new ResultService("Error al recibir los datos del test seleccionado", false);

        } catch (Exception e) {
            throw new SupabaseConnectionException("ERROR CONECTANDO CON EL SERVIDOR");
        }
    }

    public ResultService sendReviewedQuestions(List<Integer> rightIDS, String jwt) {
        // SEND TO DB ALL CORRECTS QUESTIONS ID FOR EACH STUDENT
        try {
            String url = RPC_URL + "resolver_fallos";
            JsonObject object = parseReviewedQuestionsID(rightIDS);

            HttpResponse<String> response = super.postJson(url, object.toString(), jwt);
            JsonObject result = JsonParser.parseString(response.body()).getAsJsonObject();

            if (result.get("ok").getAsBoolean()) {
                return new ResultService("Datos actualizados", true);
            }
            return new ResultService("Error eliminando errores del usuario", false);
        } catch (Exception e) {
            throw new SupabaseConnectionException("ERROR CONECTANDO CON EL SERVIDOR");
        }

    }

    // ----------- PRIVATE METHODS -------------
    private JsonObject parseReviewedQuestionsID(List<Integer> corrects) {

        JsonArray jsonArray = new JsonArray();
        corrects.forEach(jsonArray::add);
        JsonObject object = new JsonObject();
        object.add("p_ids_acertados", jsonArray);

        return object;
    }

    private JsonObject parseJsonTestFinished(StudentTest studentTest) {
        JsonArray wrongJson = new JsonArray();
        studentTest.getAnswers().forEach(answer -> {
            JsonObject fallo = new JsonObject();
            fallo.addProperty("id_pregunta", answer.getIdQuestion());
            wrongJson.add(fallo);
        });
        JsonObject datos = new JsonObject();
        datos.addProperty("id_test", studentTest.getIdTest());
        datos.addProperty("nota", studentTest.getNote());
        datos.addProperty("errores", studentTest.getErrors());
        datos.addProperty("aciertos", studentTest.getRight());
        datos.add("fallos", wrongJson);

        JsonObject body = new JsonObject();
        body.add("p_datos", datos);
        return body;
    }

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
