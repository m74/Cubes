/**
 * Пустые значения одинаковы ('' == undefined == null == [])
 * Для того, что бы не сбрасывать не измененные значения при сохранении модели
 */
Ext.define('overrides.data.field.Field', {
    override: 'Ext.data.field.Field',

    isEqual: function (v1, v2) {
        return (Ext.isEmpty(v1) && Ext.isEmpty(v2)) || this.callParent(arguments);
    }
});