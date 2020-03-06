package ru.com.m74.cubes.common;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;

public class XmlUtils {

    public static <T> T xml2obj(Class<T> type, String xml) {
        if (xml == null) {
            return null;
        }

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(type);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            StringReader reader = new StringReader(xml);
            JAXBElement<T> el = jaxbUnmarshaller.unmarshal(new StreamSource(reader), type);
            return el.getValue();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка приобрахования XML", e);
        }
    }

    public static <T> String obj2xml(T obj) {
        if (obj == null) {
            return null;
        }

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = jaxbContext.createMarshaller();

            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            StringWriter out = new StringWriter();
//            JAXBElement<T> el
//                    = new JAXBElement<T>(new QName("", rootElement), (Class<T>) obj.getClass(), obj);
            marshaller.marshal(obj, out);
            return out.getBuffer().toString();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка приобрахования XML", e);
        }
    }
}
