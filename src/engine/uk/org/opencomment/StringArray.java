package uk.org.opencomment;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class StringArray extends ArrayList<String> {
	
	private static final long serialVersionUID = 9101907582918775913L;
	
	private Log log = LogFactory.getLog(StringArray.class);
	
	public boolean find(String name) {
		return indexOf(name) != -1;
	}
	
	public void push(String name) {
		if (log.isDebugEnabled()) {
			log.debug("Added new item: " + name);
		}
		add(name);
	}
	
	public void pushNew(String name) {
		if (indexOf(name) == -1) {
			add(name);
		}
	}
	
	public StringArray concat(StringArray other) {
		if (log.isDebugEnabled()) {
			log.debug("Concatenating items");
		}
		StringArray result = new StringArray();
		result.addAll(this);
		result.addAll(other);
		return result;
	}
}
