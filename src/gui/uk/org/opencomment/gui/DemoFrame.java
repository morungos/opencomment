package uk.org.opencomment.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import uk.org.opencomment.engine.Engine;
import uk.org.opencomment.engine.FeedbackEngine;

public class DemoFrame extends JFrame {
	private static final long serialVersionUID = -4390623581302568195L;
	JButton feedbackButton, selectButton;
	JLabel response;
	JTextArea answer, qtext;
	JTextField fileLabel;
	JComboBox question;
	JSpinner attempt;
	FeedbackEngine feedback;
	
	File selectedFile;
	
	//private static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
	//private static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
	
	//private final static String namespace = "http://openmentor.org.uk/opencomment.xsd";

	//static final String schemaSource = "jar:file:/Users/stuart/Documents/workspace/Open Comment Service/target/OpenComment.jar!/src/xsd/opencomment.xsd";
	//    "http://java.sun.com/xml/jaxp/properties/schemaSource";
	
	List<String> questions = new ArrayList<String>();

	DocumentBuilder builder;
	Document doc;
	
	private Log log = LogFactory.getLog(Engine.class);
	
	public DemoFrame() {
		super("Open Comment Demonstration");
		
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

		feedback = new uk.org.opencomment.engine.Engine();
		setSize(450, 250);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		ActionListener chooseListener = new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				setFileAction();
			}
		};
		
		ActionListener questionListener = new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				setQuestionAction();
			}
		};
		
		ActionListener feedbackListener = new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				handleFeedback();
			}
		};
		
		JPanel pane = new JPanel();
		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		selectButton = new JButton("Choose File...");
		c.weightx = 0;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		c.insets = new Insets(8, 8, 0, 8);
		c.anchor = GridBagConstraints.LINE_START;
		pane.add(selectButton, c);
		selectButton.addActionListener(chooseListener);
		
		fileLabel = new JTextField();
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridy = 1;
		c.insets = new Insets(8, 8, 8, 8);
		pane.add(fileLabel, c);
		
		JLabel qlabel = new JLabel("Choose question identifier:", null, JLabel.LEFT);
		c.gridy = 2;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(8, 8, 0, 8);
		c.gridwidth = 1;
		pane.add(qlabel, c);
		
		question = new JComboBox();
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridy = 3;
		c.insets = new Insets(8, 8, 8, 8);
		pane.add(question, c);
		question.addActionListener(questionListener);
		
		JLabel qAttempt = new JLabel("Attempt:", null, JLabel.LEFT);
		c.gridy = 2;
		c.gridx = 1;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(8, 8, 0, 8);
		pane.add(qAttempt, c);
		
		attempt = new JSpinner();
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridy = 3;
		c.insets = new Insets(8, 8, 8, 8);
		pane.add(attempt, c);
		attempt.setValue(new Integer(1));
		
		JLabel questionLabel = new JLabel("Question:", null, JLabel.LEFT);
		c.gridx = 0;
		c.gridy = 4;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(8, 8, 0, 8);
		c.gridwidth = 2;
		pane.add(questionLabel, c);
		
		qtext = new JTextArea(5, 50);
		JScrollPane qScrollPane = 
		    new JScrollPane(qtext,
		                    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
		                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		c.gridy = 5;
		c.insets = new Insets(8, 8, 8, 8);
		pane.add(qScrollPane, c);

		JLabel label = new JLabel("Enter your answer:", null, JLabel.LEFT);
		c.gridy = 6;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(8, 8, 0, 8);
		pane.add(label, c);
		
		answer = new JTextArea(5, 50);
		JScrollPane scrollPane = 
		    new JScrollPane(answer,
		                    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
		                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		c.gridy = 7;
		c.insets = new Insets(8, 8, 8, 8);
		pane.add(scrollPane, c);
		
		feedbackButton = new JButton("Get Feedback");
		feedbackButton.setEnabled(false);
		c.gridy = 8;
		c.insets = new Insets(8, 8, 0, 8);
		c.anchor = GridBagConstraints.LINE_START;
		pane.add(feedbackButton, c);
		feedbackButton.addActionListener(feedbackListener);
		
		response = new JLabel();
		response.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		response.setVerticalAlignment(JLabel.TOP);
		response.setPreferredSize(new Dimension(300, 200));
		response.setMaximumSize(response.getPreferredSize());
		c.weightx = 1;
		c.weighty = 1;
		c.gridy = 9;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(8, 8, 24, 8);
		response.setText("\n\n\n\n\n\n\n\n\n");
		pane.add(response, c);

		add(pane, BorderLayout.NORTH);
		pack();
	}
	
	public static void main(String args[]) {
		DemoFrame frame = new DemoFrame();
		frame.setBounds(100, 100, 650, 700);
		frame.setVisible(true);
	}
	
	public void setFileAction() {
		final FileDialog fc = new FileDialog(this, "Choose a file");
		fc.setVisible(true);
		if (fc.getFile() != null) {
			setFile(new File(fc.getDirectory(), fc.getFile()));
		}
	}
	
	public void setQuestionAction() {
		String id = (String) question.getSelectedItem();
		try {
			doc = builder.parse(selectedFile);
			
			Element qElement = null;
			NodeList questions = doc.getElementsByTagName("question");
			for(int i = 0; i < questions.getLength(); i++) {
				Element element = (Element) questions.item(i);
				if (element.getAttribute("id").equals(id)) {
					qElement = element;
				}
			}

			if (qElement == null) {
				if (log.isErrorEnabled()) {
					log.error("Question not found");
				}
				throw new Exception("Question not found");
			}
			
			NodeList text = qElement.getElementsByTagName("text");
			if (text == null) {
				if (log.isErrorEnabled()) {
					log.error("Question text not specified");
				}
				throw new Exception("Question text not specified");
			}
			String questionText = text.item(0).getTextContent();
			questionText = questionText.trim();
			qtext.setText(questionText);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setFile(File theFile) {
		if (log.isDebugEnabled()) {
			log.debug("Setting file: " + theFile);
		}
		try {
			selectedFile = theFile.getAbsoluteFile();
			fileLabel.setText(selectedFile.toString());
			Vector<String> names = new Vector<String>();

			// Now we can look for all the questions!
			doc = builder.parse(selectedFile);
			NodeList questions = doc.getElementsByTagName("question");
			for(int i = 0; i < questions.getLength(); i++) {
				Element element = (Element) questions.item(i);
				String id = element.getAttribute("id");
				if (log.isDebugEnabled()) {
					log.debug("Found question: " + id);
				}
				names.add(id);
			}
			question.setModel(new DefaultComboBoxModel(names));
			question.setSelectedIndex((names.size() > 0) ? 0 : -1);
		} catch (Exception e) {
			e.printStackTrace();
			feedbackButton.setEnabled(false);
		} finally {
			feedbackButton.setEnabled(true);
		}
	}
	
	public void handleFeedback() {
		String theAnswer = answer.getText();
		String theURL = selectedFile.toString();
		try {
			theURL = selectedFile.toURI().toURL().toExternalForm();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (log.isDebugEnabled()) {
			log.debug("Using URL: " + theURL);
		}
		Integer attemptNumber = (Integer) attempt.getValue();
		response.setText(getFeedback((String)question.getSelectedItem(), theURL, theAnswer, attemptNumber.intValue()));
	}
	
	public String getFeedback(String theQuestion, String theDescriptionUrl, 
			                  String theAnswer, int theAttempt) {
		try {
			feedback.initialiseEngine();
			return feedback.getFeedback(theQuestion, theDescriptionUrl,  theAnswer, theAttempt);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return "Error! (" + e.getClass().getName() + ") " + e.getMessage();
		}
	}
}
