package at.medunigraz.imi.bst.trec.model;

public class Result {
	private String id;

	private float score;

	public Result(String id, float score) {
		this.id = id;
		this.score = score;
	}

	public String getId() {
		return id;
	}

	public float getScore() {
		return score;
	}
}
