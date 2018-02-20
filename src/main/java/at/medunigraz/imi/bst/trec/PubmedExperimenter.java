package at.medunigraz.imi.bst.trec;

import java.io.File;
import java.util.Set;

import at.medunigraz.imi.bst.trec.experiment.Experiment;
import at.medunigraz.imi.bst.trec.experiment.ExperimentsBuilder;
import at.medunigraz.imi.bst.trec.model.Gene;

public class PubmedExperimenter {
	public static void main(String[] args) {
		final File boostTemplate = new File(
				PubmedExperimenter.class.getResource("/templates/boost-extra.json").getFile());
		final File geneTemplate = new File(
				PubmedExperimenter.class.getResource("/templates/must-match-gene.json").getFile());
		final File boostKeywordsTemplate = new File(
				PubmedExperimenter.class.getResource("/templates/boost-keywords.json").getFile());
		final File relaxedTemplate = new File(
				PubmedExperimenter.class.getResource("/templates/relaxed.json").getFile());
		final File englishTemplate = new File(
				PubmedExperimenter.class.getResource("/templates/english.json").getFile());
		final File b0Template = new File(PubmedExperimenter.class.getResource("/templates/b0.json").getFile());
		final File synonymTemplate = new File(
				PubmedExperimenter.class.getResource("/templates/synonym.json").getFile());
		final File regexpDrugsTemplate = new File(
				PubmedExperimenter.class.getResource("/templates/regexp-drugs.json").getFile());
		final File negativeBoostKeywordsTemplate = new File(
				PubmedExperimenter.class.getResource("/templates/negative-boost-keywords.json").getFile());
		final File shouldTemplate = new File(PubmedExperimenter.class.getResource("/templates/should.json").getFile());

		final Gene.Field[] expandTo = { Gene.Field.SYMBOL, Gene.Field.DESCRIPTION };

		final Experiment.GoldStandard goldStandard = Experiment.GoldStandard.FINAL;
		final Experiment.Task target = Experiment.Task.PUBMED;

		
		ExperimentsBuilder builder = new ExperimentsBuilder();

		builder.newExperiment().withGoldStandard(goldStandard).withTarget(target).withTemplate(boostTemplate)
				.withWordRemoval();

		builder.newExperiment().withGoldStandard(goldStandard).withTarget(target).withTemplate(boostTemplate)
				.withGeneExpansion(expandTo).withWordRemoval();

		// mugpubgene
		builder.newExperiment().withGoldStandard(goldStandard).withTarget(target)
				.withTemplate(negativeBoostKeywordsTemplate).withGeneExpansion(expandTo).withWordRemoval();

		builder.newExperiment().withGoldStandard(goldStandard).withTarget(target).withTemplate(geneTemplate)
				.withWordRemoval();

		builder.newExperiment().withGoldStandard(goldStandard).withTarget(target).withTemplate(geneTemplate)
				.withGeneExpansion(expandTo).withWordRemoval();

		// mugpubbase
		builder.newExperiment().withGoldStandard(goldStandard).withTarget(target).withTemplate(boostKeywordsTemplate)
				.withWordRemoval();

		builder.newExperiment().withGoldStandard(goldStandard).withTarget(target).withKeyword(String.valueOf(2))
				.withTemplate(relaxedTemplate).withWordRemoval();

		builder.newExperiment().withGoldStandard(goldStandard).withTarget(target).withTemplate(englishTemplate)
				.withWordRemoval();

		builder.newExperiment().withGoldStandard(goldStandard).withTarget(target).withTemplate(b0Template)
				.withWordRemoval();

		builder.newExperiment().withGoldStandard(goldStandard).withTarget(target).withTemplate(synonymTemplate)
				.withWordRemoval();

		builder.newExperiment().withGoldStandard(goldStandard).withTarget(target).withTemplate(boostKeywordsTemplate)
				.withWordRemoval().withDiseaseExpander();

		// mugpubdiseas
		builder.newExperiment().withGoldStandard(goldStandard).withTarget(target)
				.withTemplate(negativeBoostKeywordsTemplate).withWordRemoval().withDiseaseExpander();

		builder.newExperiment().withGoldStandard(goldStandard).withTarget(target).withTemplate(boostKeywordsTemplate)
				.withWordRemoval().withDiseaseReplacer();

		builder.newExperiment().withGoldStandard(goldStandard).withTarget(target).withTemplate(regexpDrugsTemplate)
				.withWordRemoval();

		// mugpubboost
		builder.newExperiment().withGoldStandard(goldStandard).withTarget(target)
				.withTemplate(negativeBoostKeywordsTemplate).withWordRemoval();

		// mugpubshould
		builder.newExperiment().withGoldStandard(goldStandard).withTarget(target).withTemplate(shouldTemplate)
				.withWordRemoval();

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
