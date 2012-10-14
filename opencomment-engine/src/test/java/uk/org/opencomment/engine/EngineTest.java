package uk.org.opencomment.engine;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.org.opencomment.classifiers.RegexClassifier;

import junit.framework.TestCase;

public class EngineTest extends TestCase {

    private Logger log = LoggerFactory.getLogger(RegexClassifier.class);

    public void testGetFeedback() throws Exception {
        Engine engine = new Engine();
        String result = engine.getFeedback("default", "file:src/test/resources/question_joll_1A.xml", "It's the end of an era", 1);
        assertNotNull(result);
        assertTrue(result.length() > 0);
    }

    public void testAbsoluteFeedback() throws Exception {
        Engine engine = new Engine();
        File test = new File("src/test/resources/question_rule.xml");
        test = test.getAbsoluteFile();
        String testUrl = test.toURI().toURL().toString();
        if (log.isDebugEnabled()) {
            log.debug("Testing: " + testUrl);
        }
        String result = engine.getFeedback("A207.maps", testUrl, "Hello", 1);
        if (log.isDebugEnabled()) {
            log.debug("Result for: 'Hello' is: " + result);
        }
    }

    public void testSampleFeedback() throws Exception {
        Engine engine = new Engine();
        File test = new File("src/test/resources/question_rule.xml");
        test = test.getAbsoluteFile();
        String testUrl = test.toURI().toURL().toString();
        if (log.isDebugEnabled()) {
            log.debug("Testing: " + testUrl);
        }
        String result = engine.getFeedback("A207.maps", testUrl, "Do philosophers dine in Corsica?", 1);
        if (log.isDebugEnabled()) {
            log.debug("Result for: 'Do philosophers dine in Corsica?' is: " + result);
        }
    }

    public void testNoQuestion(){
        Engine engine = new Engine();
        try {
            engine.getFeedback("A208.maps", "file:src/data/question_rule.xml", "Hello", 1);
            fail("Failed to throw exception for missing question");
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug("Correctly throwing exception for missing question");
            }
        }
    }
}
