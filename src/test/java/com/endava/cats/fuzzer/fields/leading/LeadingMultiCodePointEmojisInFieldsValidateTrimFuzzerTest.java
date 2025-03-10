package com.endava.cats.fuzzer.fields.leading;

import com.endava.cats.args.FilesArguments;
import com.endava.cats.fuzzer.http.ResponseCodeFamily;
import com.endava.cats.generator.simple.PayloadGenerator;
import com.endava.cats.io.ServiceCaller;
import com.endava.cats.model.FuzzingStrategy;
import com.endava.cats.report.TestCaseListener;
import com.endava.cats.util.CatsUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class LeadingMultiCodePointEmojisInFieldsValidateTrimFuzzerTest {
    private final CatsUtil catsUtil = new CatsUtil(null);
    @Mock
    private ServiceCaller serviceCaller;
    @Mock
    private TestCaseListener testCaseListener;
    @Mock
    private FilesArguments filesArguments;

    private LeadingMultiCodePointEmojisInFieldsValidateTrimFuzzer leadingMultiCodePointEmojisInFieldsValidateTrimFuzzer;

    @BeforeEach
    void setup() {
        leadingMultiCodePointEmojisInFieldsValidateTrimFuzzer = new LeadingMultiCodePointEmojisInFieldsValidateTrimFuzzer(serviceCaller, testCaseListener, catsUtil, filesArguments);
    }

    @Test
    void shouldProperlyOverrideMethods() {
        FuzzingStrategy fuzzingStrategy = leadingMultiCodePointEmojisInFieldsValidateTrimFuzzer.getFieldFuzzingStrategy(null, null).get(0);
        Assertions.assertThat(fuzzingStrategy.name()).isEqualTo(FuzzingStrategy.prefix().name());

        Assertions.assertThat(fuzzingStrategy.getData()).isEqualTo("\uD83D\uDC69\uD83C\uDFFE");
        Assertions.assertThat(leadingMultiCodePointEmojisInFieldsValidateTrimFuzzer.getExpectedHttpCodeWhenFuzzedValueNotMatchesPattern()).isEqualTo(ResponseCodeFamily.FOURXX);
        Assertions.assertThat(leadingMultiCodePointEmojisInFieldsValidateTrimFuzzer.getExpectedHttpCodeWhenOptionalFieldsAreFuzzed()).isEqualTo(ResponseCodeFamily.FOURXX);
        Assertions.assertThat(leadingMultiCodePointEmojisInFieldsValidateTrimFuzzer.getExpectedHttpCodeWhenRequiredFieldsAreFuzzed()).isEqualTo(ResponseCodeFamily.FOURXX);

        Assertions.assertThat(leadingMultiCodePointEmojisInFieldsValidateTrimFuzzer.description()).isNotNull();
        Assertions.assertThat(leadingMultiCodePointEmojisInFieldsValidateTrimFuzzer.typeOfDataSentToTheService()).isNotNull();
    }


    @Test
    void shouldNotFuzzIfDiscriminatorField() {
        PayloadGenerator.GlobalData.getDiscriminators().add("pet#type");
        Assertions.assertThat(leadingMultiCodePointEmojisInFieldsValidateTrimFuzzer.isFuzzingPossibleSpecificToFuzzer(null, "pet#type", null)).isFalse();
    }

    @Test
    void shouldFuzzIfNotDiscriminatorField() {
        PayloadGenerator.GlobalData.getDiscriminators().add("pet#type");
        Assertions.assertThat(leadingMultiCodePointEmojisInFieldsValidateTrimFuzzer.isFuzzingPossibleSpecificToFuzzer(null, "pet#number", null)).isTrue();
    }
}
