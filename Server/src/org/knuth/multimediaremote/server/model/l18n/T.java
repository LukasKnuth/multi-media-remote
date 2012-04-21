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

    /** Contains translated strings for View-elements */
    public static class View{

        /**
         * Translated strings for the {@code Instructions}-class.
         * @see org.knuth.multimediaremote.server.view.elements.Instructions
         */
        public static class Instructions{

            /** The instructions printed on the gui */
            public static final String instructions = "view.instructions.instructions_string";
            /** Label for not running servers */
            public static final String not_running = "view.instructions.not_running";
            /** Label for servers which are not activated */
            public static final String deactivated = "view.instructions.deactivated";
        }

        /**
         * Translated strings for the {@code Configurator}-class.
         * @see org.knuth.multimediaremote.server.model.settings.Configurator
         */
        public static class Configurator{
            
            /** The label for the "start"-button */
            public static final String start = "view.configurator.start_button";
            /** The label for the "stop"-button */
            public static final String stop = "view.configurator.stop_button";
            /** The label for the WebEnd switch */
            public static final String webend = "view.configurator.webend_switch";
            /** The label for the port-textfield */
            public static final String port = "view.configurator.port_textfield";

        }

        /**
         * Translated strings for the {@code ErrorDialog}-class.
         * @see org.knuth.multimediaremote.server.view.elements.ErrorDialog
         */
        public static class ErrorDialog{
            
            /** The title for the dialog-window */
            public static final String title = "view.error_dialog.title";
            /** The instructions-string in the dialog */
            public static final String instructions = "view.error_dialog.instructions";
        }
    }
}
