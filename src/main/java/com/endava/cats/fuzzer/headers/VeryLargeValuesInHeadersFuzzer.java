package com.endava.cats.fuzzer.headers;

import com.endava.cats.args.ProcessingArguments;
import com.endava.cats.fuzzer.HeaderFuzzer;
import com.endava.cats.fuzzer.headers.base.ExpectOnly4XXBaseHeadersFuzzer;
import com.endava.cats.generator.simple.StringGenerator;
import com.endava.cats.io.ServiceCaller;
import com.endava.cats.model.FuzzingStrategy;
import com.endava.cats.report.TestCaseListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;


/**
 *
 */
@Component
@HeaderFuzzer
@ConditionalOnProperty(value = "fuzzer.headers.VeryLargeValuesInHeadersFuzzer.enabled", havingValue = "true")
public class VeryLargeValuesInHeadersFuzzer extends ExpectOnly4XXBaseHeadersFuzzer {
    private final ProcessingArguments processingArguments;

    @Autowired
    public VeryLargeValuesInHeadersFuzzer(ServiceCaller sc, TestCaseListener lr, ProcessingArguments pa) {
        super(sc, lr);
        this.processingArguments = pa;
    }

    @Override
    public String typeOfDataSentToTheService() {
        return "large values";
    }

    @Override
    protected List<FuzzingStrategy> fuzzStrategy() {
        return Collections.singletonList(
                FuzzingStrategy.replace().withData(
                        StringGenerator.generateLargeString(processingArguments.getLargeStringsSize() / 4)));
    }

    @Override
    public String description() {
        return "iterate through each header and send requests with large values in the targeted header";
    }
}
