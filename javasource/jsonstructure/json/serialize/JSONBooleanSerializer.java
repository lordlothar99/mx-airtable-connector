package jsonstructure.json.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import jsonstructure.proxies.JSONBoolean;

import java.io.IOException;

public class JSONBooleanSerializer extends JsonSerializer<JSONBoolean> {
    @Override
    public void serialize(JSONBoolean jsonBoolean, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeBoolean(jsonBoolean.getValue());
    }
}
