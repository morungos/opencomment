package uk.org.opencomment;

import junit.framework.TestCase;

public class ConceptTest extends TestCase {

	public void testConcept() {
		Concept c = new Concept("name");
		assertNotNull(c);
	}

	public void testGetName() {
		Concept c = new Concept("name");
		assertNotNull(c);
		assertEquals(c.getName(), "name");
	}
}
