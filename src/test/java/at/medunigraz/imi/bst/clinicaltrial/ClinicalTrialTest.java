package at.medunigraz.imi.bst.clinicaltrial;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.isEmptyOrNullString;

import java.io.File;

import org.junit.Test;

public class ClinicalTrialTest {

    @Test
    public void testParsing() {  	
    	File xmlFile = new File(getClass().getResource("/data/clinicaltrials-samples/NCT00283075.xml").getFile());

        ClinicalTrial trial = ClinicalTrial.fromXml(xmlFile.getAbsolutePath());

        assertThat(trial.sex, contains("female", "male"));
        assertThat(trial.minAge, is(18));
        assertThat(trial.maxAge, is(65));
        assertThat(trial.inclusion, containsString("Endstage"));
        assertThat(trial.inclusion, containsString("survival"));
        assertThat(trial.exclusion, containsString("Multiple"));
        assertThat(trial.exclusion, containsString("individual"));
        
        
        xmlFile = new File(getClass().getResource("/data/clinicaltrials-samples/NCT02912559.xml").getFile());

         trial = ClinicalTrial.fromXml(xmlFile.getAbsolutePath());

        assertThat(trial.sex, contains("female", "male"));
        assertThat(trial.minAge, is(18));
        assertThat(trial.maxAge, is(100));
        assertThat(trial.inclusion, containsString("Histologically"));
        assertThat(trial.inclusion, containsString("leucovorin"));
        assertThat(trial.exclusion, isEmptyOrNullString());
    }

}