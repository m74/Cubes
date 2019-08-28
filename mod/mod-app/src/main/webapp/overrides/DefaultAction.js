/**
 * overrides.container.DefaultAction
 *
 * @author mixam
 * @date 27/08/2019
 */
Ext.define('overrides.DefaultAction', {
    override: 'Ext.panel.Table',
    initComponent: function () {
        var me = this;
        this.callParent(arguments);
        if (me.actions && me.defaultAction) {
            this.on('rowdblclick', function () {
                me.doAction(me.defaultAction);
            });
            this.on('rowkeydown', function (grid, rec, tr, index, e) {
                if (e.keyCode === Ext.event.Event.ENTER) {
                    me.doAction(me.defaultAction);
                }
            });

        }
    },
    doAction: function (name) {
        var a = this.actions[name];
        if (a) a.execute();
    }
});
