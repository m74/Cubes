package ru.com.m74.cubes.common;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.apache.commons.io.IOUtils.copy;

public class Zipper extends ZipOutputStream {

    public Zipper(OutputStream out) {
        super(out);
    }

    public void addEntry(String fileName, File file) throws IOException {
        addEntry(fileName, new FileInputStream(file));
    }

    public void addEntry(String fileName, InputStream in) throws IOException {
        ZipEntry zipEntry = new ZipEntry(fileName);
        putNextEntry(zipEntry);
        copy(in, this);
        flush();
        closeEntry();
        in.close();
    }

//    public static void packZip(OutputStream outputStream, Map<String, InputStream> sources) throws IOException {
//        final Zipper zipOut = new Zipper(outputStream);
//        for (String fileName : sources.keySet()) {
//            InputStream inputStream = sources.get(fileName);
//            zipOut.addEntry(fileName, inputStream);
//        }
//        zipOut.close();
//    }
}
