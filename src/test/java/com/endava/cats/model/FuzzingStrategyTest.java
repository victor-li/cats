package com.endava.cats.model;

import com.endava.cats.model.strategy.NoopFuzzingStrategy;
import com.endava.cats.model.strategy.PrefixFuzzingStrategy;
import com.endava.cats.model.strategy.ReplaceFuzzingStrategy;
import com.endava.cats.model.strategy.SkipFuzzingStrategy;
import com.endava.cats.model.strategy.TrailFuzzingStrategy;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FuzzingStrategyTest {

    @Test
    void givenTheFuzzingStrategyClass_whenCallingTheStaticCreateMethods_thenProperInstancesAreReturned() {
        Assertions.assertThat(FuzzingStrategy.trail()).isInstanceOf(TrailFuzzingStrategy.class);
        Assertions.assertThat(FuzzingStrategy.replace()).isInstanceOf(ReplaceFuzzingStrategy.class);
        Assertions.assertThat(FuzzingStrategy.prefix()).isInstanceOf(PrefixFuzzingStrategy.class);
        Assertions.assertThat(FuzzingStrategy.noop()).isInstanceOf(NoopFuzzingStrategy.class);
        Assertions.assertThat(FuzzingStrategy.skip()).isInstanceOf(SkipFuzzingStrategy.class);
    }

    @ParameterizedTest
    @CsvSource({"' '", "'\t'"})
    void givenANullString_whenMergingTheFuzzingWithAnotherString_thenTheReplaceFuzzingStrategyIsApplied(String space) {
        String result = FuzzingStrategy.mergeFuzzing(String.valueOf(space), "air");

        Assertions.assertThat(result).isEqualTo(String.valueOf(space));
    }

    @ParameterizedTest
    @CsvSource({"' '", "'\t'"})
    void givenASpacedPrefixedString_whenMergingTheFuzzingWithAnotherString_thenThePrefixFuzzingStrategyIsApplied(String space) {
        String result = FuzzingStrategy.mergeFuzzing(space + space + "test", "air");

        Assertions.assertThat(result).isEqualTo(space + space + "air");
    }

    @ParameterizedTest
    @CsvSource({"' '", "'\t'"})
    void givenASpaceTrailingString_whenMergingTheFuzzingWithAnotherString_thenTheTrailFuzzingStrategyIsApplied(String space) {
        String result = FuzzingStrategy.mergeFuzzing("test" + space + space, "air");

        Assertions.assertThat(result).isEqualTo("air" + space + space);
    }

    @ParameterizedTest
    @CsvSource({"' '", "'\t'"})
    void givenAnEmptyString_whenMergingTheFuzzingWithAnotherString_thenTheReplaceFuzzingStrategyIsApplied(String space) {
        String result = FuzzingStrategy.mergeFuzzing("replaced", "air");

        Assertions.assertThat(result).isEqualTo("replaced");
    }

    @Test
    void givenAStringWithNoSpaces_whenMergingTheFuzzingWithAnotherString_thenTheSuppliedValueIsUnchanged() {
        String result = FuzzingStrategy.mergeFuzzing("test", "air");

        Assertions.assertThat(result).isEqualTo("test");
    }

    @Test
    void givenAFuzzingStrategy_whenSettingAnInnerValueAboveTruncationThreshold_thenTheValueIsProperlyTruncated() {
        FuzzingStrategy strategy = FuzzingStrategy.replace().withData(StringUtils.repeat("t", 50));

        Assertions.assertThat(strategy.truncatedValue()).isEqualTo(strategy.name() + " with " + StringUtils.repeat("t", 30) + "...");
        Assertions.assertThat(strategy).hasToString(strategy.name() + " with " + StringUtils.repeat("t", 50));
    }

    @Test
    void givenAFuzzingStrategy_whenSettingAnInnerValueBelowTruncationThreshold_thenTheValueIsNotTruncated() {
        FuzzingStrategy strategy = FuzzingStrategy.replace().withData(StringUtils.repeat("t", 30));

        Assertions.assertThat(strategy).hasToString(strategy.truncatedValue());
    }

    @Test
    void givenAFuzzingStrategy_whenSettingAnInnerValueWithNull_thenToStringMatchesName() {
        FuzzingStrategy strategy = FuzzingStrategy.replace().withData(null);

        Assertions.assertThat(strategy).hasToString(strategy.name());
    }

    @Test
    void shouldHaveProperNameForInsert() {
        FuzzingStrategy strategy = FuzzingStrategy.insert().withData(null);

        Assertions.assertThat(strategy).hasToString(strategy.name());
    }

    @Test
    void shouldTruncateValue() {
        FuzzingStrategy strategy = FuzzingStrategy.replace().withData(StringUtils.repeat("a", 31));

        Assertions.assertThat(strategy.truncatedValue()).contains("REPLACE", "with", StringUtils.repeat("a", 30), "...");
    }

    @Test
    void shouldReturnNameOnlyWhenDataIsNull() {
        FuzzingStrategy strategy = FuzzingStrategy.replace().withData(null);

        Assertions.assertThat(strategy.truncatedValue()).isEqualTo(strategy.name());
    }

    @ParameterizedTest
    @CsvSource({"'\u2001'", "'\u0001'"})
    void shouldMatchUnicodeWhitespaceAndPrefix(char c) {
        String result = FuzzingStrategy.mergeFuzzing(c + "test", "air");
        Assertions.assertThat(result).isEqualTo(c + "air");
    }

    @ParameterizedTest
    @CsvSource({"'\u2001'", "'\u0001'"})
    void shouldMatchUnicodeWhitespaceAndTrail(char c) {
        String result = FuzzingStrategy.mergeFuzzing("test" + c, "air");
        Assertions.assertThat(result).isEqualTo("air" + c);
    }

    @Test
    void shouldInsertInSuppliedValue() {
        String result = FuzzingStrategy.mergeFuzzing("te\uD83E\uDD76st", "air");
        Assertions.assertThat(result).isEqualTo("a\uD83E\uDD76ir");
    }

    @Test
    void shouldReplaceWhenLargeString() {
        String result = FuzzingStrategy.mergeFuzzing("caTTTTTTTTts", "air");
        Assertions.assertThat(result).isEqualTo("caTTTTTTTTts");
    }

}