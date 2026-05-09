package org.zeki.aprobados.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.zeki.aprobados.app.SessionManager;
import org.zeki.aprobados.dto.StudentStatistDto;
import org.zeki.aprobados.dto.UserLoginDto;
import org.zeki.aprobados.dto.UserSignupDto;
import org.zeki.aprobados.exception.SupabaseConnectionException;
import org.zeki.aprobados.model.user.Student;
import org.zeki.aprobados.model.user.User;
import org.zeki.aprobados.model.user.UserFactory;

import java.net.http.HttpResponse;

public class UserService extends SupabaseClient {

    public ResultService signUp(UserSignupDto userDto) throws SupabaseConnectionException {
        // CREATE JSON AND SEND TO DB
        try {
            String url = AUTH_URL + "signup";
            JsonObject jsonData = createJsonForSignUP(userDto);

            HttpResponse<String> response = super.postJson(url, String.valueOf(jsonData));
            int status = response.statusCode();
            String body = response.body();

            if (status == 200) {
                // CHECK CREATED
                if (body.contains("access_token"))
                    return new ResultService("Registro realizado con éxito, ya puede acceder al sistema", true);
                else if (body.contains("email_verified"))
                    return new ResultService("Registro realizado con éxito, revisa el email para activar tu cuenta", true);
                else return new ResultService("El email introducido ya está en uso", false);
            } else if (status == 422) {
                return new ResultService("Error de registro, los datos introducido no son válidos", false);
            } else {
                return new ResultService("Error de acceso al servidor", false);
            }

        } catch (Exception e) {
            throw new SupabaseConnectionException("ERROR ENVIANDO DATOS AL ENDPOINT DEL SERVIDOR");
        }
    }

    public ResultService login(UserLoginDto userDto) {
        // CHECK AUTH_DB
        ResultService resultLogin = checkCredentials(userDto);
        if (!resultLogin.isSuccess()) return resultLogin;
        // GET USER DATA
        try {
            String url = RPC_URL + "get_perfil_usuario";
            String jwt = resultLogin.getMessage();
            HttpResponse<String> response = super.getJson(url, jwt);
            JsonObject profileJson = JsonParser.parseString(response.body()).getAsJsonObject();

            User currentUser = createLoginUser(profileJson, jwt);
            SessionManager.getInstance().logIn(currentUser);
            return new ResultService("Login ok", true);

        } catch (Exception e) {
            throw new SupabaseConnectionException("ERROR ENVIANDO DATOS AL SERVIDOR");
        }
    }

    public ResultService uploadStudentStatist(String jwt) {
        try {
            String url = RPC_URL + "get_estadisticas_usuario";
            HttpResponse<String> response = super.getJson(url, jwt);

            if (response.statusCode() == 200) {
                JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
                StudentStatistDto statistDto = parseStatistStudent(json);
                SessionManager.getInstance().getStudent().reloadStatist(statistDto);
                return new ResultService("Estadísticas del estudiante actualizadas", true);
            }
            return new ResultService("Error actualizando estadísticas del estudiante", false);

        } catch (Exception e) {
            throw new SupabaseConnectionException("ERROR ENVIANDO DATOS AL SERVIDOR");
        }
    }

    // ----------- PRIVATE METHODS -------------
    private StudentStatistDto parseStatistStudent(JsonObject json) {

        int completedTest = json.get("test_completados").getAsInt();
        int rightAnswers = json.get("preguntas_correctas").getAsInt();
        int wrongAnswers = json.get("preguntas_incorrectas").getAsInt();
        int reviewQuestions = json.get("preguntas_pendientes").getAsInt();

        return new StudentStatistDto(completedTest, rightAnswers, wrongAnswers, reviewQuestions);
    }

    private ResultService checkCredentials(UserLoginDto userDto) {
        // CHECK MATCHES EMAIL - PASS IN DB
        try {
            String url = AUTH_URL + "token?grant_type=password";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("email", userDto.getEmail());
            jsonObject.addProperty("password", userDto.getPassword());

            HttpResponse<String> response = super.postJson(url, jsonObject.toString());

            if (response.statusCode() != 200) {
                return new ResultService("Credenciales incorrectas", false);
            }

            JsonObject loginJson = JsonParser.parseString(response.body()).getAsJsonObject();
            return new ResultService(loginJson.get("access_token").getAsString(), true);

        } catch (Exception e) {
            throw new SupabaseConnectionException("ERROR ENVIANDO DATOS AL ENDPOINT DEL SERVIDOR");
        }
    }

    private User createLoginUser(JsonObject profile, String jwt) {

        String idUser = profile.get("id_usuario").getAsString();
        String name = profile.get("nombre").getAsString();
        String lastName = profile.get("apellidos").getAsString();
        String study = profile.get("estudios").getAsString();
        String role = profile.get("rol").getAsString();

        UserFactory factory = new UserFactory();

        if (role.equals("admin")) return factory.createAdmin(idUser, jwt, name, lastName, study);
        else {

            int testFinished = profile.get("test_completados").getAsInt();
            int rightQuestions = profile.get("preguntas_correctas").getAsInt();
            int wrongQuestions = profile.get("preguntas_incorrectas").getAsInt();
            int reviewQuestions = profile.get("preguntas_pendientes").getAsInt();

            Student student = factory.createStudent(idUser, jwt, name, lastName, study);
            student.setStudentSettings(testFinished, rightQuestions, wrongQuestions, reviewQuestions);
            return student;
        }
    }

    private JsonObject createJsonForSignUP(UserSignupDto userDto) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("email", userDto.getEmail());
        jsonObject.addProperty("password", userDto.getPassword());
        JsonObject nameObject = new JsonObject();
        nameObject.addProperty("nombre", userDto.getName());
        nameObject.addProperty("apellidos", userDto.getLastName());
        nameObject.addProperty("estudios", userDto.getStudy());

        jsonObject.add("data", nameObject);
        return jsonObject;
    }

}
