package com.aws.lambda.java8.output.integer;

import java.util.Arrays;
import java.util.function.BiFunction;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class InputIntArrayIntArrayOutputInt implements RequestHandler<Object, Integer> {

	@Override
	public Integer handleRequest(Object input, Context context) {
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
		JsonArray inputB = jsonObject.get("B").getAsJsonArray();
		int[] B = new Gson().fromJson(inputB, int[].class);

		long start1 = System.nanoTime();
		int sol1 = functionGetSolution.apply(A, B);
		long end1 = System.nanoTime();
		Double time1 = Math.round(((end1 - (double) start1) / 1000000000) * 100000d) / 100000d;

		context.getLogger().log("Solution 1: " + time1 + " sec");
		context.getLogger().log("--------------------------");
		return sol1;
	}

	BiFunction<int[], int[], Integer> functionGetSolution = (A, B) -> {
		Integer[] arrayA = Arrays.stream(A).boxed().toArray(Integer[]::new);
		Integer[] arrayB = Arrays.stream(B).boxed().toArray(Integer[]::new);

		return arrayA.length * arrayB.length;
	};
}
