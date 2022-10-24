package jsonstructure.json.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.mendix.core.Core;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.systemwideinterfaces.core.IMendixObject;
import jsonstructure.proxies.*;

import java.io.IOException;
import java.util.List;

public class JSONArraySerializer extends JsonSerializer<JSONArray> {
    @Override
    public void serialize(JSONArray jsonArray, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        IContext context = (IContext) serializerProvider.getAttribute("context");

        jsonGenerator.writeStartArray();

        List<IMendixObject> items = Core.retrieveByPath(context, jsonArray.getMendixObject(), JSONObject.MemberNames.array_items.toString());

        items.forEach(iMendixObject -> {
            JSONObject jsonObject = JSONObject.initialize(context, iMendixObject);

            try {
                serializerProvider.defaultSerializeValue(jsonObject, jsonGenerator);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        jsonGenerator.writeEndArray();
    }
}
