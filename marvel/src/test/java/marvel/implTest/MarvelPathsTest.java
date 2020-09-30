package marvel.implTest;

import graph.Graph;
import marvel.MarvelPaths;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * This class contains a set of test cases that can be used to test the implementation
 * of the MarvelPaths class.
 */
public class MarvelPathsTest {

    /**
     * handle the exception when the given characters are null
     * throw new IllegalArgumentException if one of them is null
     */
    @Test
    public void testFindPathCatchException() {
        try {
            MarvelPaths.bFSFindPath(new Graph<String, String>(), null, null);
            fail("Expected IllegalArgumentException();");
        } catch (IllegalArgumentException ex) {
            // DO nothing
        } catch (Exception e) {
            fail("Wrong Exception thrown");
        }
    }
}