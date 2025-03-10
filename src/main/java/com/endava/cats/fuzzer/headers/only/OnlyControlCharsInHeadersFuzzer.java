package com.endava.cats.fuzzer.headers.only;

import com.endava.cats.fuzzer.ControlCharFuzzer;
import com.endava.cats.fuzzer.HeaderFuzzer;
import com.endava.cats.fuzzer.headers.base.InvisibleCharsBaseFuzzer;
import com.endava.cats.io.ServiceCaller;
import com.endava.cats.model.FuzzingStrategy;
import com.endava.cats.report.TestCaseListener;
import com.endava.cats.util.CatsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@HeaderFuzzer
@ControlCharFuzzer
@ConditionalOnProperty(value = "fuzzer.headers.OnlyControlCharsInHeadersFuzzer.enabled", havingValue = "true")
public class OnlyControlCharsInHeadersFuzzer extends InvisibleCharsBaseFuzzer {

    @Autowired
    public OnlyControlCharsInHeadersFuzzer(CatsUtil cu, ServiceCaller sc, TestCaseListener lr) {
        super(cu, sc, lr);
    }

    @Override
    public String typeOfDataSentToTheService() {
        return "replace value with control chars";
    }

    @Override
    public List<String> getInvisibleChars() {
        return catsUtil.getControlCharsHeaders();
    }

    @Override
    public FuzzingStrategy concreteFuzzStrategy() {
        return FuzzingStrategy.replace();
    }

    @Override
    protected boolean matchResponseSchema() {
        return false;
    }
}
