package com.aws.lambda.java8.output.array;

import java.util.stream.IntStream;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class InputIntArrayIntArrayStringOutputStringArray implements RequestHandler<Object, Object[]> {

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
		JsonArray inputB = jsonObject.get("B").getAsJsonArray();
		int[] B = new Gson().fromJson(inputB, int[].class);
		String S = jsonObject.get("S").getAsString();

		long start1 = System.nanoTime();
		Object[] sol1 = functionGetSolution.apply(A, B, S);
		long end1 = System.nanoTime();

		context.getLogger().log("Solution 1: " + (end1 - start1) + " nano");
		context.getLogger().log("--------------------------");
		return sol1;
	}

	@FunctionalInterface
	interface FunctionSolution<T, K, P, O> {
		public O apply(T t, K k, P p);
	}

	FunctionSolution<int[], int[], String, Object[]> functionGetSolution = (A, B, S) -> {
		Object[] arrayA = IntStream.range(0, A.length)
				.mapToObj(i -> new StringBuffer(S).reverse().append(A[i] * B[i]).toString()).toArray();
		return arrayA;
	};

}
