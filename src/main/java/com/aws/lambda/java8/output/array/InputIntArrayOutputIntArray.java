package com.aws.lambda.java8.output.array;

import java.util.Arrays;
import java.util.function.Function;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class InputIntArrayOutputIntArray implements RequestHandler<Object, Object[]> {

	@Override
	public Object[] handleRequest(Object input, Context context) {
		context.getLogger().log("--------------------------");
		context.getLogger().log("Input: " + input);
		JsonObject jsonObject;
		try {
			JsonParser parser = new JsonParser();
			JsonElement jsonElement = parser.parse(input.toString());
			jsonObject = jsonElement.getAsJsonObject();
		} catch (Exception ex) {
			context.getLogger().log("Use GsonBuilder to parse a JSON object");
			Gson gson = new GsonBuilder().create();
			JsonElement jsonElement = gson.toJsonTree(input);
			jsonObject = jsonElement.getAsJsonObject();
		}
		JsonArray inputA = jsonObject.get("A").getAsJsonArray();
		int[] A = new Gson().fromJson(inputA, int[].class);

		long start1 = System.nanoTime();
		Integer[] sol1 = functionGetSolution.apply(A);
		long end1 = System.nanoTime();
		Double time1 = Math.round(((end1 - (double) start1) / 1000000000) * 100000d) / 100000d;

		context.getLogger().log("Solution 1: " + time1 + " sec");
		context.getLogger().log("--------------------------");
		return sol1;
	}

	Function<int[], Integer[]> functionGetSolution = A -> {
		Integer[] arrayA = Arrays.stream(A).boxed().toArray(Integer[]::new);

		return arrayA;
	};

}
