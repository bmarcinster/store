package com.talent.alpha.store.repostitory;

import com.talent.alpha.store.searcher.ResultHolder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.CompletableFuture;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SlideResultsDynamicRetrieverTest {

    @Mock
    private ResultHolder resultHolderMock;

    private final SlideResultsDynamicRetriever slideResultsDynamicRetriever = new SlideResultsDynamicRetriever();

    @Test
    public void testRetriever() {
        //given
        String slide = "slide";

        //when
        slideResultsDynamicRetriever.retrieve(slide, resultHolderMock);

        //then
        verify(resultHolderMock).add(any(CompletableFuture.class));
    }

}
