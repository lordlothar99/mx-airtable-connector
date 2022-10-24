package jsonstructure.json.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import jsonstructure.proxies.JSONInteger;

import java.io.IOException;

public class JSONIntegerSerializer extends JsonSerializer<JSONInteger> {
    @Override
    public void serialize(JSONInteger jsonInteger, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(jsonInteger.getValue());
    }
}
