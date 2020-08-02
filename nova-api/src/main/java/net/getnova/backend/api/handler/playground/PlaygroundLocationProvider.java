package net.getnova.backend.api.handler.playground;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;
import net.getnova.backend.api.data.ApiEndpointCollectionData;
import net.getnova.backend.codec.http.HttpUtils;
import net.getnova.backend.codec.http.server.HttpLocationProvider;
import net.getnova.backend.json.JsonUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TreeSet;

@Slf4j
class PlaygroundLocationProvider implements HttpLocationProvider<PlaygroundLocation> {

    private final File baseDir;
    private final ByteBuf playground;

    PlaygroundLocationProvider(final TreeSet<ApiEndpointCollectionData> collections) {
        this.baseDir = this.loadBaseDir();
        this.playground = this.generatePlayground(collections);

        /* Create Index file, with instructions how to install the playground. */
        final File indexFile = new File(this.baseDir, "index.html");
        if (!indexFile.exists()) this.createIndexFile(indexFile);
    }

    private File loadBaseDir() {
        final File baseDir = new File("www/playground").getAbsoluteFile();
        if (!baseDir.exists()) baseDir.mkdirs();
        return baseDir;
    }

    private ByteBuf generatePlayground(final TreeSet<ApiEndpointCollectionData> collections) {
        return Unpooled.copiedBuffer(JsonUtils.toJson(collections).toString(), HttpUtils.CHARSET);
    }

    private void createIndexFile(final File indexFile) {
        try (InputStream inputStream = PlaygroundLocationProvider.class.getResourceAsStream("/www/playground/index.html");
             OutputStream outputStream = new FileOutputStream(indexFile)) {
            inputStream.transferTo(outputStream);
        } catch (IOException e) {
            log.warn("Unable to write file {}: {}", indexFile.getPath(), e.toString());
        }
    }

    @NotNull
    @Override
    public PlaygroundLocation getLocation() {
        return new PlaygroundLocation(this.baseDir, this.playground);
    }
}
