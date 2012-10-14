package uk.org.opencomment;

import java.util.regex.Pattern;

/**
 * A class representing a matcher, designed to be accessible to JavaScript.
 * @author stuart
 */
public class Match {
    
	public static final int INCLUDE = 0;
    public static final int EXCLUDE = 1;

    private Pattern pattern;
    private int type;

    /**
     * @param type
     * @param pattern
     */
    public Match(int type, String pattern) {
        super();
        this.type = type;
        this.pattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
    }

    /**
     * @return
     */
    public Pattern getPattern() {
        return pattern;
    }

    /**
     * @return
     */
    public int getType() {
        return type;
    }
}
