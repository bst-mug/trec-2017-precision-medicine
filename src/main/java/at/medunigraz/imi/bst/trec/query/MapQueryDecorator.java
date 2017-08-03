package at.medunigraz.imi.bst.trec.query;

import java.util.Map;

public abstract class MapQueryDecorator extends QueryDecorator {

	public MapQueryDecorator(Query decoratedQuery) {
		super(decoratedQuery);
	}

	protected void map(Map<String, String> keymap) {
		String jsonQuery = getJSONQuery();
		
		for (Map.Entry<String, String> entry : keymap.entrySet()) {
			String search = String.format("{{%s}}", entry.getKey());
			jsonQuery = jsonQuery.replace(search, entry.getValue());
		}

		setJSONQuery(jsonQuery);
	}

}
