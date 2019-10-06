package se.anders_raberg.gson_test;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class GsonTest {

    private interface Alpha {
    }

    private static class Beta implements Alpha {
        private final Integer _x;
        private final Double _y;

        public Beta(Integer x, Double y) {
            _x = x;
            _y = y;
        }

        @Override
        public String toString() {
            return "Beta [_x=" + _x + ", _y=" + _y + "]";
        }

    }

    private static class Gamma implements Alpha {
        private final Boolean _b;
        private final Double _y;

        public Gamma(Boolean b, Double y) {
            _b = b;
            _y = y;
        }

        @Override
        public String toString() {
            return "Gamma [_b=" + _b + ", _y=" + _y + "]";
        }

    }

    private static class Wrapper {
        private final List<Alpha> _alphas;

        public Wrapper(Alpha... alphas) {
            _alphas = Arrays.asList(alphas);
        }

        @Override
        public String toString() {
            return "Wrapper [_alphas=" + _alphas + "]";
        }

    }

    private static class AlphaAdapter implements JsonSerializer<Alpha>, JsonDeserializer<Alpha> {
        private static final String PROPERTIES = "properties";
        private static final String TYPE = "type";

        @Override
        public JsonElement serialize(Alpha src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject result = new JsonObject();
            result.add(TYPE, new JsonPrimitive(src.getClass().getName()));
            result.add(PROPERTIES, context.serialize(src, src.getClass()));
            return result;
        }

        @Override
        public Alpha deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
            JsonObject jsonObject = json.getAsJsonObject();
            String type = jsonObject.get(TYPE).getAsString();
            JsonElement element = jsonObject.get(PROPERTIES);

            if (Beta.class.getName().equals(type)) {
                return context.deserialize(element, Beta.class);
            } else if (Gamma.class.getName().equals(type)) {
                return context.deserialize(element, Gamma.class);
            } else {
                throw new JsonParseException("Unknown type: " + type);
            }
        }

    }

    public static void main(String[] args) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Alpha.class, new AlphaAdapter());
        Gson gson = gsonBuilder.create();

        String json = gson.toJson(new Wrapper(new Beta(42, 3.141592), new Gamma(true, 2.71)));
        System.out.println(json);

        Wrapper fromJson = gson.fromJson(json, Wrapper.class);

        System.out.println(fromJson);
    }

}
