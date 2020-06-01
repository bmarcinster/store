package com.talent.alpha.store.searcher;

import com.talent.alpha.store.repostitory.EvaluationResult;
import com.talent.alpha.store.repostitory.SlideResultsDynamicRetriever;
import com.talent.alpha.store.repostitory.SlidesEvaluator;
import com.talent.alpha.store.searcher.context.SearchContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TaskProcessorTest {

    @Mock
    private SlideResultsDynamicRetriever slideResultsDynamicRetrieverMock;
    @Mock
    private SlidesEvaluator slidesEvaluatorMock;
    @Mock
    private ResultHolder resultHolderMock;

    @InjectMocks
    private TaskProcessor taskProcessor;

    @Test
    public void testTaskProcessor_notExists() {
        //given
        String sentence = "Ala ma";
        when(slidesEvaluatorMock.evaluate(anyString())).thenReturn(EvaluationResult.NOT_EXISTS);

        //when
        taskProcessor.process(SearchContext.of(Arrays.asList(sentence.split("(?= )"))), resultHolderMock);

        //then
        verify(slidesEvaluatorMock).evaluate(sentence);
        verify(slidesEvaluatorMock).evaluate("Ala");
        verify(slidesEvaluatorMock).evaluate("ma");
        verifyNoMoreInteractions(resultHolderMock, slidesEvaluatorMock);
    }

    @Test
    public void testTaskProcessor_added() {
        //given
        String sentence = "Mary went Mary's gone";
        List<String> inMemoryResults = Arrays.asList("Mary", "Mary gone", "Mary's gone", "went Mary's", "went");

        inMemoryResults.stream().forEach(result -> when(slidesEvaluatorMock.evaluate(result)).thenReturn(EvaluationResult.ADDED));

        //when
        taskProcessor.process(SearchContext.of(Arrays.asList(sentence.split("(?= )"))), resultHolderMock);

        //then
        verify(slidesEvaluatorMock).evaluate(sentence);
        verify(slideResultsDynamicRetrieverMock).retrieve("went Mary's", resultHolderMock);
        verify(slideResultsDynamicRetrieverMock).retrieve("Mary", resultHolderMock);
        //Mary went Mary's gone
        //Mary went Mary's
        //went Mary's gone
        //Mary went
        //went Mary's
        //Mary
        //gone
        verify(slidesEvaluatorMock, times(7)).evaluate(anyString());
        verifyNoMoreInteractions(resultHolderMock);
    }

    @Test
    public void testTaskProcessor_consumed() {
        //given
        String sentence = "Ala ma";
        when(slidesEvaluatorMock.evaluate(anyString())).thenReturn(EvaluationResult.CONSUMED);

        //when
        taskProcessor.process(SearchContext.of(Arrays.asList(sentence.split("(?= )"))), resultHolderMock);

        //then
        verify(slidesEvaluatorMock).evaluate(sentence);
        verifyNoMoreInteractions(resultHolderMock, slidesEvaluatorMock);
    }
}
