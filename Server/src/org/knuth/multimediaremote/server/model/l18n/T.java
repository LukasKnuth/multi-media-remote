package org.knuth.multimediaremote.server.model.l18n;

/**
 * This class contains all the identifier-strings, used to get a
 *  translated String.</p>
 * The strings obtained from the constants defined in this class
 *  should be used as the identifier-string for the {@code tS()}-
 *  method of the {@code L18N}-class.
 * @see L18N#tS(String) 
 * @author Lukas Knuth
 * @version 1.0
 */
public class T {

    /**
     * This class only manages static content, so it's not instantiable.
     */
    private T(){}

    /** Contains error-messages! */
    public static class Errors{

        /** An example Error */
        public static final String EXAMPLE = "example";
    }
}
