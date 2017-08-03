package at.medunigraz.imi.bst.trec.model;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Metrics {
	
	private Map<String, Double> metrics = new TreeMap<>();

	public void put(String name, double value) {
		metrics.put(name, value);
	}
	
	public double getMetric(String name) {
		return metrics.getOrDefault(name, 0d);
	}
	
	public boolean hasMetric(String name) {
		return metrics.containsKey(name);
	}
	
	public String getMetricsAsString() {
		StringBuilder sb = new StringBuilder();
		
		Set<Map.Entry<String, Double>> entries = metrics.entrySet();
		for (Map.Entry<String, Double> entry : entries) {
			sb.append(entry.getKey());
			sb.append("=");
			sb.append(entry.getValue());
			sb.append("\n");
		}
		
		return sb.toString();
	}
	
	public double getNDCG() {
		return getMetric("ndcg");
	}

	public double getRPrec() {
		return getMetric("Rprec");
	}

	public double getInfAP() {
		return getMetric("infAP");
	}

	public double getP10() {
		return getMetric("P_10");
	}

	public double getF() {
		return getMetric("set_F");
	}
}
