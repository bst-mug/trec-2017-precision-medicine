package at.medunigraz.imi.bst.extraabstracts;

import at.medunigraz.imi.bst.config.TrecConfig;
import at.medunigraz.imi.bst.medline.PubMedArticle;
import at.medunigraz.imi.bst.medline.XmlPubMedArticleSet;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ExtraAbstractsTests {

    @Test
    public void readOneFile() throws Exception {

        PubMedArticle extraAbstract = Indexing.getExtraAbstractFromFile(TrecConfig.SAMPLE_EXTRA_ABSTRACT_TXT);

    }
}
