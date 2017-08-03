package at.medunigraz.imi.bst.trec;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import at.medunigraz.imi.bst.trec.experiment.Experiment;
import at.medunigraz.imi.bst.trec.experiment.ExperimentsBuilder;

public class KeywordExperimenter {
	public static void main(String[] args) {
		final File keywordTemplate = new File(
				KeywordExperimenter.class.getResource("/templates/keyword.json").getFile());
		final File keywordsSource = new File(KeywordExperimenter.class.getResource("/negative-keywords/").getFile());

		ExperimentsBuilder builder = new ExperimentsBuilder();

		File[] files = null;
		if (keywordsSource.isDirectory()) {
			files = keywordsSource.listFiles();
		} else {
			files = new File[] { keywordsSource };
		}

		for (File keywordFile : files) {
			List<String> lines;
			try {
				lines = FileUtils.readLines(keywordFile, "UTF-8");
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}

			for (String keyword : lines) {
				builder.newExperiment().withGoldStandard(Experiment.GoldStandard.FINAL)
						.withTarget(Experiment.Task.PUBMED).withKeyword(keyword).withTemplate(keywordTemplate)
						.withWordRemoval();
			}
		}

		Set<Experiment> experiments = builder.build();

		for (Experiment exp : experiments) {
			exp.start();
			try {
				exp.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		for (Experiment exp : experiments) {

		}
	}

}
