//

Ext.define('overrides.data.field.Date', {
    override: 'Ext.data.field.Date',

    dateFormat: 'c',

    convert(v) {
        if (Ext.isNumber(v)) return new Date(v);
        else return this.callParent(arguments);
    }
});