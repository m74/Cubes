/**
 * Ext.cubes.data.field.Array
 *
 * @author mixam
 * @date 28/01/2020
 */
Ext.define('Ext.cubes.data.field.Array', {
    extend: 'Ext.data.field.Field',
    alias: ['data.field.array'],

    convert(v) {
        if (Ext.isEmpty(v)) return [];
        if (Ext.isString(v)) return Ext.decode(v);
        return v;
    },

    compare(v1, v2) {
        return this.callParent([String(v1), String(v2)]);
    }
});