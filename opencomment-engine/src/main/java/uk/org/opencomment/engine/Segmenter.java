package uk.org.opencomment.engine;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class Segmenter {
    private Pattern pattern = null;

    /**
     * @param block
     * @throws Exception
     */
    public Segmenter(String block) throws Exception {
        block = block.trim();
        if (block.startsWith("/") && block.endsWith("/")) {
            block = block.substring(1, block.length() - 1);
        } else {
            throw new Exception("Failed to initialise segmenter: syntax error for pattern");
        }
        pattern = Pattern.compile(block);
    }

    /**
     * @param input
     * @return
     */
    public List<String> segment(String input) {
        String segments[] = pattern.split(input);
        return Arrays.asList(segments);
    }
}
