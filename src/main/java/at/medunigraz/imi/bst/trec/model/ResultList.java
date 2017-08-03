package at.medunigraz.imi.bst.trec.model;

import java.util.ArrayList;
import java.util.List;

public class ResultList {
	private Topic topic;
	
	private List<Result> results = new ArrayList<>();
	
	public ResultList(Topic topic) {
		this.topic = topic;
	}
	
	public boolean add(Result result) {
		return results.add(result);
	}

	/**
	 * 
	 * @deprecated Use add(Result result) instead
	 * @param results
	 */
	public void setResults(List<Result> results) {
		this.results = results;
	}

	public Topic getTopic() {
		return topic;
	}
	
	public List<Result> getResults() {
		return results;
	}
}
