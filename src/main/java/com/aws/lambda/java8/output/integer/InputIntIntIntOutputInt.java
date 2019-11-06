package com.aws.lambda.java8.output.integer;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class InputIntIntIntOutputInt implements RequestHandler<Object, Integer> {

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
		int A = jsonObject.get("A").getAsInt();
		int B = jsonObject.get("B").getAsInt();
		int C = jsonObject.get("C").getAsInt();

		long start1 = System.nanoTime();
		int sol1 = functionGetSolution.apply(A, B, C);
		long end1 = System.nanoTime();

		context.getLogger().log("Solution 1: " + (end1 - start1) + " nano");
		context.getLogger().log("--------------------------");
		return sol1;
	}

	@FunctionalInterface
	public interface FunctionSolution<T, U, V, R> {
		public R apply(T t, U u, V v);
	}

	FunctionSolution<Integer, Integer, Integer, Integer> functionGetSolution = (A, B, C) -> {
		Integer[] arrayA = (A + "").chars().mapToObj(a -> a - '0').toArray(Integer[]::new);
		Integer[] arrayB = (B + "").chars().mapToObj(b -> b - '0').toArray(Integer[]::new);

		return arrayA.length * arrayB.length * C;
	};

}
