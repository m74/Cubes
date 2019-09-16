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
        // debugger
        var store = Ext.StoreMgr.lookup({
            type: 'array',
            fields: ['id', 'title', 'roles'],
            filters: [{
                filterFn: function (rec) {
                    return Ext.hasRoles(rec.get('roles'));
                }
            }]
        });
        var arr = [];
        Ext.each(this.initialConfig.data, function (xtype) {
            var cls = Ext.ClassManager.getByAlias('widget.' + xtype);
            var cfg = cls.prototype.config;
            if (Ext.hasRoles(cfg.roles)) {
                arr.push({
                    id: xtype,
                    title: cfg.title
                });
            }
        });

        store.setData(arr);
        this.items = {
            xtype: 'dataview',
            cls: 'x-dashboard-list',
            store: store,
            tpl: [
                '<ul class=""><tpl for=".">',
                '<li class="x-dashboard-item"><div><a href="#{id}"><i class="fa fa-cogs"></i></a></div><div class="title">{title}</div></li>',
                '</tpl></ul>'
            ],
            itemSelector: 'a.x-dashboard-item',
            emptyText: 'No widgets'
        };
        this.callParent(arguments);
    }
});
