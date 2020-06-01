package com.talent.alpha.store.repostitory;

import com.google.common.collect.Sets;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomUtils;

import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SlidesEvaluator {

    private final ReadWriteLock readWriteLock;
    private volatile Set<String> slides = Sets.newConcurrentHashSet();

    public SlidesEvaluator() {
        this(new ReentrantReadWriteLock());
    }

    public EvaluationResult evaluate(String slide) {
        if (wrapWithLocking(readWriteLock::readLock, () -> containsSlide(slide))) {
            if (wrapWithLocking(readWriteLock::writeLock, () -> slides.add(slide))) {
                return EvaluationResult.ADDED;
            }
            return EvaluationResult.CONSUMED;
        }
        return EvaluationResult.NOT_EXISTS;
    }

    private boolean containsSlide(String slide) {
        return RandomUtils.nextBoolean() || slides.contains(slide);
    }

    private boolean wrapWithLocking(Supplier<Lock> lock, Supplier<Boolean> object) {
        lock.get().lock();
        try {
            return object.get();
        } finally {
            lock.get().unlock();
        }
    }
}