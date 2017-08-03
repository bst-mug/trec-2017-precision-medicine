package at.medunigraz.imi.bst.trec.experiment;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import at.medunigraz.imi.bst.config.TrecConfig;
import at.medunigraz.imi.bst.trec.evaluator.TrecEval;
import at.medunigraz.imi.bst.trec.evaluator.TrecWriter;
import at.medunigraz.imi.bst.trec.model.Result;
import at.medunigraz.imi.bst.trec.model.ResultList;
import at.medunigraz.imi.bst.trec.model.Topic;
import at.medunigraz.imi.bst.trec.model.TopicSet;
import at.medunigraz.imi.bst.trec.query.Query;
import at.medunigraz.imi.bst.trec.stats.CSVStatsWriter;
import at.medunigraz.imi.bst.trec.stats.XMLStatsWriter;

public class Experiment extends Thread {

	private static final Logger LOG = LogManager.getLogger();

	@Deprecated
	private String id = null;
	
	private Query decorator;
	
	private Task task;
	
	private GoldStandard goldStandard;
	
	public static enum Task {
		CLINICAL_TRIALS, PUBMED
	}
	
	public static enum GoldStandard {
		EXAMPLE, EXTRA, FINAL
	}

	@Override
	public void run() {
		final String collection = getExperimentId().substring(0, getExperimentId().indexOf('-'));

		final String name = getExperimentId() + " with decorators " + decorator.getName();

		LOG.info("Running collection " + name + "...");

		File example = new File(CSVStatsWriter.class.getResource("/topics/" + collection + ".xml").getPath());
		TopicSet topicSet = new TopicSet(example);

		File output = new File("results/" + getExperimentId() + ".trec_results");
		TrecWriter tw = new TrecWriter(output);

		// TODO DRY Issue #53
		Set<ResultList> resultListSet = new HashSet<>();
		for (Topic topic : topicSet.getTopics()) {
			List<Result> results = decorator.query(topic);

			ResultList resultList = new ResultList(topic);
			resultList.setResults(results);
			resultListSet.add(resultList);
		}

		tw.write(resultListSet);
		tw.close();

		File goldStandard = new File(CSVStatsWriter.class.getResource("/gold-standard/" + getExperimentId() + ".qrels").getPath());
		TrecEval te = new TrecEval(goldStandard, output);

		XMLStatsWriter xsw = new XMLStatsWriter(new File("stats/" + getExperimentId() + ".xml"));
		xsw.write(te.getMetrics());
		xsw.close();

		CSVStatsWriter csw = new CSVStatsWriter(new File("stats/" + getExperimentId() + ".csv"));
		csw.write(te.getMetrics());
		csw.close();

		LOG.info("Got NDCG: " + te.getNDCG() + " for collection " + name);
		LOG.trace(te.getMetricsByTopic("all"));
		
		// TODO Experiment API #53
		System.out.println(te.getNDCG() + ";" + name);
	}
	
	@Deprecated
	public void setExperimentId(String id) {
		this.id = id;
	}

	public void setDecorator(Query decorator) {
		this.decorator = decorator;
	}

	public String getExperimentId() {
		if (id != null) {
			return id;
		}
		
		return getCollectionName() + "-" + getShortTaskName();
		
	}
	
	public void setTask(Task task) {
		this.task = task;
	}

	public void setGoldStandard(GoldStandard goldStandard) {
		this.goldStandard = goldStandard;
	}

	public String getCollectionName() {
		switch (goldStandard) {
		case EXAMPLE:
			return "example";
		case EXTRA:
			return "extra";
		case FINAL:
			return "topics2017";
		default:
			return "";
		}
	}
	
	public String getTaskName() {
		switch (task) {
		case CLINICAL_TRIALS:
			return "clinicaltrials";
		case PUBMED:
			return "trec";
		default:
			return "";
		}
	}
	
	public String[] getTypes() {
		String[] ret = new String[0];	// Everything
		
		if (task == Task.CLINICAL_TRIALS) {
			return new String[] { TrecConfig.TRIALS_TYPE };
		}
		
		if (task == Task.PUBMED && goldStandard == GoldStandard.FINAL) {
			return new String[] { TrecConfig.EXTRA_TYPE, TrecConfig.MEDLINE_TYPE };
		}
		
		return ret;
	}
	
	public String getShortTaskName() {
		switch (task) {
		case CLINICAL_TRIALS:
			return "ct";
		case PUBMED:
			return "pmid";
		default:
			return "";
		}
	}

	public Query getDecorator() {
		return decorator;
	}
	
	
}
