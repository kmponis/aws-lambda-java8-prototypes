package com.aws.lambda.java8.sample;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.function.Supplier;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
		String jsonString = "{\"tests\":[{\"S\":\"aassdf\",\"N\":50,\"A\":[1,2],\"SA\":[\"Angela\",\"Aaron\",\"Bob\",\"Claire\",\"David\",\"Bob\"],\"output\":[100]}"
				+ ", {\"S\":\"assdddf\",\"N\":51, \"A\":[1,2],\"SA\":[\"Angela\"],\"output\":[102]}"
				+ ", {\"S\":\"asddfffff\",\"N\":52, \"A\":[1,2],\"SA\":[\"\"],\"output\":[104]}]}";
		System.out.println(MethodHandles.lookup().lookupClass().getSimpleName());
		System.out.println(jsonString);

		createGSONJsonFunction.get();
		input = jsonString;
	}

	private static Supplier<String> createGSONJsonFunction = () -> {

		// create the albums object
		JsonObject albums = new JsonObject();
		// add a property calle title to the albums object
		albums.addProperty("title", "album1");

		// create an array called datasets
		JsonArray datasets = new JsonArray();

		// create a dataset
		JsonObject dataset = new JsonObject();
		// add the property album_id to the dataset
		dataset.addProperty("album_id", 1);
		// add the property album_year to the dataset
		dataset.addProperty("album_year", 1996);

		datasets.add(dataset);

		albums.add("dataset", datasets);

		// create the gson using the GsonBuilder. Set pretty printing on. Allow
		// serializing null and set all fields to the Upper Camel Case
		Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls()
				.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
		/*
		 * prints { "title": "album1", "dataset": [ { "album_id": 1, "album_year": 1996
		 * } ] }
		 */
		System.out.println(gson.toJson(albums));
		return gson.toJson(albums);
	};

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
