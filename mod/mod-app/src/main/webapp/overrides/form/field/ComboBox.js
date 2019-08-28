/**
 *
 */
Ext.define('overrides.form.field.ComboBox', {
    override: 'Ext.form.field.ComboBox',
    setValue: function (v) {
        if (v && this.store.find(this.valueField, v[this.valueField], 0, false, false, true) === -1) {
            this.setSelection(this.store.add(v));
        }
        this.callParent([v]);
    },
    initComponent: function () {
        if (this.url) {
            this.store = {proxy: {type: 'rest', url: this.url}}
        }
        this.callParent(arguments);
    }
});