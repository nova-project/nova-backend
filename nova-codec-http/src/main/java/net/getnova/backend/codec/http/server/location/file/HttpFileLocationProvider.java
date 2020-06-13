package net.getnova.backend.codec.http.server.location.file;

import lombok.EqualsAndHashCode;
import net.getnova.backend.codec.http.server.HttpLocationProvider;

import java.io.File;

@EqualsAndHashCode
public final class HttpFileLocationProvider implements HttpLocationProvider<HttpFileLocation> {

    private final String baseDir;
    private final boolean edit;

    /**
     * Creates a new {@link HttpFileLocationProvider}.
     *
     * @param baseDir the folder from where the files should be taken
     * @param edit    if the files are allowed to edit
     */
    public HttpFileLocationProvider(final File baseDir, final boolean edit) {
        this.baseDir = baseDir.getAbsolutePath();
        this.edit = edit;
    }

    @Override
    public HttpFileLocation getLocation() {
        return new HttpFileLocation(this.baseDir, this.edit);
    }
}
