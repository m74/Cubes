/**
 *
 */
Ext.define('overrides.form.field.ComboBox', {
    override: 'Ext.form.field.ComboBox',
    valueField: 'value',
    setValue: function (v) {
        if (this.autoLoad && !this.store.isLoaded()) {
            this.store.on('load', function () {
                this.setValue(v)
            }, this, {single: true});
        } else if (v && this.store.find(this.valueField, v[this.valueField] || v, 0, false, false, true) === -1) {
            this.setSelection(this.store.add(v));
        }

        if (v && v[this.valueField]) v = v[this.valueField];

        this.callParent([v]);
    },
    initComponent: function () {
        if (this.url) {
            this.store = {
                autoLoad: this.autoLoad,
                proxy: {
                    type: 'rest',
                    url: this.url
                }
            }
        }
        this.callParent(arguments);
    }
});