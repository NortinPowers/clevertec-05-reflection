package by.clevertec.reflection;

import static by.clevertec.reflection.converter.JsonSerializer.transformObjectToJson;
import static by.clevertec.reflection.util.BaseBuilder.getBase;

import by.clevertec.reflection.converter.CustomJsonDeserializer;
import by.clevertec.reflection.entity.Base;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class Main {

    public static void main(String[] args) {
        Base base = getBase();

        String jsonString = transformObjectToJson(base);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement jsonElement = JsonParser.parseString(jsonString);
        String prettyJsonString = gson.toJson(jsonElement);
        System.out.println("Convert to json:\n" + prettyJsonString);

        System.out.println("\n________________\n");

        CustomJsonDeserializer jsonDeserializer = new CustomJsonDeserializer();
        Base converted = jsonDeserializer.convertJsonToBase(jsonString);
        System.out.println("Convert to object:\n" + converted);
    }
}
