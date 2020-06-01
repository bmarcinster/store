package com.talent.alpha.store.repostitory;

import com.google.common.collect.Maps;
import com.talent.alpha.store.searcher.ResultHolder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomUtils;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class SlideResultsDynamicRetriever {

    public void retrieve(String slide, ResultHolder resultHolder) {
        resultHolder.add(CompletableFuture.supplyAsync(() -> readValue(slide)));
    }

    // heavy operation simulator
    @SneakyThrows
    private Map.Entry<String, Integer> readValue(String slide) {
        int seconds = RandomUtils.nextInt(1, 10);

        Thread.sleep(Duration.ofSeconds(seconds).toMillis());

        return Maps.immutableEntry(slide, seconds);
    }
}