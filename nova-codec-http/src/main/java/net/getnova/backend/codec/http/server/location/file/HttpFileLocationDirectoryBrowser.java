package net.getnova.backend.codec.http.server.location.file;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.getnova.backend.codec.http.HttpUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.Arrays;

final class HttpFileLocationDirectoryBrowser {

    private static final byte[] HEAD = ("<!doctype html><html><head><meta charset=\"utf-8\"><style type=\"text/css\">a{color:#0088CC;text-decoration:none}"
            + "tr:nth-child(odd){background-color:#fafafa}tr td:nth-child(2){width:150px;color:#cc8800;text-align:right}tr td:nth-child(3){width:250px;color:#0000cc;"
            + "text-align:center}</style></head><body><h3>Current Location: ").getBytes(StandardCharsets.UTF_8);
    private static final byte[] FOOTER = "</tbody></table></body></html>".getBytes(StandardCharsets.UTF_8);

    private HttpFileLocationDirectoryBrowser() {
        throw new UnsupportedOperationException();
    }

    static ByteBuf create(final String baseDir, final String uri, final File file) {
        final ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeBytes(HEAD);

        String parentName = baseDir.equals(file.getAbsolutePath()) ? null : file.getParent().substring(baseDir.length()).replace(File.separatorChar, '/');
        if (parentName != null && parentName.isEmpty()) parentName = "/";

        final StringBuilder builder = new StringBuilder()
                .append(uri).append("</h3>");
        if (parentName != null)
            builder.append("<a href=\"").append(parentName).append("\">Patent folder: ").append(parentName).append("</a>")
                    .append("<br><br>");

        builder.append("<table><tbody>")

                .append("<tr>")
                .append("<th>Name</th>")
                .append("<th>Size</th>")
                .append("<th>Last Modified</th>")
                .append("</tr>");

        final File[] files = file.listFiles();

        if (files != null) {
            Arrays.sort(files, (file1, file2) -> {
                if (file1.isDirectory() && file2.isDirectory()) return 0;
                else if (file1.isDirectory()) return -1;
                else if (file2.isDirectory()) return 1;
                else return 0;
            });

            boolean folder;
            String name;

            for (final File currentFile : files) {
                if (!HttpUtils.fileExist(currentFile)) continue;

                folder = currentFile.isDirectory();
                name = currentFile.getName();

                builder.append("<tr>")
                        .append("<td><a href=\"").append(name).append("\">").append(name).append("</a></td>")
                        .append("<td>").append(folder ? "" : readableFileSize(currentFile.length())).append("</td>")
                        .append("<td>").append(folder ? "" : HttpUtils.formatDate(currentFile.lastModified())).append("</td>")
                        .append("</tr>");
            }
        }

        byteBuf.writeCharSequence(builder.toString(), StandardCharsets.UTF_8);
        byteBuf.writeBytes(FOOTER);
        return byteBuf;
    }

    private static String readableFileSize(final long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
