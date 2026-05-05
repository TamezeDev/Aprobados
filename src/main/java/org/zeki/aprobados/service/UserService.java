package org.zeki.aprobados.service;

import com.google.gson.JsonObject;
import org.zeki.aprobados.dto.UserSignupDto;
import org.zeki.aprobados.exception.SupabaseConnectionException;

import java.net.http.HttpResponse;

public class UserService extends SupabaseClient {

    public ResultService signUp(UserSignupDto userDto) {
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

    // ----------- PRIVATE METHODS -------------

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
