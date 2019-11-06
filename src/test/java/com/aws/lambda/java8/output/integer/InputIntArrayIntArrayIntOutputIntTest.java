package com.aws.lambda.java8.output.integer;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.aws.lambda.java8.output.integer.InputIntArrayIntArrayIntOutputInt;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class InputIntArrayIntArrayIntOutputIntTest {

	private static Object input;

	@BeforeClass
	public static void createInput() throws IOException {
		String jsonString = "{\"tests\":[{\"A\":[1,2,3,4,5],\"B\":[1,2,3],\"K\":3,\"output\":45}"
				+ ", {\"A\":[1],\"B\":[3,4,5],\"K\":3,\"output\":9}" + ", {\"A\":[1,2,3],\"B\":[4,5,6],\"K\":1,\"output\":9}]}";
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
		InputIntArrayIntArrayIntOutputInt handler = new InputIntArrayIntArrayIntOutputInt();
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
