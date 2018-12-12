package com.example.milken.githubsearchapp.utils

import io.reactivex.schedulers.TestScheduler
import io.reactivex.subjects.PublishSubject
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit

class RxUtilTest {

    private val testScheduler = TestScheduler()

    private val rxUtil = RxUtil(testScheduler)

    private val subject = PublishSubject.create<CharSequence>()

    private val debounceTime = RxUtil.DEBOUNCE_TIME
    private val lessThanDebounceTime = (0.5 * RxUtil.DEBOUNCE_TIME).toLong()

    private val emptyValue = ""
    private val firstValue = "a"
    private val secondValue = "abc"

    private val testObserver = rxUtil.searchObservableFrom(subject).test().apply {
        subject.onNext(emptyValue)
        testScheduler.advanceTimeBy(debounceTime, TimeUnit.MILLISECONDS)
    }

    @Test
    fun searchObservableFrom_singleValue(){
        subject.onNext(firstValue)
        testScheduler.advanceTimeBy(debounceTime, TimeUnit.MILLISECONDS)

        testObserver.assertValues(firstValue)
    }

    @Test
    fun searchObservableFrom_twoValuesEmitsLast() {
        subject.onNext(firstValue)
        testScheduler.advanceTimeBy(lessThanDebounceTime, TimeUnit.MILLISECONDS)

        subject.onNext(secondValue)
        testScheduler.advanceTimeBy(debounceTime, TimeUnit.MILLISECONDS)

        testObserver.assertValues(secondValue)
    }

    @Test
    fun searchObservableFrom_twoValuesEmitsBoth() {
        subject.onNext(firstValue)
        testScheduler.advanceTimeBy(debounceTime, TimeUnit.MILLISECONDS)

        subject.onNext(secondValue)
        testScheduler.advanceTimeBy(debounceTime, TimeUnit.MILLISECONDS)

        testObserver.assertValues(firstValue, secondValue)
    }

    @Test
    fun searchObservableFrom_emptyValueEmitsNothing() {
        subject.onNext(emptyValue)
        testScheduler.advanceTimeBy(debounceTime, TimeUnit.MILLISECONDS)

        testObserver.assertValues()
    }

    @Test
    fun searchObservableFrom_assertRepeatEmitsOnce() {
        subject.onNext(secondValue)
        testScheduler.advanceTimeBy(debounceTime, TimeUnit.MILLISECONDS)

        subject.onNext(firstValue)
        testScheduler.advanceTimeBy(lessThanDebounceTime, TimeUnit.MILLISECONDS)

        subject.onNext(secondValue)
        testScheduler.advanceTimeBy(debounceTime, TimeUnit.MILLISECONDS)

        testObserver.assertValues(secondValue)
    }
}