/**
 */
Ext.define('overrides.Action', {
    override: 'Ext.Action',

    /**
     * Установить tooltip на связанных контролах. Предполагается, что в них есть метод setTooltip.
     * @param {String/Object} tooltip Описание см. в Ext.button.Button.setTooltip
     */
    setTooltip: function (tooltip) {
        this.callEach('setTooltip', [tooltip]);
    }
});


Ext.define('overrides.actions.Button', {
    override: 'Ext.Button',
    fireHandler: function (e) {
        if (Ext.isFunction(this.handler) && !this.scope) {
            this.scope = this.resolveListenerScope();
        }
        this.callParent(arguments);
    }
});

Ext.define('overrides.actions.MenuItem', {
    override: 'Ext.menu.Item',
    onClick: function (e) {
        if (Ext.isFunction(this.handler) && !this.scope) {
            this.scope = this.resolveListenerScope();
        }
        this.callParent(arguments);
    }
});