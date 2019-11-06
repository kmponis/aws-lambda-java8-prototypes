package com.aws.lambda.java8.output.integer;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.aws.lambda.java8.output.integer.InputIntArrayOutputInt;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class InputIntArrayOutputIntTest {

	private static Object input;

	@BeforeClass
	public static void createInput() throws IOException {
		String jsonString = "{\"tests\":[{\"A\":[1,2,3,4,5],\"output\":5}" + ", {\"A\":[1],\"output\":1}"
				+ ", {\"A\":[123],\"output\":3}]}";
		System.out.println(MethodHandles.lookup().lookupClass().getSimpleName());
		System.out.println(jsonString);

		input = jsonString;
	}

	private Context createContext() {
		TestContext ctx = new TestContext();
		ctx.setFunctionName(MethodHandles.lookup().lookupClass().getSimpleName());
		return ctx;
	}

	@Test
	public void testSample() {
		InputIntArrayOutputInt handler = new InputIntArrayOutputInt();
		Context ctx = createContext();

		JsonParser parser = new JsonParser();
		JsonElement jsonElement = parser.parse((String) input);
		JsonObject rootObject = jsonElement.getAsJsonObject();
		JsonArray testsArray = rootObject.get("tests").getAsJsonArray();

		for (int i = 0; i < testsArray.size(); i++) {
			JsonObject object = testsArray.get(i).getAsJsonObject();

			Integer expected = object.get("output").getAsInt();
			Integer output = handler.handleRequest(object, ctx);
			Assert.assertEquals(expected, output);
		}
	}
}
