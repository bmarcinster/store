package com.talent.alpha.store.searcher;

import com.talent.alpha.store.repostitory.EvaluationResult;
import com.talent.alpha.store.repostitory.SlideResultsDynamicRetriever;
import com.talent.alpha.store.repostitory.SlidesEvaluator;
import com.talent.alpha.store.searcher.context.SearchContext;
import com.talent.alpha.store.searcher.context.SearchContext.SlideContext;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
public class TaskProcessor {

    private final SlideResultsDynamicRetriever resultsDynamicRetriever;
    private final SlidesEvaluator slidesEvaluator;

    public void process(SearchContext searchContext, ResultHolder resultHolder) {

        while (searchContext.hasNext()) {
            searchContext = searchContext.next();
            SlideContext slideContext = searchContext.getSlideContext();

            while (slideContext.hasNext()) {
                String slide = slideContext.next();

                EvaluationResult evaluationResult = slidesEvaluator.evaluate(slide);

                if (evaluationResult == EvaluationResult.ADDED) {
                    resultsDynamicRetriever.retrieve(slide, resultHolder);
                }
                if (Arrays.asList(EvaluationResult.ADDED, EvaluationResult.CONSUMED).contains(evaluationResult)) {
                    searchContext.split().forEach(context -> process(context, resultHolder));

                    return;
                }
            }
        }
    }
}
