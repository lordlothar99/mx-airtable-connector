package jsonstructure.json.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.mendix.core.Core;
import com.mendix.core.CoreException;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.systemwideinterfaces.core.IMendixObject;
import jsonstructure.proxies.JSONObject;
import jsonstructure.proxies.JSONProperty;
import jsonstructure.proxies.JSONValue;

import java.io.IOException;
import java.util.List;

public class JSONObjectSerializer extends JsonSerializer<JSONObject> {
    @Override
    public void serialize(JSONObject jsonObject, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        IContext context = (IContext) serializerProvider.getAttribute("context");

        jsonGenerator.writeStartObject();

        List<IMendixObject> properties = Core.retrieveByPath(context, jsonObject.getMendixObject(), JSONProperty.MemberNames.object_properties.toString());

        properties.forEach(iMendixObject -> {
            JSONProperty jsonProperty = JSONProperty.initialize(context, iMendixObject);

            try {
                JSONValue jsonPropertyValue = jsonProperty.getproperty_value();
                serializerProvider.defaultSerializeField(jsonProperty.getName(), jsonPropertyValue, jsonGenerator);
            } catch (IOException | CoreException e) {
                e.printStackTrace();
            }
        });

        jsonGenerator.writeEndObject();
    }
}
