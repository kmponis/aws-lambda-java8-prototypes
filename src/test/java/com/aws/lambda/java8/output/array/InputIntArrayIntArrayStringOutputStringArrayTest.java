package com.aws.lambda.java8.output.array;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.aws.lambda.java8.output.array.InputIntArrayIntArrayStringOutputStringArray;
import com.aws.lambda.java8.output.integer.TestContext;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class InputIntArrayIntArrayStringOutputStringArrayTest {

	private static Object input;

	@BeforeClass
	public static void createInput() throws IOException {
		String jsonString = "{\"tests\":[{\"A\":[1,2,3,4,5],\"B\":[2,3,4,5,6],\"S\":\"pame\",\"output\":[\"emap2\",\"emap6\",\"emap12\",\"emap20\",\"emap30\"]}"
				+ ", {\"A\":[1],\"B\":[2],\"S\":\"ola\",\"output\":[\"alo2\"]}"
				+ ", {\"A\":[123],\"B\":[3],\"S\":\"0\",\"output\":[\"0369\"]}]}";
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
		InputIntArrayIntArrayStringOutputStringArray handler = new InputIntArrayIntArrayStringOutputStringArray();
		Context ctx = createContext();

		JsonParser parser = new JsonParser();
		JsonElement jsonElement = parser.parse((String) input);
		JsonObject rootObject = jsonElement.getAsJsonObject();
		JsonArray testsArray = rootObject.get("tests").getAsJsonArray();

		for (int i = 0; i < testsArray.size(); i++) {
			JsonObject object = testsArray.get(i).getAsJsonObject();

			Object[] expected = new Gson().fromJson(object.get("output").getAsJsonArray(), Object[].class);
			Object[] output = handler.handleRequest(object, ctx);
			Assert.assertArrayEquals(expected, output);
		}
	}

}
