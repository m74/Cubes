Ext.define('overrides.form.Panel', {
    override: 'Ext.form.Panel',
    initComponent: function () {
        this.callParent(arguments);
        if (this.record) this.loadRecord(this.record);
    }
});