package com.talent.alpha.store.searcher;

import com.google.common.collect.Maps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ResultHolderTest {

    private static final int RETRIEVED_VALUE = 2;
    private static final String SLIDE_1 = "slide1";
    private static final String SLIDE_2 = "slide2";

    @Mock
    private CompletableFuture<Map.Entry<String, Integer>> completableFutureMock1;

    @Mock
    private CompletableFuture<Map.Entry<String, Integer>> completableFutureMock2;

    @Test
    public void testResultHolder() {
        //given
        ResultHolder resultHolder = new ResultHolder();
        when(completableFutureMock1.join()).thenReturn(Maps.immutableEntry(SLIDE_1, RETRIEVED_VALUE));
        when(completableFutureMock2.join()).thenReturn(Maps.immutableEntry(SLIDE_2, RETRIEVED_VALUE));

        //when
        resultHolder.add(completableFutureMock1);
        resultHolder.add(completableFutureMock2);

        //then
        assertThat(resultHolder.get())
                .containsEntry(SLIDE_1, RETRIEVED_VALUE)
                .containsEntry(SLIDE_2, RETRIEVED_VALUE)
                .hasSize(2);
    }
}
