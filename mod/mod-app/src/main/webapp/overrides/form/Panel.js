Ext.define('overrides.form.Panel', {
    override: 'Ext.form.Panel',
    initComponent: function () {
        this.callParent(arguments);
        if (this.record) {
            if (!this.record.isModel && this.model) {
                this.record = this.model.create(this.record);
            }
            this.loadRecord(this.record);
        }
        if (this.values) this.form.setValues(this.values);
    }
});