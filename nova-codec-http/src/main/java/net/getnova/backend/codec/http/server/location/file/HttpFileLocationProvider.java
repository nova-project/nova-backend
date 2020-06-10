package net.getnova.backend.codec.http.server.location.file;

import net.getnova.backend.codec.http.server.HttpLocationProvider;

import java.io.File;

public class HttpFileLocationProvider implements HttpLocationProvider<HttpFileLocation> {

    private final String baseDir;
    private final boolean edit;

    public HttpFileLocationProvider(final File baseDir, final boolean edit) {
        this.baseDir = baseDir.getAbsolutePath();
        this.edit = edit;
    }

    @Override
    public HttpFileLocation getLocation() {
        return new HttpFileLocation(this.baseDir, this.edit);
    }
}
