package com.talent.alpha.store.controllers;

import com.talent.alpha.store.repostitory.SlideResultsDynamicRetriever;
import com.talent.alpha.store.repostitory.SlidesEvaluator;
import com.talent.alpha.store.searcher.ResultHolder;
import com.talent.alpha.store.searcher.TaskProcessor;
import com.talent.alpha.store.searcher.context.SearchContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.talent.alpha.store.repostitory.SentenceSplitter.getWords;

@RestController
public class SlidesController {
    @Autowired
    private SlideResultsDynamicRetriever slideResultsDynamicRetriever;

    @GetMapping(value = "/slides", consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<Map<String, Integer>> slides(@RequestBody String sentence) {
        ResultHolder resultHolder = new ResultHolder();

        TaskProcessor taskProcessor = new TaskProcessor(slideResultsDynamicRetriever, new SlidesEvaluator());

        taskProcessor.process(SearchContext.of(getWords(sentence)), resultHolder);

        return ResponseEntity.ok().body(resultHolder.get());
    }
}
