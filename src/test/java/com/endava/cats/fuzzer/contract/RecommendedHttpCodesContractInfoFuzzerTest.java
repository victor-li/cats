package com.endava.cats.fuzzer.contract;

import com.endava.cats.args.FilterArguments;
import com.endava.cats.http.HttpMethod;
import com.endava.cats.io.TestCaseExporter;
import com.endava.cats.model.FuzzingData;
import com.endava.cats.report.ExecutionStatisticsListener;
import com.endava.cats.report.TestCaseListener;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;

@ExtendWith(SpringExtension.class)
class RecommendedHttpCodesContractInfoFuzzerTest {

    @SpyBean
    private TestCaseListener testCaseListener;

    @MockBean
    private ExecutionStatisticsListener executionStatisticsListener;

    @MockBean
    private FilterArguments filterArguments;

    @MockBean
    private TestCaseExporter testCaseExporter;

    @SpyBean
    private BuildProperties buildProperties;

    private RecommendedHttpCodesContractInfoFuzzer recommendedHttpCodesContractInfoFuzzer;

    @BeforeAll
    static void init() {
        System.setProperty("name", "cats");
        System.setProperty("version", "4.3.2");
        System.setProperty("time", "100011111");
    }

    @BeforeEach
    void setup() {
        recommendedHttpCodesContractInfoFuzzer = new RecommendedHttpCodesContractInfoFuzzer(testCaseListener);
    }

    @ParameterizedTest
    @CsvSource(value = {"400,500,200;POST", "400,500,201;POST", "400,500,202;POST", "400,500,204;POST", "400,404,500,201;PUT", "400,404,500,202;GET", "404,200;HEAD",
            "404,202;HEAD", "400,404,500,200;DELETE", "400,404,500,201;DELETE", "400,404,500,202;DELETE", "400,404,500,204;DELETE",
            "400,404,500,200;PATCH", "400,404,500,201;PATCH", "400,404,500,202;PATCH", "400,404,500,204;PATCH", "400,500,200;TRACE"}, delimiter = ';')
    void shouldReportInfoWhenAllResponseCodesAreValid(String responseCode, HttpMethod method) {
        FuzzingData data = ContractFuzzerDataUtil.prepareFuzzingData("PetStore", method, responseCode.split(","));

        recommendedHttpCodesContractInfoFuzzer.fuzz(data);

        Mockito.verify(testCaseListener, Mockito.times(1)).reportInfo(Mockito.any(), Mockito.eq("All recommended HTTP codes are defined!"));
    }

    @ParameterizedTest
    @CsvSource(value = {"400,500;POST;200|201|202|204", "400,500;POST;200|201|202|204", "400,202;POST;500", "500,204;POST;400", "404,500,201;PUT;400", "400,500,202;GET;404", "200;HEAD;404",
            "404;HEAD;200|202", "404,500,200;DELETE;400", "400,500,201;DELETE;404", "400,404,202;DELETE;500", "400,404,500;DELETE;200|201|202|204",
            "404,500,200;PATCH;400", "400,500,201;PATCH;404", "400,404,202;PATCH;500", "400,404,500;PATCH;200|201|202|204", "500,200;TRACE;400", "400,200;TRACE;500"}, delimiter = ';')
    void shouldReportErrorWhenAllResponseCodesAreValid(String responseCode, HttpMethod method, String missing) {
        FuzzingData data = ContractFuzzerDataUtil.prepareFuzzingData("PetStore", method, responseCode.split(","));

        recommendedHttpCodesContractInfoFuzzer.fuzz(data);

        Mockito.verify(testCaseListener, Mockito.times(1)).reportError(Mockito.any(), Mockito.eq("The following recommended HTTP response codes are missing: {}"), Mockito.eq(Collections.singletonList(missing)));
    }


    @Test
    void shouldReturnSimpleClassNameForToString() {
        Assertions.assertThat(recommendedHttpCodesContractInfoFuzzer).hasToString(recommendedHttpCodesContractInfoFuzzer.getClass().getSimpleName());
    }

    @Test
    void shouldReturnMeaningfulDescription() {
        Assertions.assertThat(recommendedHttpCodesContractInfoFuzzer.description()).isEqualTo("verifies that the current path contains all recommended HTTP response codes for all operations");
    }
}
