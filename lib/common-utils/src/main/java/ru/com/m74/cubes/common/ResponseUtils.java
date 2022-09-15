package ru.com.m74.cubes.common;

import org.apache.commons.io.FileUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ResponseUtils {

    public static void write(HttpServletResponse response, File file) throws IOException {
        write(response, FileUtils.readFileToByteArray(file));
    }

    public static void write(HttpServletResponse response, byte[] data) throws IOException {
        response.setContentLength(data.length);
        ServletOutputStream out = response.getOutputStream();
        out.write(data);
    }

    public static void setResponseFileName(HttpServletResponse response, String fileName, boolean inLine) {
        try {
            String fn = fileName != null ? URLEncoder.encode(fileName, "UTF-8").replace("+", "%20") : "";
            response.setHeader("Content-Disposition", (inLine ? "inline" : "attachment") + "; filename*=UTF-8''" + fn);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setPdfHeaders(HttpServletResponse response) {
        response.setHeader("Content-Type", "application/pdf");
        response.setHeader("X-Frame-Options", "SAMEORIGIN");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate");
        response.setDateHeader("Expires", 0);
    }

}