package at.medunigraz.imi.bst.trec;

import java.io.File;
import java.util.Set;

import at.medunigraz.imi.bst.trec.experiment.Experiment;
import at.medunigraz.imi.bst.trec.experiment.ExperimentsBuilder;

public class RunnerDemo {
	public static void main(String[] args) {
		final File pmTemplate = new File(RunnerDemo.class.getResource("/templates/negative-boost-keywords.json").getFile());

		final File ctTemplate = new File(RunnerDemo.class.getResource("/templates/improved-ct.json").getFile());

		ExperimentsBuilder builder = new ExperimentsBuilder();

		builder.newExperiment().withGoldStandard(Experiment.GoldStandard.EXAMPLE).withTarget(Experiment.Task.PUBMED)
				.withTemplate(pmTemplate).withWordRemoval();
		builder.newExperiment().withGoldStandard(Experiment.GoldStandard.EXTRA).withTarget(Experiment.Task.PUBMED)
				.withTemplate(pmTemplate).withWordRemoval();
		builder.newExperiment().withGoldStandard(Experiment.GoldStandard.FINAL).withTarget(Experiment.Task.PUBMED)
				.withTemplate(pmTemplate).withWordRemoval();
		builder.newExperiment().withGoldStandard(Experiment.GoldStandard.EXTRA)
				.withTarget(Experiment.Task.CLINICAL_TRIALS).withTemplate(ctTemplate).withWordRemoval();
		builder.newExperiment().withGoldStandard(Experiment.GoldStandard.FINAL)
				.withTarget(Experiment.Task.CLINICAL_TRIALS).withTemplate(ctTemplate).withWordRemoval();

		Set<Experiment> bestExperiments = builder.build();

		for (Experiment exp : bestExperiments) {
			exp.start();
			try {
				exp.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		for (Experiment exp : bestExperiments) {

		}
	}

}
