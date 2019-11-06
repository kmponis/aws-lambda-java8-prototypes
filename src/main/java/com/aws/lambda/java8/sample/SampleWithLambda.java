package com.aws.lambda.java8.sample;

import java.util.Arrays;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class SampleWithLambda implements RequestHandler<Object, Object[]> {

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
		int N = jsonObject.get("N").getAsInt();
		JsonArray inputA = jsonObject.get("A").getAsJsonArray();
		int[] A = new Gson().fromJson(inputA, int[].class);

		long start1 = System.nanoTime();
		Object[] sol1 = functionGetSolution.apply(Integer.valueOf(N), Arrays.stream(A).boxed().toArray(Integer[]::new));
		long end1 = System.nanoTime();

		context.getLogger().log("Solution 1: " + (end1 - start1) + " nano");
		context.getLogger().log("--------------------------");
		return sol1;
	}

	@FunctionalInterface
	interface FunctionSolution<N, A, O> {
		public O apply(N n, A a);
	}

	FunctionSolution<Integer, Integer[], Integer[]> functionGetSolution = (n, a) -> {
		return new Integer[] { n * a.length };
	};

}
