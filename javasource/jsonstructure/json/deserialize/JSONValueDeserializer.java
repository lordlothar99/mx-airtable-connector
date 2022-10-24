package jsonstructure.json.deserialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mendix.core.Core;
import com.mendix.systemwideinterfaces.core.IContext;
import jsonstructure.proxies.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Iterator;

public class JSONValueDeserializer extends JsonDeserializer<JSONValue> {
    @Override
    public JSONValue deserialize(JsonParser jp, DeserializationContext ctx) throws IOException {
        ObjectMapper objectMapper = (ObjectMapper)jp.getCodec();
        JsonNode node = objectMapper.readTree(jp);
        IContext context = (IContext) ctx.getAttribute("context");

        if (node == null)
            return null;

        switch (node.getNodeType()) {
            case OBJECT:
                JSONObject jsonObject = JSONObject.initialize(context, Core.instantiate(context, JSONObject.getType()));

                Iterator<String> fieldNames = node.fieldNames();

                while(fieldNames.hasNext()) {
                    String fieldName = fieldNames.next();
                    JsonNode fieldValue = node.get(fieldName);

                    JSONProperty jsonProperty = JSONProperty.initialize(context, Core.instantiate(context, JSONProperty.getType()));
                    jsonProperty.setName(fieldName);
                    jsonProperty.setobject_properties(jsonObject);

                    if (fieldValue != null)
                    {
                        JsonParser parser = fieldValue.traverse();
                        parser.setCodec(jp.getCodec());

                        JSONValue jsonValue = ctx.readValue(parser, JSONValue.class);
                        if (jsonValue != null)
                            jsonValue.setproperty_value(jsonProperty);
                    }
                }

                return jsonObject;
            case ARRAY:
                JSONArray jsonArray = JSONArray.initialize(context, Core.instantiate(context, JSONArray.getType()));

                for(int i = 0; i < node.size(); i++) {
                    JsonParser parser = node.get(i).traverse();
                    parser.setCodec(jp.getCodec());
                    JSONObject arrayItem = (JSONObject)ctx.readValue(parser, JSONValue.class);
                    arrayItem.setOrder((long) i);
                    arrayItem.setarray_items(jsonArray);
                }

                return jsonArray;
            case BOOLEAN:
                JSONBoolean jsonBoolean = JSONBoolean.initialize(context, Core.instantiate(context, JSONBoolean.getType()));
                jsonBoolean.setValue(node.booleanValue());
                return jsonBoolean;
            case NUMBER:
                if (node.isIntegralNumber()) {
                    JSONInteger jsonInteger = JSONInteger.initialize(context, Core.instantiate(context, JSONInteger.getType()));
                    jsonInteger.setValue(node.intValue());
                    return jsonInteger;
                } else if (node.isFloatingPointNumber()) {
                    JSONDecimal jsonDecimal = JSONDecimal.initialize(context, Core.instantiate(context, JSONDecimal.getType()));
                    jsonDecimal.setValue(node.isBigDecimal() ? node.decimalValue() : BigDecimal.valueOf(node.floatValue()));
                    return jsonDecimal;
                }
            case STRING:
                JSONString jsonString = JSONString.initialize(context, Core.instantiate(context, JSONString.getType()));
                jsonString.setValue(node.textValue());
                return jsonString;
            case MISSING:
            case NULL:
                return null;
            case BINARY:
                throw new RuntimeException("Deserializing binary properties is not supported");
            case POJO:
                throw new RuntimeException("Deserializing pojo properties is not supported");
            default:
                throw new RuntimeException("Unknown type: "+node.getNodeType());
        }
    }
}
