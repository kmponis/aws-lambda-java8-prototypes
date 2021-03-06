package com.aws.lambda.java8.sample;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
    int N = jsonObject.get("N").getAsInt();
    int[] NA = new Gson().fromJson(jsonObject.get("NA").getAsJsonArray(), int[].class);
    String S = jsonObject.get("S").getAsString();
    String[] SA = new Gson().fromJson(jsonObject.get("SA").getAsJsonArray(), String[].class);

    long start1 = System.nanoTime();
    Object[] sol1 = getSolution(N, NA, S, SA);
    long end1 = System.nanoTime();
    Double time1 = Math.round(((end1 - (double) start1) / 1000000000) * 100000d) / 100000d;
    context.getLogger().log("Solution 1: " + sol1 + " - time " + time1 + " sec");

    context.getLogger().log("--------------------------");
    return sol1;
  }

  private Object[] getSolution(int N, int[] NA, String S, String SA[]) {
    // String Array
    String[] arrayS = S.chars().mapToObj(s -> (char) s + "").toArray(String[]::new);
    System.out.println("String Array: " + Arrays.toString(arrayS));

    // Create Map<s, count> and convert to Object[]
    Map<String, Long> createCountMap = Stream.of(arrayS).collect(Collectors.groupingBy(s -> s, Collectors.counting()));
    Object[] createCountArray = createCountMap.entrySet().stream().toArray();
    System.out.println("Group Character: " + Arrays.deepToString(createCountArray));

    // Create Map<s, length> and replace
    Map<String, Integer> groupStringMap = Stream.of(SA).distinct().collect(Collectors.toMap(s -> s, s -> s.length()));
    groupStringMap.replaceAll((s, length) -> s.startsWith("A") ? length + 10000 : length);
    System.out
        .println("Create Map and replace: " + Arrays.asList(groupStringMap.containsValue(0) ? "" : groupStringMap));

    // Create List<String> and filter
    List<String> listSA = Arrays.stream(SA).filter(name -> name.startsWith("A")).collect(Collectors.toList());
    listSA.forEach(s -> System.out.println("Create List and Filter: " + s));

    // Create List<String> and replace
    List<String> upperCaseListSA = Arrays.stream(SA).collect(Collectors.toList());
    upperCaseListSA.replaceAll(s -> s.toUpperCase());
    System.out.println("Create List and replace: " + upperCaseListSA);

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

    return new Object[] { N * NA.length };
  }

}
