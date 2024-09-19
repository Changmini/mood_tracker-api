package kr.co.moodtracker.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChartVO {
	private Map<String, Object> chartData;
	private List<Object> datasets;
	private Map<String, Object> dataset;
	private int[] data = null;
	
	public ChartVO() {}
	/**
	 * <pre>
	 *   labels는 차트 축(x or y)에 들어갈 라벨들을 가리킨다.
	 *   (타입은 Array or List, 둘 다 가능하다)
	 * </pre>
	 * @param labels
	 */
	public ChartVO(Object labels) {
		this.datasets = new ArrayList<>();
		this.chartData = new HashMap<>();
		this.chartData.put("labels",labels);
		this.chartData.put("datasets",this.datasets);
	}
	
	/**
	 * <pre>
	 *   label은 legend(범례)를 의미하고
	 *   data는 실질적으로 차트에 표기되는 데이터 값이다.
	 * </pre>
	 * @param label
	 * @param dataSize
	 */
	public void setElement(String label, int dataSize) {
		this.data = new int[dataSize];
		this.dataset = new HashMap<>();
		this.dataset.put("label", label);
		this.dataset.put("data", data);
		this.datasets.add(dataset);
	}
	
	public Map<String, Object> getChartData() {
		return chartData;
	}
	public List<Object> getDatasets() {
		return datasets;
	}
	public Map<String, Object> getDataset() {
		return dataset;
	}
	public int[] getData() {
		return data;
	}
}
