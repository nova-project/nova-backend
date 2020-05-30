package net.getnova.backend;

/**
 * This is the main entry point class of the Nova Backend,
 * is contains the {@link Bootstrap#main(String[])} method,
 * witch is executed first on the bootstrap process.
 *
 * @see Bootstrap#main(String[])
 */
public final class Bootstrap {

    /**
     * Do not create an instance of this class.
     * This class is only used as main entry point of the Nova Backend.
     *
     * @see Bootstrap#main(String[])
     */
    private Bootstrap() {
        throw new UnsupportedOperationException();
    }

    /**
     * The main entry point of the Nova Backend.
     *
     * @param args the program arguments.
     */
    public static void main(final String[] args) {
        new Nova(args);
    }
}
