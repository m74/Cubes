package ru.com.m74.cubes.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.apache.commons.io.IOUtils.copy;

public class Zipper extends ZipOutputStream {

    private static final Logger logger = LoggerFactory.getLogger(Zipper.class);

    public Zipper(OutputStream out) {
        super(out);
    }

    private Map<String, Integer> names = new HashMap<>();

    public void addEntry(String fileName, File file) throws IOException {
        addEntry(fileName, new FileInputStream(file));
    }

    public void addEntry(String fileName, InputStream in) throws IOException {
        Integer count = names.get(fileName);
        if (count == null) {
            names.put(fileName, 1);
        } else {
            names.put(fileName, count + 1);

            int i = fileName.lastIndexOf('.');
            if (i != -1) {
                fileName = fileName.substring(0, i) + " (" + count + ")" + fileName.substring(i);
            } else {
                fileName = fileName + " (" + count + ")";
            }
        }

        logger.debug("add entity: {}", fileName);

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
