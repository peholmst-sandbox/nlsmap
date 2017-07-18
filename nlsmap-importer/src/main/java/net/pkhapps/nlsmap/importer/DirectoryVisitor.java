package net.pkhapps.nlsmap.importer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * TODO Document me
 */
@FunctionalInterface
public interface DirectoryVisitor {

    /**
     * @param path
     */
    void visitDirectory(Path path) throws IOException;

    /**
     * @param directory
     * @param glob
     * @param visitor
     */
    static void visit(Path directory, String glob, DirectoryVisitor visitor) throws IOException {
        for (Path file : Files.newDirectoryStream(directory, glob)) {
            // We can't use forEach since visitDirectory can throw an IOException
            visitor.visitDirectory(file);
        }
    }

    /**
     * @param directory
     * @param glob
     * @param visitor
     */
    static void visit(File directory, String glob, DirectoryVisitor visitor) throws IOException {
        visit(Paths.get(directory.toURI()), glob, visitor);
    }
}
