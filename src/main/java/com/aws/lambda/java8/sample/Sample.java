package com.aws.lambda.java8.sample;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Sample implements RequestHandler<Object, Object[]> {

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
		String S = jsonObject.get("S").getAsString();
		int N = jsonObject.get("N").getAsInt();
		JsonArray inputA = jsonObject.get("A").getAsJsonArray();
		int[] A = new Gson().fromJson(inputA, int[].class);

		long start1 = System.nanoTime();
		Object[] sol1 = getSolution(S, N, A);
		long end1 = System.nanoTime();

		context.getLogger().log("Solution 1: " + (end1 - start1) + " nano");
		context.getLogger().log("--------------------------");
		return sol1;
	}

	private Object[] getSolution(String S, int N, int[] A) {
		// String Array
		String[] arrayS = S.chars().mapToObj(s -> (char) s + "").toArray(String[]::new);
		System.out.println("String Array: " + Arrays.toString(arrayS));

		// Create Map<Character, Count>
		Map<String, Long> groupCharactersMap = Stream.of(arrayS)
				.collect(Collectors.groupingBy(s -> s, Collectors.counting()));
		Object[] groupCharactersArray = groupCharactersMap.entrySet().stream().toArray();
		System.out.println("Group Character: " + Arrays.deepToString(groupCharactersArray));

		// Reverse int
		Integer[] reverseArrayN = new StringBuffer(N + "").reverse().chars().mapToObj(n -> n - '0').toArray(Integer[]::new);
		System.out.println("Reverse int: " + Arrays.toString(reverseArrayN));

		// Iterate with limit
		Integer[] iterateIntegers = Stream.iterate(0, i -> i + 1).limit(10).toArray(Integer[]::new);
		System.out.println("Iterate with limit: " + Arrays.toString(iterateIntegers));

		// Generate Int
		int[] fibs = { 0, 1 };
		Integer[] fibonacci = Stream.generate(() -> {
			int result = fibs[1];
			int fib3 = fibs[0] + fibs[1];
			fibs[0] = fibs[1];
			fibs[1] = fib3;
			return result;
		}).skip(3).limit(10).toArray(Integer[]::new);
		System.out.println("Generate fibonacci: " + Arrays.toString(fibonacci));

		return new Object[] { N * A.length };
	}

}
