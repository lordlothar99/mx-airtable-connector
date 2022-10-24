package jsonstructure.json.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import jsonstructure.proxies.JSONDecimal;

import java.io.IOException;

public class JSONDecimalSerializer extends JsonSerializer<JSONDecimal> {
    @Override
    public void serialize(JSONDecimal jsonDecimal, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(jsonDecimal.getValue());
    }
}
