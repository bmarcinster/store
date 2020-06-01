package com.talent.alpha.store.repostitory;

import com.google.common.collect.ImmutableList;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class SentenceSplitterTest {

    @Test
    public void testSplitter() {
        //given
        String sentece = " Ala has    big cat ";

        //when
        List<String> words = SentenceSplitter.getWords(sentece);

        //then
        assertEquals(ImmutableList.of("Ala", " has", " big", " cat"), words);
    }

    @Test
    public void testEmptySentenceSplitter() {
        //given
        String sentece = "  ";

        //when
        List<String> words = SentenceSplitter.getWords(sentece);

        //then
        Assertions.assertThat(words).isEmpty();
    }
}