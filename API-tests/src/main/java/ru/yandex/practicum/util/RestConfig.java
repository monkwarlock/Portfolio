package ru.yandex.practicum.util;

import java.util.ArrayList;
import java.util.List;

public class RestConfig {
    public static final String HOST = "https://stellarburgers.education-services.ru/";
    public static final String DOMAIN_YANDEX = "@yandex.ru";
    public static final String INVALID_EMAIL = "fghdf@yandex.ru";
    public static final String INVALID_PASSWORD = "Dhhre234";

    public static String getOrderCreatingBody(String name, Integer number){
        return "{\n" +
                "    \"name\": " + "\"" + name + "\"" + ",\n" +
                "    \"order\": {\n" +
                "        \"number\": " + number + "\n" +
                "    },\n" +
                "    \"success\": true\n" +
                "}";
    }

    public static String getUserCreatingBody(String accessToken, String refreshToken,
                                             String email, String name){
        return "{\n" +
                "    \"success\": true,\n" +
                "    \"user\": {\n" +
                "        \"email\": \"" + email + "\",\n" +
                "        \"name\": \"" + name + "\"\n" +
                "    },\n" +
                "    \"accessToken\": \"" + accessToken + "\",\n" +
                "    \"refreshToken\": \"" + refreshToken + "\"\n" +
                "}";
    }

    public static String getUserLoginBody(String accessToken, String refreshToken,
                                          String email, String name){
        return "{\n" +
                "    \"success\": true,\n" +
                "    \"accessToken\": \"" + accessToken + "\",\n" +
                "    \"refreshToken\": \"" + refreshToken + "\",\n" +
                "    \"user\": {\n" +
                "        \"email\": \"" + email + "\",\n" +
                "        \"name\": \"" + name + "\"\n" +
                "    }\n" +
                "}";
    }

    public static List<String> getIngredientsWithInvalidHash () {
        List<String> ingredients = new ArrayList<>();
        ingredients.add(0, "Ghjsdf515");
        ingredients.add(1, "Ebu1r9TUHG");
        return ingredients;
    }
}