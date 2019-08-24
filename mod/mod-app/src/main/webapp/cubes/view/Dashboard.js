Ext.Loader.loadScriptsSync('cubes/view/Dashboard.css');
/**
 * Ext.cubes.view.Dashboard
 *
 * @author mixam
 * @date 30.09.2018
 */
Ext.define('Ext.cubes.view.Dashboard', {
    extend: 'Ext.Panel',
    requires: [],

    xtype: 'dashboard',

    layout: 'fit',
    initComponent: function () {
        var store = Ext.StoreMgr.lookup({
            type: 'array',
            model: 'Ext.cubes.model.Shortcut',
            filters: [{
                filterFn: function (rec) {
                    return rec.id !== 'Dashboard';
                }
            }]
        });
        store.setData(Ext.getApplication().shortcuts.items);
        this.items = {
            xtype: 'dataview',
            cls: 'x-dashboard-list',
            store: store,
            tpl: [
                '<ul class=""><tpl for=".">',
                '<li class="x-dashboard-item"><div><a href="#{itemId}"><i class="fa fa-cogs"></i></a></div><div class="title">{title}</div></li>',
                '</tpl></ul>'
            ],
            itemSelector: 'a.x-dashboard-item',
            emptyText: 'No widgets'
        };
        this.callParent(arguments);
    }
});
