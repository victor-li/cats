package com.endava.cats.fuzzer.headers.base;

import com.endava.cats.io.ServiceCaller;
import com.endava.cats.model.FuzzingStrategy;
import com.endava.cats.report.TestCaseListener;
import com.endava.cats.util.CatsUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Base class for fuzzers sending Control Chars or Unicode Separators in headers.
 */
public abstract class InvisibleCharsBaseFuzzer extends ExpectOnly4XXBaseHeadersFuzzer {
    protected final CatsUtil catsUtil;

    protected InvisibleCharsBaseFuzzer(CatsUtil cu, ServiceCaller sc, TestCaseListener lr) {
        super(sc, lr);
        this.catsUtil = cu;
    }

    @Override
    public List<FuzzingStrategy> fuzzStrategy() {
        return this.getInvisibleChars()
                .stream().map(value -> concreteFuzzStrategy().withData(value))
                .collect(Collectors.toList());
    }

    @Override
    public String description() {
        return "iterate through each header and " + typeOfDataSentToTheService();
    }

    public abstract List<String> getInvisibleChars();

    public abstract FuzzingStrategy concreteFuzzStrategy();
}