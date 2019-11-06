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

public class InputIntArrayIntOutputInt implements RequestHandler<Object, Integer> {

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
		int K = jsonObject.get("K").getAsInt();

		long start1 = System.nanoTime();
		int sol1 = functionGetSolution.apply(A, K);
		long end1 = System.nanoTime();

		context.getLogger().log("Solution 1: " + (end1 - start1) + " nano");
		context.getLogger().log("--------------------------");
		return sol1;
	}

	BiFunction<int[], Integer, Integer> functionGetSolution = (A, K) -> {
		Integer[] arrayA = Arrays.stream(A).boxed().toArray(Integer[]::new);

		return arrayA.length * K;
	};

}
