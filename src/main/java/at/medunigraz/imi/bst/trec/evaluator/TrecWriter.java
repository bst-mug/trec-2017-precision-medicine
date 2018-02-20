package at.medunigraz.imi.bst.trec.evaluator;

import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.Set;

import com.opencsv.CSVWriter;

import at.medunigraz.imi.bst.trec.model.Result;
import at.medunigraz.imi.bst.trec.model.ResultList;

public class TrecWriter implements Closeable {
	private static final int NUM_FIELDS = 6;
	private CSVWriter writer;

	public TrecWriter(File output) {
		try {
			writer = new CSVWriter(new FileWriter(output), '\t', CSVWriter.NO_QUOTE_CHARACTER);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void write(Set<ResultList> resultListSet) {
		for (ResultList resultList : resultListSet) {
			write(resultList);
		}
	}

	public void write(ResultList resultList) {		
		String[] entries = new String[NUM_FIELDS];
		
		// Sets fixed fields
		entries[0] = String.valueOf(resultList.getTopic().getNumber());
		entries[1] = "Q0";
		entries[5] = "mugpubbase"; // XXX must be 1-12 alphanumeric characters
		
		int rank = 1;
		for (Result result : resultList.getResults()) {
			entries[2] = String.valueOf(result.getId());
			entries[3] = String.valueOf(rank++);
			entries[4] = String.format(Locale.ROOT, "%.6f", result.getScore());
			writer.writeNext(entries);
		}
	}
	
	public void flush() {
		try {
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
