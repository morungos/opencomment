package uk.org.opencomment;

public class Response {
	private String text = "";
	
	public void addParagraph(String value) {
		text = text + "<p>" + value + "</p>";
	}
	
	public void addList(StringArray values) {
		String value = "<ul>\n";
		for (String o : values) {
			value = value + "<li>" + o.toString() + "</li>\n";
		}
		value = value + "</ul>";
		text = text + value;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String body) {
		text = body;
	}
}
