package uk.org.opencomment.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.WrappedException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import uk.org.opencomment.Answer;
import uk.org.opencomment.classifiers.Classifier;

public class Engine implements FeedbackEngine {
	
	DocumentBuilder builder;
	Document doc;
	Map<String, Classifier> classifiers = new TreeMap<String, Classifier>();
	List<String> rules = new ArrayList<String>();
	
	List<String> segments = new ArrayList<String>();
	
	Segmenter segmenter = null;
	
	//private final static String namespace = "http://openmentor.org.uk/opencomment.xsd";
	
	//private static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
	//private static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";

	private Log log = LogFactory.getLog(Engine.class);
	
	//static final String schemaSource = "jar:file:/Users/stuart/Documents/workspace/Open Comment Service/target/OpenComment.jar!/src/xsd/opencomment.xsd";
	//static final String JAXP_SCHEMA_SOURCE =
	//   "http://java.sun.com/xml/jaxp/properties/schemaSource";
	
	public Engine() {
		super();
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			factory.setNamespaceAware(false);
			factory.setValidating(false);
			
			//factory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA); // use LANGUAGE here instead of SOURCE
			//factory.setAttribute(JAXP_SCHEMA_SOURCE, 
			//		Engine.class.getClassLoader().getResource("opencomment.xsd").toString());
			
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public Classifier getClassifier(String name) {
		return classifiers.get(name);
	}
	
	public void parseConfiguration(String theQuestion, String theUrl) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Parsing configuration file: " + theUrl);
		}
		
		doc = builder.parse(theUrl);
		
		Element qElement = null;
		NodeList questions = doc.getElementsByTagName("question");
		System.err.println(questions.getLength());
		System.err.println("Q: " + theQuestion);
		
		for(int i = 0; i < questions.getLength(); i++) {
			Element element = (Element) questions.item(i);
			System.err.println(element.getAttribute("id"));
			if (element.getAttribute("id").equals(theQuestion)) {
				qElement = element;
			}
		}
		
		if (qElement == null) {
			if (log.isErrorEnabled()) {
				log.error("Question not found");
			}
			throw new Exception("Question not found");
		}
		
		if (qElement == null) {
			if (log.isErrorEnabled()) {
				log.error("Can't locate question: " + theQuestion);
			}
			throw new Exception("Can't locate question: " + theQuestion);
		}
		
		NodeList segElement = qElement.getElementsByTagName("segment");
		if (segElement == null) {
			if (log.isErrorEnabled()) {
				log.error("Segmenter pattern not defined");
			}
			throw new Exception("Segmenter pattern not defined");
		} else {
			segmenter = new Segmenter(segElement.item(0).getTextContent());
		}
		
		classifiers.clear();
		NodeList classElements = qElement.getElementsByTagName("concept");
		for(int i = 0; i < classElements.getLength(); i++) {
			Element element = (Element) classElements.item(i);
			String name = element.getAttribute("name");
			String c = element.getAttribute("class");
			Classifier classifier = (Classifier) Engine.class.getClassLoader().loadClass(c).newInstance();
			classifier.initialize(name, element);
			classifiers.put(name, classifier);
		}
		
		rules.clear();
		NodeList ruleElements = qElement.getElementsByTagName("rules");
		for(int i = 0; i < ruleElements.getLength(); i++) {
			Element element = (Element) ruleElements.item(i);
			String text = element.getTextContent();
			rules.add(text);
		}
	}

	public void initialiseEngine() throws Exception {
		// Nothing needed yet, although setting up any caching would be 
		// very sensible and advisable for most purposes. 
	}
	
	public String getFeedback(String theQuestion, String theDescriptionUrl, 
			                  String theAnswer, int theAttempt) throws Exception {
		
		parseConfiguration(theQuestion, theDescriptionUrl);
		return getFeedback(theAnswer, theAttempt);
	}
	
	public String getFeedback(String theAnswer, int theAttempt) throws Exception {
		Answer answer = new Answer();
		answer.setText(theAnswer);
		answer.setAttempt(theAttempt);
		
		if (log.isDebugEnabled()) {
			log.debug("Requesting feedback on \"" + theAnswer + "\" (attempt " + theAttempt);
		}
		
		List<String> segments = segmenter.segment(theAnswer);
		for (String s : segments) { 
			for (String name : classifiers.keySet()) {
				if (getClassifier(name).classify(s)) {
					if (log.isDebugEnabled()) {
						log.debug("Matched concept: " + name + " on segment: " + s);
					}
					answer.concepts.add(name);
				}
			}
		}
		
		StringBuilder concepts = new StringBuilder();
		String debugSetting = System.getProperty("debug.opencomment", "false");
		if (debugSetting != null && debugSetting.equals("true")) {
			if (log.isDebugEnabled()) {
				log.debug("Recording concepts for output");
			}
			for (String name : answer.concepts) {
				if (concepts.length() > 0) {
					concepts.append(", ");
				}
				concepts.append(name);
			}
		}
		
		Context cx = Context.enter();
		
		try {
            Scriptable scope = cx.initStandardObjects();            
            Object JSanswer = Context.javaToJS(answer, scope);
            ScriptableObject.putProperty(scope, "answer", JSanswer);
            for(String r : rules) {
            	try {
            		Object result = cx.evaluateString(scope, r, "<rule>", 1, null);
            		if (log.isDebugEnabled()) {
            			log.debug("JavaScript result: " + result.toString());
            		}
            	} catch (WrappedException v) {
            		if (v.getWrappedException() instanceof Answer.AnswerValue) {
            			Answer.AnswerValue value = (Answer.AnswerValue) v.getWrappedException();
            			if (log.isDebugEnabled()) {
                			log.debug("JavaScript exit value: " + value.getValue());
            			}
            			break;
            		} else {
            			throw v;
            		}
            	}
            }
        } finally {
            Context.exit();
        }
		
        if (log.isDebugEnabled()) {
        	log.debug("Answer test: " + answer.response.getText());
        }
        String text = answer.response.getText();
        if (concepts.length() > 0) {
        	if (log.isDebugEnabled()) {
        		log.debug("Including concepts for debugging");
        	}
        	text = text+ "<p>DEBUG: Matched concepts: <b>" + concepts.toString() + "</b></p>\n";
        }
        text = "<html>\n" + text + "</html>\n";
        return text;
		
//		segment(theAnswer);
//		identifyConcepts();
//		generateResponse();
//		
//		String result = "Answer: <p><b>" + theAnswer + "</b></p><br>\n";
//		List<Response> pos = new ArrayList<Response>();
//		List<Response> sugg = new ArrayList<Response>();
//		pos.addAll(positiveResponses);
//		sugg.addAll(suggestionResponses);
//		if (pos.size() > 3) {
//			Collections.shuffle(pos);
//			pos = pos.subList(0, 3);
//		}
//		if (sugg.size() > 3) {
//			Collections.shuffle(sugg);
//			sugg = sugg.subList(0, 3);
//		}
//		result += "<p>Well done</p>\n";
//		if (! pos.isEmpty()) {
//			result += "<ul>\n";
//			for(Response r : pos)
//				result += "<li>" + r.getText() + "</li>\n";
//			result += "</ul>\n";
//		}
//		result += "<p>Possible areas of further improvement include:</p>\n";
//		if (! sugg.isEmpty()) {
//			result += "<ul>\n";
//			for(Response r : sugg)
//				result += "<li>" + r.getText() + "</li>\n";
//			result += "</ul>\n";
//		}
//		return "<html>\n" + result + "</html>\n";
	}
}
