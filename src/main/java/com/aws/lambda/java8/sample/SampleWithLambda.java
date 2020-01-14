package com.aws.lambda.java8.sample;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class SampleWithLambda implements RequestHandler<Object, String> {

  @Override
  public String handleRequest(Object input, Context context) {
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
    String sol1 = getSolution1.get();
    long end1 = System.nanoTime();
    Double time1 = Math.round(((end1 - (double) start1) / 1000000000) * 100000d) / 100000d;
    context.getLogger().log("Solution 1: " + sol1 + " - time " + time1 + " sec");

    long start2 = System.nanoTime();
    String sol2 = getSolution2.apply("");
    long end2 = System.nanoTime();
    Double time2 = Math.round(((end2 - (double) start2) / 1000000000) * 100000d) / 100000d;
    context.getLogger().log("Solution 2: " + sol2 + " - time " + time2 + " sec");

    long start3 = System.nanoTime();
    String sol3 = getSolution3.apply("", "");
    long end3 = System.nanoTime();
    Double time3 = Math.round(((end3 - (double) start3) / 1000000000) * 100000d) / 100000d;
    context.getLogger().log("Solution 3: " + sol3 + " - time " + time3 + " sec");

    long start4 = System.nanoTime();
    String sol4 = getSolution4.apply("", "", "");
    long end4 = System.nanoTime();
    Double time4 = Math.round(((end4 - (double) start4) / 1000000000) * 100000d) / 100000d;
    context.getLogger().log("Solution 4: " + sol4 + " - time " + time4 + " sec");

    context.getLogger().log("--------------------------");
    return "";
  }

  Supplier<String> getSolution1 = () -> {
    return "";
  };

  Function<String, String> getSolution2 = a -> {
    return a;
  };

  BiFunction<String, String, String> getSolution3 = (n, a) -> {
    return n + a;
  };

  @FunctionalInterface
  interface FunctionSolution<A, B, C, O> {
    public O apply(A a, B b, C c);
  }

  FunctionSolution<String, String, String, String> getSolution4 = (a, b, c) -> {
    return a + b + c;
  };

}
