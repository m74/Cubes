/**
 *
 */
Ext.define('overrides.toolbar.Toolbar', {
    override: 'Ext.toolbar.Toolbar',
    defaultType: 'tdbutton'
});

Ext.define('Ext.toolbar.Button', {
    extend: 'Ext.Button',
    xtype: 'tdbutton',

    setText: function (text) {
        if (this._hasIcon()) {
            this.setTooltip(text)
        } else {
            this.callParent(arguments);
        }
    }
});