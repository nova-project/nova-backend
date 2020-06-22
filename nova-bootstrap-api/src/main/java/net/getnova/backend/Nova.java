package net.getnova.backend;

/**
 * This is the main class, of the nova backend.
 */
public interface Nova {

    /**
     * This method triggers the shutdown progress of the backend.
     */
    void shutdown();

    /**
     * Returns the current state of the backend. (debug or no debug)
     *
     * @return the current state of the backend
     */
    boolean isDebug();
}
