package ru.com.m74.cubes.jdbc.utils;

import ru.com.m74.cubes.jdbc.ColumnNotFoundException;
import ru.com.m74.cubes.jdbc.Link;
import ru.com.m74.cubes.jdbc.annotations.LinkTo;
import ru.com.m74.cubes.sql.base.Select;
import ru.com.m74.extjs.dto.Sorter;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static ru.com.m74.cubes.jdbc.utils.Utils.isEmpty;

public class EMUtils {
    public static final String TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ssXXX";

    public static <T> void sort(Select<T> q, Sorter sorters[]) {
        if (Utils.isNotEmpty(sorters)) {
            for (Sorter sorter : sorters) {
                q.addOrderBy(SqlUtils.getOrderBy(q.getType(), sorter.getProperty()), sorter.getDirection());
            }
        }
    }

    /**
     * Получить значение из базы
     *
     * @param rs
     * @param field
     * @return
     * @throws SQLException
     */
    public static Object getResultSetValue(ResultSet rs, Field field) throws ColumnNotFoundException {

        Class fieldType = field.getType();
        String colName = SqlUtils.getResultSetFieldName(field);


//        if (field.isAnnotationPresent(DictionaryValue.class)) {
//            Object id = get(rs, field.getName() + "_ID");
//            return id == null ? null : new ListItemDTO(
//                    id,
//                    get(rs, field.getName() + "_TEXT", String.class),
//                    get(rs, field.getName() + "_BK", String.class),
//                    get(rs, field.getName() + "_CODE", String.class)
//            );
//        }


//        if (field.isAnnotationPresent(Nsi.class)) {
//            try {
//                String nsiAlias = field.getName();
//
//                Object id = get(rs, nsiAlias + "_ID");
//                if (id == null) return null;
//
//                ListItemDTO nsiValue = (ListItemDTO) fieldType.newInstance();
//                nsiValue.setValue(id);
//                nsiValue.setText(rs.getString(nsiAlias + "_TEXT"));
//                nsiValue.setBusinessKey(rs.getString(nsiAlias + "_BK"));
//
//                try {
//                    nsiValue.setCode(rs.getString(nsiAlias + "_CODE"));
//                } catch (SQLException e) {
//                    nsiValue.setCode(null);
//                }
//
//                // проверка на наличие атрибутов
//                List<Field> fieldsNsiAttrib = DTOUtils.getModelFields(field.getType(), fieldAttr -> fieldAttr.isAnnotationPresent(NsiAttribute.class));
//
//                for (Field fieldAttrib : fieldsNsiAttrib) {
//                    Object attr = rs.getObject(nsiAlias + "ATTR_" + fieldAttrib.getName(), fieldType.getDeclaredField(fieldAttrib.getName()).getType());
//
//                    String methodName = "set" + org.apache.commons.lang3.StringUtils.capitalize(fieldAttrib.getName());
//                    Method method = fieldType.getMethod(methodName, fieldType.getDeclaredField(fieldAttrib.getName()).getType());
//                    method.invoke(nsiValue, attr);
//                }
//
//                return nsiValue;
//            } catch (Exception e) {
//                throw new SddSystemException("Ошибка при получении данных НСИ. ", e);
//            }
//        }

        if (field.isAnnotationPresent(LinkTo.class)) {
            LinkTo annotation = field.getAnnotation(LinkTo.class);
            if (fieldType.equals(Link.class)) {
                Object id = get(rs, field.getName() + "_" + annotation.id());
                if (id == null) return null;
                String title = get(rs, field.getName() + "_" + annotation.title(), String.class);
                Link item = new Link(id, title);
                if (Utils.isNotEmpty(annotation.bk())) {
                    item.setBusinessKey(get(rs, field.getName() + "_" + annotation.bk(), String.class));
                }
                return item;
//            } else if (fieldType.equals(AttachmentDTO.class)) {
//                AttachmentDTO dto = new AttachmentDTO();
//                dto.setId(((BigDecimal) id).longValue());
//                dto.setFileName(title);
//                dto.setContentType(get(rs, field.getName() + "_contentType", String.class));
//                dto.setFilePath(get(rs, field.getName() + "_filePath", String.class));
//                dto.setRealFileName(get(rs, field.getName() + "_realFileName", String.class));
//                dto.setPutDate(get(rs, field.getName() + "_putDate", Date.class));
//                return dto;
            }
            throw new RuntimeException("Не совместимы тип: " + fieldType);
        }

        if (fieldType.equals(int.class)) {
            fieldType = Integer.class;
        } else if (fieldType.equals(boolean.class)) {
            fieldType = Boolean.class;
        } else if (fieldType.equals(long.class)) {
            fieldType = Long.class;
        } else if (fieldType.equals(double.class)) {
            fieldType = Double.class;
        }

        return fieldType.equals(Object.class) ? get(rs, colName) : get(rs, colName, fieldType);
    }

    private static Object get(ResultSet rs, String name) throws ColumnNotFoundException {
        return get(rs, name, null);
    }

    private static <T> T get(ResultSet rs, String name, Class<T> type) throws ColumnNotFoundException {
        try {
            return type != null ? rs.getObject(name, type) : (T) rs.getObject(name);
        } catch (SQLException e) {
            throw new ColumnNotFoundException(name, e);
        }
    }

    /**
     * Преобразовать строковое значение к указанному типу
     *
     * @param value значение
     * @param cls   тип
     * @param <T>
     * @return
     */
    public static <T> T cast(Object value, Class<T> cls) {

        if (cls.equals(boolean.class) && isEmpty(value)) {
            value = false;
        }

        if (cls.isInstance(value)) {
            return (T) value;
        }

        if (isEmpty(value)) {
            return null;
        }

        if (!(value instanceof String || value instanceof Number || value instanceof Boolean)) {
            throw new RuntimeException("Not a string, number or boolean value: " + value);
        }

        String strVal = value.toString();

        if (cls.equals(Date.class)) {
            try {
                SimpleDateFormat timestampFormat = new SimpleDateFormat(TIMESTAMP_FORMAT);
                return (T) timestampFormat.parse(strVal);
            } catch (ParseException e) {
                throw new RuntimeException("Ошибка при конвертации строки в дату", e);
            }
        }
        if (cls.equals(Long.class) || cls.equals(long.class)) {
            try {
                if (value instanceof Double) {
                    return (T) new Long(((Double) value).longValue());
                } else {
                    return (T) Long.valueOf(strVal);
                }
            } catch (ClassCastException e) {
                throw new RuntimeException("Ошибка при конвертации строки в число", e);
            }
        }
        if (cls.equals(Integer.class) || cls.equals(int.class)) {
            try {
                return (T) Integer.valueOf(strVal);
            } catch (ClassCastException e) {
                throw new RuntimeException("Ошибка при конвертации строки в число", e);
            }
        }
        if (cls.equals(Double.class) || cls.equals(double.class)) {
            try {
                return (T) Double.valueOf(strVal);
            } catch (ClassCastException e) {
                throw new RuntimeException("Ошибка при конвертации строки в число", e);
            }
        }
        if (cls.equals(Boolean.class) || cls.equals(boolean.class)) {
            try {
                return (T) Boolean.valueOf(strVal);
            } catch (ClassCastException e) {
                throw new RuntimeException("Ошибка при конвертации строки в логический тип", e);
            }
        }
        if (cls.equals(BigDecimal.class)) {
            try {
                return (T) new BigDecimal(strVal);
            } catch (ClassCastException e) {
                throw new RuntimeException("Ошибка при конвертации строки в большое число", e);
            }
        }
//        if (cls.isAnnotationPresent(Dictionary.class)) {
//            return (T) Enum.valueOf((Class<Enum>) cls, strVal);
//        }

        throw new RuntimeException("Unsupported type: " + cls);
    }
}
