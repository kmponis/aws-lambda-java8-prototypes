package com.aws.lambda.java8.sample;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class SampleTest {

	private static Object input;

	@BeforeClass
	public static void createInput() throws IOException {
		String jsonString = "{\"tests\":[{\"S\":\"aassdf\",\"N\":50, \"A\":[1,2],\"output\":[100]}"
				+ ", {\"S\":\"assdddf\",\"N\":51, \"A\":[1,2],\"output\":[102]}"
				+ ", {\"S\":\"asddfffff\",\"N\":52, \"A\":[1,2],\"output\":[104]}]}";
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
		Sample handler = new Sample();
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
