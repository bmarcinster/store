package com.talent.alpha.store.repostitory;

import org.apache.logging.log4j.util.Strings;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SentenceSplitter {
    public static List<String> getWords(String sentence) {
        return Arrays.stream(sentence.trim().split("(?= )"))
                .filter(Strings::isNotBlank)
                .collect(Collectors.toList());
    }
}
