/**
 */
Ext.define('overrides.Element', {
    override: 'Ext.dom.Element',
    /**
     * fix err
     */
    unmask: function () {
        if (!this.destroyed) this.callParent(arguments);
    }
});
