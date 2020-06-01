package com.talent.alpha.store.searcher.context;

import com.google.common.collect.ImmutableList;
import com.talent.alpha.store.searcher.context.SearchContext.SlideContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class SearchContextTest {

    private static final ImmutableList<String> WORDS = ImmutableList.of("a", "b", "c", "d", "e", "f");

    @Test
    public void testContextForEmptyList() {
        //given
        SearchContext searchContext = SearchContext.of(emptyList());

        //when
        assertFalse(searchContext.hasNext());
    }

    @Test
    public void testContextForFirstElement() {
        //given
        SearchContext searchContext = SearchContext.of(WORDS);

        //when
        int searchedSlideLength = searchContext.next().getSearchedSlideLength();

        //then
        assertTrue(searchContext.hasNext());
        assertEquals(6, searchedSlideLength);
    }

    @Test
    public void testContextSplitterOnZeroPosition() {
        //given
        SearchContext searchContext = SearchContext.of(WORDS);

        //when
        // searching for slides 6 words
        searchContext = searchContext.next();
        // searching for slides 5 words
        searchContext = searchContext.next();
        // 5 word slide found from position 0
        searchContext.getSlideContext().next();
        // slide found - splitting to new contexts
        List<SearchContext> splittedContext = searchContext.split().stream()
                // move cursor to first item
                .map(SearchContext::next)
                .collect(Collectors.toList());


        //then
        assertThat(splittedContext)
                .contains(SearchContext.of(emptyList(), 1))
                .contains(SearchContext.of(ImmutableList.of("f"), 1))
                .hasSize(2);
    }

    @Test
    public void testContextSplitter() {
        //given
        SearchContext searchContext = SearchContext.of(WORDS);

        //when
        // searching for slides 6 words
        searchContext = searchContext.next();
        // searching for slides 5 words
        searchContext = searchContext.next();
        // searching for slides 4 words
        searchContext = searchContext.next();
        // searching for slides 3 words
        searchContext = searchContext.next();
        // 3 word slide found from position 0
        SlideContext slideContext = searchContext.getSlideContext();
        // checking 3 word slide on position 1
        slideContext.next();
        // checking 3 word slide on position 2
        slideContext.next();
        // checking 3 word slide on position 3
        slideContext.next();
        // slide found - splitting to new contexts
        List<SearchContext> splittedContext = searchContext.split()
                .stream()
                // move cursor to first item
                .map(SearchContext::next)
                .collect(Collectors.toList());

        //then
        assertThat(splittedContext)
                .contains(SearchContext.of(ImmutableList.of("a", "b"), 2))
                .contains(SearchContext.of(ImmutableList.of("f"), 2))
                .hasSize(2);
    }

    @Test
    public void testContextSplitter_slideContextIterator() {
        //given
        SearchContext searchContext = SearchContext.of(WORDS);

        //when-then
        for (int i = 6; i > 0; i--) {
            searchContext = searchContext.next();
            assertEquals(i, searchContext.getSearchedSlideLength());
        }

        SlideContext slideContext = searchContext.getSlideContext();
        for (int i = 0; i < 6; i++) {
            assertEquals(WORDS.get(i), slideContext.next());
        }

        assertFalse(slideContext.hasNext());
        assertFalse(searchContext.hasNext());
    }

    @Test
    public void testContextSplitter_searchContextIterator() {
        //given
        SearchContext searchContext = SearchContext.of(ImmutableList.of("a", "b", "c", "d", "e", "f"));

        //when-then
        for (int i = 6; i > 0; i--) {
            searchContext = searchContext.next();
            assertEquals(i, searchContext.getSearchedSlideLength());
        }

        assertFalse(searchContext.hasNext());
    }
}