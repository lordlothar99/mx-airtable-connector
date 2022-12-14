// This file was generated by Mendix Studio Pro.
//
// WARNING: Only the following code will be retained when actions are regenerated:
// - the import list
// - the code between BEGIN USER CODE and END USER CODE
// - the code between BEGIN EXTRA CODE and END EXTRA CODE
// Other code you write will be lost the next time you deploy the project.
// Special characters, e.g., é, ö, à, etc. are supported in comments.

package jsonstructure.actions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mendix.core.Core;
import com.mendix.core.CoreException;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.webui.CustomJavaAction;
import com.mendix.systemwideinterfaces.core.IMendixObject;
import jsonstructure.proxies.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * Convert a generic JSON structure to its text representation.
 * Normally, you would pass either a JSON Object entity or a JSON Array entity. However, it is possible to serialize other JSON values as well.
 * 
 */
public class Serialize extends CustomJavaAction<java.lang.String>
{
	private IMendixObject __JSONValue;
	private jsonstructure.proxies.JSONValue JSONValue;
	private java.lang.Boolean SkipEmpty;
	private java.lang.Boolean Pretty;

	public Serialize(IContext context, IMendixObject JSONValue, java.lang.Boolean SkipEmpty, java.lang.Boolean Pretty)
	{
		super(context);
		this.__JSONValue = JSONValue;
		this.SkipEmpty = SkipEmpty;
		this.Pretty = Pretty;
	}

	@java.lang.Override
	public java.lang.String executeAction() throws Exception
	{
		this.JSONValue = __JSONValue == null ? null : jsonstructure.proxies.JSONValue.initialize(getContext(), __JSONValue);

		// BEGIN USER CODE
		ObjectMapper mapper = new ObjectMapper();

		if (this.SkipEmpty) {
			mapper.setSerializationInclusion(JsonInclude.Include.NON_ABSENT)
				.setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
				.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		}

		JsonNode jsonNode = traverse(getContext(), this.JSONValue);

		ObjectWriter writer = this.Pretty ? mapper.writerWithDefaultPrettyPrinter() : mapper.writer();
		return writer.withAttribute("context", getContext()).writeValueAsString(jsonNode);
		// END USER CODE
	}

	/**
	 * Returns a string representation of this action
	 */
	@java.lang.Override
	public java.lang.String toString()
	{
		return "Serialize";
	}

	// BEGIN EXTRA CODE
	private JsonNode traverse(IContext context, JSONValue jsonValue) {
		JsonNodeFactory jsonNodeFactory = JsonNodeFactory.withExactBigDecimals(true);

		if (jsonValue == null)
			return jsonNodeFactory.nullNode();

		if (JSONObject.class.equals(jsonValue.getClass())) {
			ObjectNode objectNode = jsonNodeFactory.objectNode();

			List<IMendixObject> properties = Core.retrieveByPath(context, jsonValue.getMendixObject(), JSONProperty.MemberNames.object_properties.toString());

			properties.forEach(iMendixObject -> {
				JSONProperty jsonProperty = JSONProperty.initialize(context, iMendixObject);
				try {
					JsonNode jsonNode = traverse(context, jsonProperty.getproperty_value());

					if (!jsonNode.isNull())
						objectNode.set(jsonProperty.getName(), jsonNode);
				} catch (CoreException e) {
					e.printStackTrace();
				}
			});

			return objectNode;
		} else if (JSONArray.class.equals(jsonValue.getClass())) {
			ArrayNode arrayNode = jsonNodeFactory.arrayNode();

			List<IMendixObject> items = Core.retrieveByPath(context, jsonValue.getMendixObject(), JSONObject.MemberNames.array_items.toString());

			items.forEach(iMendixObject -> {
				JSONObject jsonObject = JSONObject.initialize(context, iMendixObject);
				arrayNode.add(traverse(context, jsonObject));
			});

			return arrayNode;
		} else if (JSONString.class.equals(jsonValue.getClass())) {
			String value = ((JSONString) jsonValue).getValue();
			return value == null ? jsonNodeFactory.nullNode() : jsonNodeFactory.textNode(value);
		} else if (JSONInteger.class.equals(jsonValue.getClass())) {
			Integer value = ((JSONInteger) jsonValue).getValue();
			return value == null ? jsonNodeFactory.nullNode() : jsonNodeFactory.numberNode(value);
		} else if (JSONDecimal.class.equals(jsonValue.getClass())) {
			BigDecimal value = ((JSONDecimal) jsonValue).getValue();
			return value == null ? jsonNodeFactory.nullNode() : jsonNodeFactory.numberNode(value);
		} else if (JSONBoolean.class.equals(jsonValue.getClass())) {
			Boolean value = ((JSONBoolean) jsonValue).getValue();
			return value == null ? jsonNodeFactory.nullNode() : jsonNodeFactory.booleanNode(value);
		} else {
			return jsonNodeFactory.nullNode();
		}
	}
	// END EXTRA CODE
}
