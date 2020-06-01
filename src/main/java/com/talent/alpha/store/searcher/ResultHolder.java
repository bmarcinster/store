package com.talent.alpha.store.searcher;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class ResultHolder {
    private final List<CompletableFuture<Map.Entry<String, Integer>>> futureTasks = Lists.newArrayList();

    public void add(CompletableFuture<Map.Entry<String, Integer>> supplyAsync) {
        futureTasks.add(supplyAsync);
    }

    public Map<String, Integer> get() {
        return futureTasks.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
