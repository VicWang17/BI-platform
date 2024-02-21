package com.yupi.springbootinit.model.vo;

/**
 *  bi返回结果
 */
public class BiResponse {

    private String genChart;
    private String genResult;

    public Long getChartId() {
        return chartId;
    }

    public void setChartId(Long chartId) {
        this.chartId = chartId;
    }

    private Long chartId;

    public String getGenChart() {
        return genChart;
    }

    public void setGenChart(String genChart) {
        this.genChart = genChart;
    }

    public String getGenResult() {
        return genResult;
    }

    public void setGenResult(String genResult) {
        this.genResult = genResult;
    }
}
