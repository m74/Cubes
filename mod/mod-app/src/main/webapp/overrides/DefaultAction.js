/**
 * overrides.container.DefaultAction
 *
 * @author mixam
 * @date 27/08/2019
 */
Ext.define('overrides.DefaultAction', {
    override: 'Ext.panel.Table',
    defaultAction: undefined,
    initComponent: function () {
        var action;
        this.callParent(arguments);
        if (this.defaultAction) {
            action = this.getAction(this.defaultAction);
            if (action) {
                this.on('rowdblclick', function () {
                    action.execute();
                });
                this.on('rowkeydown', function (grid, rec, tr, index, e) {
                    if (e.keyCode === Ext.event.Event.ENTER) {
                        action.execute();
                    }
                });
            }
        }
    }
});
