// This file was generated by Mendix Studio Pro.
//
// WARNING: Only the following code will be retained when actions are regenerated:
// - the import list
// - the code between BEGIN USER CODE and END USER CODE
// - the code between BEGIN EXTRA CODE and END EXTRA CODE
// Other code you write will be lost the next time you deploy the project.
// Special characters, e.g., é, ö, à, etc. are supported in comments.

package jsonstructure.actions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.webui.CustomJavaAction;
import com.mendix.systemwideinterfaces.core.IMendixObject;
import jsonstructure.json.deserialize.JSONValueDeserializer;
import jsonstructure.proxies.*;

/**
 * Convert any JSON string into a generic JSON structure
 */
public class Deserialize extends CustomJavaAction<IMendixObject>
{
	private java.lang.String JSON;

	public Deserialize(IContext context, java.lang.String JSON)
	{
		super(context);
		this.JSON = JSON;
	}

	@java.lang.Override
	public IMendixObject executeAction() throws Exception
	{
		// BEGIN USER CODE
		ObjectMapper mapper = new ObjectMapper();

		SimpleModule module = new SimpleModule();
		module.addDeserializer(JSONValue.class, new JSONValueDeserializer());
		mapper.registerModule(module);

		ObjectReader reader = mapper.readerFor(JSONValue.class).withAttribute("context", getContext());
		JSONValue jsonValue = reader.readValue(this.JSON);

		return jsonValue != null ? jsonValue.getMendixObject() : null;
		// END USER CODE
	}

	/**
	 * Returns a string representation of this action
	 */
	@java.lang.Override
	public java.lang.String toString()
	{
		return "Deserialize";
	}

	// BEGIN EXTRA CODE
	// END EXTRA CODE
}
