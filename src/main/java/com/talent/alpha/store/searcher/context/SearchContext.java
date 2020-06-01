package com.talent.alpha.store.searcher.context;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

import java.util.Iterator;
import java.util.List;


@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchContext implements Iterator<SearchContext> {

    @Getter
    @Delegate(excludes = Iterator.class)
    private final SlideContext slideContext;

    public static SearchContext of(List<String> words) {
        // need to be increased because of SearchContext#next for first element
        // searchedSlideLength will be decreased in first loop
        return of(words, words.size() + 1);
    }

    static SearchContext of(List<String> words, int searchLength) {
        Preconditions.checkState(searchLength >= 1, "Can not search for slide shorter than 1 word");
        return new SearchContext(new SlideContext(words, searchLength));
    }

    public List<SearchContext> split() {
        List<String> leftSubList = getLeftSubList();
        List<String> rightSubList = getRightSubList();

        int maxWordsLength = Math.max(leftSubList.size(), rightSubList.size());
        int searchSlideLength = Math.min(maxWordsLength, slideContext.getSearchedSlideLength());

        return ImmutableList.<SearchContext>builder()
                .add(createSearchContext(leftSubList, searchSlideLength))
                .add(createSearchContext(rightSubList, searchSlideLength))
                .build();
    }

    private SearchContext createSearchContext(List<String> subList, int searchedSlideLength) {
        // need to be increased because of SearchContext#next for first element
        // searchedSlideLength will be decreased in first loop
        return SearchContext.of(subList, searchedSlideLength + 1);
    }

    private List<String> getRightSubList() {
        return slideContext.getWords().subList(slideContext.getCurrentPosition() + slideContext.getSearchedSlideLength(), slideContext.getWords().size());
    }

    private List<String> getLeftSubList() {
        return slideContext.getWords().subList(0, slideContext.getCurrentPosition());
    }

    @Override
    public boolean hasNext() {
        return slideContext.getSearchedSlideLength() - 1 > 0;
    }

    @Override
    public SearchContext next() {
        return SearchContext.of(slideContext.getWords(), slideContext.getSearchedSlideLength() - 1);
    }

    @Getter
    @EqualsAndHashCode
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class SlideContext implements Iterator<String> {
        private final List<String> words;
        private final int searchedSlideLength;

        //cursor will be moved to 0 when SlideContext#next will be invoked
        private int currentPosition = -1;

        @Override
        public boolean hasNext() {
            return currentPosition + searchedSlideLength < words.size();
        }

        @Override
        public String next() {
            currentPosition++;
            return String.join("", words.subList(currentPosition, searchedSlideLength + currentPosition)).trim();
        }
    }
}
