package ru.com.m74.cubes.jdbc.sql;

import ru.com.m74.cubes.jdbc.annotations.Column;
import ru.com.m74.cubes.jdbc.annotations.Join;
import ru.com.m74.cubes.jdbc.annotations.ManyToOne;
import ru.com.m74.cubes.jdbc.annotations.Table;
import ru.com.m74.cubes.jdbc.utils.DTOUtils;
import ru.com.m74.cubes.jdbc.utils.SqlUtils;
import ru.com.m74.cubes.jdbc.utils.Utils;
import ru.com.m74.extjs.dto.Sorter;

import java.lang.reflect.Field;

import static ru.com.m74.cubes.jdbc.utils.SqlUtils.*;

/**
 */
public class Select<T> extends ru.com.m74.cubes.sql.base.Select<T> {

    private String fieldName(Field field) {
        return aliasName(field.getName());
    }

    public Select(Class<T> type) {
        super(type);
        Table table = type.getAnnotation(Table.class);
        if (table == null) {
            throw new RuntimeException("Annotation not present: " + Table.class);
        }

        String tableAlias = tableAlias(table);

        String tableName = tableName(table);
        this.from(Utils.isEmpty(tableAlias) ? tableName : tableName + " " + aliasName(tableAlias));

        Join join = type.getAnnotation(Join.class);
        if (join != null) {
            this.join(join.value());
        }

//        LeftJoin leftJoin = type.getAnnotation(LeftJoin.class);
//        if (leftJoin != null) {
//            this.leftJoin(leftJoin.value());
//        }

        for (Field field : DTOUtils.getAnnotatedModelFields(type)) {
//            Id id = field.getAnnotation(Id.class);
//            if (id != null && isNotEmpty(id.name())) {
//                setHint("/*+ INDEX (" + tableName + " " + id.name() + ") */");
//            }

            Column rsfa = field.getAnnotation(Column.class);

            if (Utils.isNotEmpty(rsfa.sql())) {
                this.field("(" + rsfa.sql() + ") as " + (Utils.isNotEmpty(rsfa.as()) ? rsfa.as() : field.getName()));
            } else {
                String column = getColumnName(type, field);

//                if (field.isAnnotationPresent(DictionaryValue.class)) {
//                    this.leftJoin("NSI_CATALOG " + fieldName(field) + " ON(" + fieldName(field) + ".ID" + "=" + column + ")");
//                    this.field(fieldName(field) + ".ID as " + field.getName() + "_ID");
//                    this.field(fieldName(field) + ".STRUCTURE_HEAD as " + field.getName() + "_TEXT");
//                    this.field(fieldName(field) + ".PREFIX as " + field.getName() + "_CODE");
//                    this.field(fieldName(field) + ".SHORT_BUSINESS_KEY as " + field.getName() + "_BK");
//                } else
                if (field.isAnnotationPresent(ManyToOne.class)) {
                    ManyToOne annotation = field.getAnnotation(ManyToOne.class);
                    String alias = annotation.as();
                    if (Utils.isEmpty(alias)) alias = fieldName(field);

                    if (Utils.isEmpty(annotation.on())) {
                        this.join(
                                "left join " + annotation.table() + " " + alias + " " +
                                        "ON(" + alias + "." + annotation.id() + "=" + column + ")");
                    } else {
                        this.join(
                                "left join " + annotation.table() + " " + alias + " " + "ON(" + annotation.on() + ")");
                    }
                    this.field(alias + "." + annotation.id() + " as " + field.getName() + "_" + annotation.id());
                    String query = annotation.query();
                    if (Utils.isEmpty(query)) {
                        query = alias + "." + annotation.title();
                    }
                    this.field(query + " as " + field.getName() + "_" + annotation.title());
                    if (Utils.isNotEmpty(annotation.bk())) {
                        this.field(alias + "." + annotation.bk() + " as " + field.getName() + "_" + annotation.bk());
                    }
//                    if (field.getType().equals(AttachmentDTO.class)) {
//                        this.field(alias + ".CONTENT_TYPE" + " as " + field.getName() + "_contentType");
//                        this.field(alias + ".FILE_PATH" + " as " + field.getName() + "_filePath");
//                        this.field(alias + ".REAL_FILE_NAME" + " as " + field.getName() + "_realFileName");
//                        this.field(alias + ".PUT_DATE" + " as " + field.getName() + "_putDate");
//                    }
//                } else if (field.isAnnotationPresent(Nsi.class)) {        // данные из НСИ
//                    // добавляем запрос для получения данны из НСИ
//                    this.leftJoin("NSI_CATALOG " + fieldName(field) + " ON(" + fieldName(field) + ".ID" + "=" + column + ")");
//                    this.field(fieldName(field) + ".ID as " + field.getName() + "_ID");
//                    this.field(fieldName(field) + ".STRUCTURE_HEAD as " + field.getName() + "_TEXT");
//                    this.field(fieldName(field) + ".PREFIX as " + field.getName() + "_CODE");
//                    this.field(fieldName(field) + ".SHORT_BUSINESS_KEY as " + field.getName() + "_BK");
//
//                    // проверка на необходимость чтения атрибутов НСИ
//                    if (ListItemDTO.class.isAssignableFrom(field.getType())) {
//                        List<Field> fieldsNsiAttrib = DTOUtils.getModelFields(field.getType(), fieldAttr -> fieldAttr.isAnnotationPresent(NsiAttribute.class));
//
//                        for (Field fieldAttrib : fieldsNsiAttrib) {
//                            String nsiAttrAlias = fieldAttrib.getName() + "_NSIATTR";
//                            String fieldName = DTOUtils.getNsiAttibFieldName(fieldAttrib);
//                            NsiAttribute semantic = fieldAttrib.getAnnotation(NsiAttribute.class);
//
//                            this.leftJoin("NSI_CATALOG_DESC_V " + nsiAttrAlias +
//                                    " ON(" + nsiAttrAlias + ".NODE_GUID" + "=" + fieldName(field) + ".NODE_GUID" + " AND " + nsiAttrAlias + ".semantics='" + semantic.value() + "')");
//                            this.field(nsiAttrAlias + "." + fieldName + " as " + field.getName() + "ATTR_" + fieldAttrib.getName());
//                        }
//                    }
                } else {
                    String as = rsfa.as();
                    if (Utils.isNotEmpty(as)) {
                        this.field(column + " as " + rsfa.as());
                    } else {
                        this.field(column);
                    }

                }
            }
        }
    }

    public void sort(Sorter sorters[]) {
        if (Utils.isNotEmpty(sorters)) {
            for (Sorter sorter : sorters) {
                addOrderBy(SqlUtils.getOrderBy(getType(), sorter.getProperty()), sorter.getDirection());
            }
        }
//        Utils.applySorters(this, getType(), sorters);
    }

//    /**
//     * @param json
//     * @return
//     * @deprecated Use: {@link #sort(Sorter[])}
//     */
//    public Select setSorters(String json) {
//        return setSorters(json, null);
//    }
//
//    /**
//     * @param json
//     * @param substitutes
//     * @return
//     * @deprecated Use: {@link #sort(Sorter[])}
//     */
//    public Select setSorters(String json, Map<String, String> substitutes) {
//        if (json == null) {
//            return this;
//        }
//
//        try {
//            List<Map<String, String>> list = (new ObjectMapper()).readValue(json, List.class);
//            List<String> sorters = new ArrayList<>();
//
//            for (Map<String, String> map : list) {
//                String sortBy = map.get(PROPERTY);
//
//                if (substitutes != null && substitutes.containsKey(sortBy)) {
//                    sorters.add(substitutes.get(sortBy) + " " + map.get("direction"));
//                } else {
//                    Field field = DTOUtils.findField(type, sortBy);
//
//                    if (field == null) {
//                        throw new RuntimeException("Field not found: " + sortBy);
//                    }
//
//                    Column rsfa = field.getAnnotation(Column.class);
//
//                    if (rsfa == null) {
//                        throw new RuntimeException("Annotation @Column not present in property: " + sortBy);
//                    }
//
//                    String val = rsfa.as();
//                    sorters.add((isNotEmpty(val) ? val : rsfa.value()) + " " + map.get("direction"));
//                }
//            }
//
//            if (isNotEmpty(sorters)) {
//                this.orderBy(String.join(", ", sorters));
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        return this;
//    }

}
