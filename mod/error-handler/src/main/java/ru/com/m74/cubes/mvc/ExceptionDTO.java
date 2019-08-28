package ru.com.m74.cubes.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mixam
 * @since 21.06.16 12:26
 */
public class ExceptionDTO {
    private String message;
    private String type;
    private Long reference;
    private List<String> stackTrace = new ArrayList<>();
    private ExceptionDTO cause = null;

    public ExceptionDTO(Throwable ex) {
        message = ex.getMessage();
        type = ex.getClass().getName();

        for (StackTraceElement el : ex.getStackTrace()) {
            stackTrace.add(el.toString());
        }

        if (ex.getCause() != null) {
            cause = new ExceptionDTO(ex.getCause());
            reference = cause.reference;
        } else {
            reference = System.currentTimeMillis();
            Logger logger = LoggerFactory.getLogger(getClass());
            logger.error(reference + ":", ex);
        }
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }

    public Long getReference() {
        return reference;
    }

    public boolean isSuccess() {
        return false;
    }

    public List<String> getStackTrace() {
        return stackTrace;
    }

    public ExceptionDTO getCause() {
        return cause;
    }
}
