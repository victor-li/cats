package com.endava.cats.fuzzer.fields.within;

import com.endava.cats.args.FilesArguments;
import com.endava.cats.fuzzer.http.ResponseCodeFamily;
import com.endava.cats.generator.simple.PayloadGenerator;
import com.endava.cats.io.ServiceCaller;
import com.endava.cats.model.FuzzingData;
import com.endava.cats.model.FuzzingStrategy;
import com.endava.cats.report.TestCaseListener;
import com.endava.cats.util.CatsUtil;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

class WithinMultiCodePointEmojisInFieldsValidateTrimFuzzerTest {
    private final CatsUtil catsUtil = new CatsUtil(null);
    @Mock
    private ServiceCaller serviceCaller;
    @Mock
    private TestCaseListener testCaseListener;
    @Mock
    private FilesArguments filesArguments;

    private WithinMultiCodePointEmojisInFieldsValidateTrimFuzzer withinMultiCodePointEmojisInFieldsValidateTrimFuzzer;

    @BeforeEach
    void setup() {
        withinMultiCodePointEmojisInFieldsValidateTrimFuzzer = new WithinMultiCodePointEmojisInFieldsValidateTrimFuzzer(serviceCaller, testCaseListener, catsUtil, filesArguments);
    }

    @Test
    void shouldProperlyOverrideSuperClassMethods() {
        FuzzingData data = Mockito.mock(FuzzingData.class);
        Map<String, Schema> reqTypes = new HashMap<>();
        reqTypes.put("field", new StringSchema());
        Mockito.when(data.getRequestPropertyTypes()).thenReturn(reqTypes);
        FuzzingStrategy fuzzingStrategy = withinMultiCodePointEmojisInFieldsValidateTrimFuzzer.getFieldFuzzingStrategy(data, "field").get(1);

        Assertions.assertThat(fuzzingStrategy.getData()).contains("\uD83D\uDC68\u200D\uD83C\uDFED️");
        Assertions.assertThat(withinMultiCodePointEmojisInFieldsValidateTrimFuzzer.getExpectedHttpCodeWhenFuzzedValueNotMatchesPattern()).isEqualTo(ResponseCodeFamily.FOURXX);
        Assertions.assertThat(withinMultiCodePointEmojisInFieldsValidateTrimFuzzer.getExpectedHttpCodeWhenOptionalFieldsAreFuzzed()).isEqualTo(ResponseCodeFamily.FOURXX);
        Assertions.assertThat(withinMultiCodePointEmojisInFieldsValidateTrimFuzzer.getExpectedHttpCodeWhenRequiredFieldsAreFuzzed()).isEqualTo(ResponseCodeFamily.FOURXX);

        Assertions.assertThat(withinMultiCodePointEmojisInFieldsValidateTrimFuzzer.description()).isNotNull();
        Assertions.assertThat(withinMultiCodePointEmojisInFieldsValidateTrimFuzzer.typeOfDataSentToTheService()).isNotNull();
    }

    @Test
    void shouldNotFuzzIfDiscriminatorField() {
        PayloadGenerator.GlobalData.getDiscriminators().add("pet#type");
        Assertions.assertThat(withinMultiCodePointEmojisInFieldsValidateTrimFuzzer.isFuzzingPossibleSpecificToFuzzer(null, "pet#type", null)).isFalse();
    }

    @Test
    void shouldFuzzIfNotDiscriminatorField() {
        PayloadGenerator.GlobalData.getDiscriminators().add("pet#type");
        Assertions.assertThat(withinMultiCodePointEmojisInFieldsValidateTrimFuzzer.isFuzzingPossibleSpecificToFuzzer(null, "pet#number", null)).isTrue();
    }
}
