package com.aws.lambda.java8.json;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class SampleOutputInt implements RequestHandler<Object, Integer> {

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
		int N = jsonObject.get("N").getAsInt();
		int[] NA = new Gson().fromJson(jsonObject.get("NA").getAsJsonArray(), int[].class);
		String S = jsonObject.get("S").getAsString();
		String[] SA = new Gson().fromJson(jsonObject.get("SA").getAsJsonArray(), String[].class);

		long start1 = System.nanoTime();
		Integer sol1 = getSolution(N, NA, S, SA);
		long end1 = System.nanoTime();
		Double time1 = Math.round(((end1 - (double) start1) / 1000000000) * 100000d) / 100000d;

		context.getLogger().log("Solution 1: " + sol1 + " time " + time1 + " sec");
		context.getLogger().log("--------------------------");
		return sol1;
	}

	private Integer getSolution(Integer N, int[] NA, String S, String[] SA) {
		// Integer
		Integer[] arrayN = (N + "").chars().mapToObj(n -> n - '0').toArray(Integer[]::new);
		System.out.println("Integer -> Integer[]: " + Arrays.toString(arrayN));

		// int[]
		Integer[] arrayNA = Arrays.stream(NA).boxed().toArray(Integer[]::new);
		System.out.println("int[] -> Integer[]: " + Arrays.toString(arrayNA));

		// String
		String[] reverseArrayS = new StringBuilder(S).reverse().chars().mapToObj(s -> (char) s + "").toArray(String[]::new);
		System.out.println("String -> Reverse String[]: " + Arrays.toString(reverseArrayS));

		// String[]
		Map<String, Integer> mapSA = Arrays.stream(SA).distinct().collect(Collectors.toMap(s -> s, String::length));
		System.out.println("String[] -> Distinct Map<String, Long>: " + Arrays.asList(mapSA));

		return N * NA.length;
	}

}
