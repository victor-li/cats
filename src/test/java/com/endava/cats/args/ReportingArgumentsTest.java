package com.endava.cats.args;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class ReportingArgumentsTest {

    @Test
    void shouldHaveHtmlJsReportFormat() {
        ReportingArguments reportingArguments = new ReportingArguments();

        ReflectionTestUtils.setField(reportingArguments, "reportFormat", "htmlJs");

        Assertions.assertThat(reportingArguments.getReportFormat()).isEqualTo(ReportingArguments.ReportFormat.HTML_JS);
    }

    @Test
    void shouldHaveHtmlOnlyReportFormat() {
        ReportingArguments reportingArguments = new ReportingArguments();

        ReflectionTestUtils.setField(reportingArguments, "reportFormat", "htmlOnly");

        Assertions.assertThat(reportingArguments.getReportFormat()).isEqualTo(ReportingArguments.ReportFormat.HTML_ONLY);
    }

    @Test
    void shouldPrintExecutionStatistics() {
        ReportingArguments reportingArguments = new ReportingArguments();

        ReflectionTestUtils.setField(reportingArguments, "printExecutionStatistics", "true");

        Assertions.assertThat(reportingArguments.printExecutionStatistics()).isTrue();
        Assertions.assertThat(reportingArguments.printDetailedExecutionStatistics()).isFalse();
    }

    @Test
    void shouldPrintDetailedExecutionStatistics() {
        ReportingArguments reportingArguments = new ReportingArguments();

        ReflectionTestUtils.setField(reportingArguments, "printExecutionStatistics", "detailed");

        Assertions.assertThat(reportingArguments.printExecutionStatistics()).isTrue();
    }

    @Test
    void shouldNotPrintExecutionStatistics() {
        ReportingArguments reportingArguments = new ReportingArguments();

        ReflectionTestUtils.setField(reportingArguments, "printExecutionStatistics", "empty");

        Assertions.assertThat(reportingArguments.printExecutionStatistics()).isFalse();
    }

    @Test
    void shouldTimestampReports() {
        ReportingArguments reportingArguments = new ReportingArguments();

        ReflectionTestUtils.setField(reportingArguments, "timestampReports", "true");

        Assertions.assertThat(reportingArguments.isTimestampReports()).isTrue();
    }

    @Test
    void shouldNotTimestampReports() {
        ReportingArguments reportingArguments = new ReportingArguments();

        ReflectionTestUtils.setField(reportingArguments, "timestampReports", "empty");

        Assertions.assertThat(reportingArguments.isTimestampReports()).isFalse();
    }
}
