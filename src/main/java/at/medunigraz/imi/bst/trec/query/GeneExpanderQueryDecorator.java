package at.medunigraz.imi.bst.trec.query;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import at.medunigraz.imi.bst.trec.model.Gene;
import at.medunigraz.imi.bst.trec.model.Result;
import at.medunigraz.imi.bst.trec.model.Topic;

public class GeneExpanderQueryDecorator extends QueryDecorator {

	private static final Logger LOG = LogManager.getLogger();

	private final File geneInfo = new File(getClass().getResource("/genes/Homo_sapiens.gene_info").getFile());

	private static final String TOKEN_SEPARATOR = " ";

	private Map<String, Gene> symbolToGene = new HashMap<>();

	private Gene.Field[] expandTo;

	public GeneExpanderQueryDecorator(Gene.Field[] expandTo, Query decoratedQuery) {
		super(decoratedQuery);
		this.expandTo = expandTo;
		readGenes();
	}

	@Override
	public List<Result> query(Topic topic) {
		expandGenes(topic);
		return decoratedQuery.query(topic);
	}

	private void readGenes() {
		LOG.info("Reading genes...");

		try (CSVReader reader = new CSVReader(new FileReader(geneInfo), '\t', CSVWriter.NO_QUOTE_CHARACTER)) {
			String[] line;
			while ((line = reader.readNext()) != null) {
				Gene gene = Gene.fromArray(line);
				symbolToGene.put(gene.getSymbol(), gene);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		LOG.info("Reading genes complete.");
	}

	private void expandGenes(Topic topic) {
		String[] geneTokens = topic.getGene().split(TOKEN_SEPARATOR);

		StringBuilder expandedGenes = new StringBuilder();
		for (String token : geneTokens) {
			if (symbolToGene.containsKey(token)) {
				Gene gene = symbolToGene.get(token);
				String expansion = gene.getExpandedField(TOKEN_SEPARATOR, expandTo);
				expandedGenes.append(expansion);
			} else {
				expandedGenes.append(token);
			}
			expandedGenes.append(TOKEN_SEPARATOR);
		}

		topic.withGene(expandedGenes.toString().trim());
	}

	/**
	 * Creates a dump in SOLR synonym file
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		final File solrSynonyms = new File("geneSynonyms.txt");
		BufferedWriter writer = new BufferedWriter(new FileWriter(solrSynonyms));

		final Gene.Field[] expandTo = { Gene.Field.DESCRIPTION, Gene.Field.SYNONYMS, Gene.Field.OTHER_DESIGNATIONS };
		// Gene.Field.SYMBOL,
		// Gene.Field.DESCRIPTION,
		// Gene.Field.SYNONYMS,
		// Gene.Field.OTHER_DESIGNATIONS

		GeneExpanderQueryDecorator decorator = new GeneExpanderQueryDecorator(expandTo, null);

		for (Map.Entry<String, Gene> entry : decorator.symbolToGene.entrySet()) {
			String symbol = entry.getKey();
			Gene gene = entry.getValue();

			StringBuilder sb = new StringBuilder(symbol);
			sb.append(" => ");
			sb.append(gene.getExpandedField(", ", expandTo));
			sb.append("\n");

			writer.write(sb.toString());
		}

		writer.close();
	}

}
