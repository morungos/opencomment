package uk.org.opencomment;

import java.util.regex.Pattern;

public class Match {
	public static final int INCLUDE = 0;
	public static final int EXCLUDE = 1;
	
	private Pattern pattern;
	private int type;
	
	public Match(int type, String pattern) {
		super();
		this.type = type;
		this.pattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
	}

	public Pattern getPattern() {
		return pattern;
	}

	public int getType() {
		return type;
	}
}
