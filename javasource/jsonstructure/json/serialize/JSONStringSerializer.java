package jsonstructure.json.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import jsonstructure.proxies.*;

import java.io.IOException;

public class JSONStringSerializer extends JsonSerializer<JSONString> {
    @Override
    public void serialize(JSONString jsonString, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(jsonString.getValue());
    }
}
