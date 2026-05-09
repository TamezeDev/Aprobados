package org.zeki.aprobados.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.zeki.aprobados.app.SessionManager;
import org.zeki.aprobados.dto.ModuleStudyDto;
import org.zeki.aprobados.exception.SupabaseConnectionException;
import org.zeki.aprobados.model.syllabus.FileStudy;
import org.zeki.aprobados.model.test.Test;
import org.zeki.aprobados.model.test.Topic;
import org.zeki.aprobados.model.user.Student;
import org.zeki.aprobados.model.user.StudentTest;

import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.OffsetDateTime;
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
                List<Topic> topics = parseTopics(jsonArray);
                return new ResultService("Módulos disponibles cargados", true, topics);
            }
            return new ResultService("Error obteniendo módulos de test", false, new ArrayList<>());

        } catch (Exception e) {
            throw new SupabaseConnectionException("ERROR CONECTANDO CON EL SERVIDOR");
        }
    }

    public ResultService getTestByModule(int idModule, String jwt) {
        // GET ALL TEST BY MODULE AND USER INFO TESTS
        try {
            String url = RPC_URL + "get_tests_modulo";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("p_id_modulo", idModule);
            HttpResponse<String> response = super.postJson(url, jsonObject.toString(), jwt);

            if (response.statusCode() == 200) {

                JsonArray jsonArray = JsonParser.parseString(response.body()).getAsJsonArray();
                List<Test> tests = parseTest(jsonArray);
                return new ResultService("Mostrando test disponibles del módulo seleccionado", tests, true);
            }
            return new ResultService("Error obteniendo los test del módulo seleccionado", new ArrayList<>(), false);

        } catch (Exception e) {
            throw new SupabaseConnectionException("ERROR CONECTANDO CON EL SERVIDOR");
        }
    }

    public ResultService getContentByModule(ModuleStudyDto dto, String jwt) {
        // GET ALL STUDY FILES ABOUT TOPIC SELECTED
        try {
            String url = RPC_URL + "get_temario_modulo";

            JsonObject body = new JsonObject();
            body.addProperty("p_id_modulo", dto.getIdModule());
            body.addProperty("p_año", dto.getYearStudy());
            body.addProperty("p_es_oficial", dto.isOfficial());

            HttpResponse<String> response = super.postJson(url, body.toString(), jwt);

            if (response.statusCode() == 200) {
                JsonArray array = JsonParser.parseString(response.body()).getAsJsonArray();
                if (array.isEmpty()) return new ResultService("No hay contenido para este tema todavía", false);

                List<FileStudy> fileStudyList = parseSyllabus(array, dto.getIdModule());
                return new ResultService(fileStudyList, "Mostrando contenido", true);
            }
            return new ResultService("Error al obtener el contenido", false);

        } catch (Exception e) {
            throw new SupabaseConnectionException("ERROR CONECTANDO CON EL SERVIDOR");
        }
    }

    // ----------- PRIVATE METHODS -------------
    private List<FileStudy> parseSyllabus(JsonArray jsonArray, int moduleID) {
        // PARSE SYLLABUS FROM SELECTED MODULE
        List<FileStudy> fileStudyList = new ArrayList<>();
        jsonArray.forEach(item -> {
            JsonObject object = item.getAsJsonObject();

            int idSyllabus = object.get("id_temario").getAsInt();
            String unity = object.get("unidad").getAsString();
            String url = object.get("url_contenido").getAsString();
            int studyYear = object.get("año").getAsInt();
            boolean official = object.get("es_oficial").getAsBoolean();

            fileStudyList.add(new FileStudy(idSyllabus, moduleID, unity, url, studyYear, official));
        });
        return fileStudyList;
    }

    private List<Test> parseTest(JsonArray jsonArray) {

        List<Test> tests = new ArrayList<>();
        List<StudentTest> studentTestsList = new ArrayList<>();
        // GET ALL DATA TEST AVAILABLE
        jsonArray.forEach(item -> {
            JsonObject object = item.getAsJsonObject();
            // CREATE BASE TEST
            int idTest = object.get("id_test").getAsInt();
            String nameTest = object.get("nombre_test").getAsString();
            Test test = new Test(idTest, nameTest);
            // IF USER HAS DONE A TEST GET LAST ATTEMPT
            if (!object.get("ultimo_intento").isJsonNull()) {

                JsonObject lastAttempt = object.getAsJsonObject("ultimo_intento").getAsJsonObject();
                int lastErrors = lastAttempt.get("errores").getAsInt();
                int lastRight = lastAttempt.get("aciertos").getAsInt();
                double lastNote = lastAttempt.get("nota").getAsDouble();
                OffsetDateTime dateTime = OffsetDateTime.parse(lastAttempt.get("fecha").getAsString());
                LocalDate lastDate = dateTime.toLocalDate();

                studentTestsList.add(new StudentTest(idTest, lastErrors, lastRight, lastNote, lastDate));
            }
            tests.add(test);
        });
        ((Student) SessionManager.getInstance().getCurrentUser()).setDoneTest(studentTestsList);
        return tests;
    }

    private List<Topic> parseTopics(JsonArray jsonArray) {

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
