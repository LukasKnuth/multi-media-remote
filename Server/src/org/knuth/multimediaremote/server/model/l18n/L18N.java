package org.knuth.multimediaremote.server.model.l18n;

import java.util.ResourceBundle;

/**
 * This class holds the mechanism to load all translated strings.
 *  It automatically uses the current locale to translate all
 *  strings to the currently used system-language, if a matching
 *  translation-file was found.</p>
 * If no matching file was found, the default language (which is
 *  English) is used as a fallback.
 * 
 * <h2>Usage</h2>
 * Getting a translated String from this class is done, by using
 *  the constants, defined in the {@code T}-class of this package.</p>
 * An example of how to do this might be:
 * <code>
 *     String translated = L18N.ts(T.Errors.general_error);
 * </code>
 * This will then obtain the translated version of the String with the
 *  identifier stored in {@code T.Errors.general_error}.</p>
 * For the discussion on this pattern, see this StackOverflow question:
 *  <a href="http://stackoverflow.com/questions/10248824">SO 10248824</a>
 * @see T
 * @author Lukas Knuth
 * @version 1.0
 */
public class L18N {

    /**
     * Private constructor - This class is a static utility class.
     */
    private L18N(){}

    /** The translated messages */
    private static final ResourceBundle messages;
    
    static {
        messages = ResourceBundle.getBundle(
                L18N.class.getPackage().getName()+ // The current package's name
                ".translation");
    }

    /**
     * Returns the translated String for the given identifier, using
     *  the current locale as the default language.</p>
     * Instead of passing in the identifier-String yourself, use the
     *  constants defined in the {@code T}-class!
     * @param identifier the identifier for the translated String.
     * @return the translated String.
     * @see T
     */
    public static String tS(String identifier){
        return messages.getString(identifier);
    }

}
