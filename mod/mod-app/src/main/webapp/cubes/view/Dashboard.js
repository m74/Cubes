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
    initComponent() {
        const store = Ext.StoreMgr.lookup({
            type: 'array',
            fields: ['id', 'title', 'permissions'],
            filters: [{
                filterFn: rec => Ext.hasPermissions(rec.get('permissions'))
            }]
        });
        const arr = [];
        Ext.each(this.initialConfig.data, xtype => {
            const cls = Ext.ClassManager.getByAlias('widget.' + xtype);
            if (cls) {
                const cfg = cls.prototype.config;
                if (Ext.hasPermissions(cls.prototype.permissions)) {
                    arr.push({
                        id: xtype,
                        title: cfg.title,
                        iconCls: cfg.iconCls || 'fa fa-cogs'
                    });
                }
            }
        });

        store.setData(arr);
        this.items = {
            xtype: 'dataview',
            cls: 'x-dashboard-list',
            store: store,
            tpl: [
                '<ul class=""><tpl for=".">',
                '<li class="x-dashboard-item"><div><a href="#{id}"><i class="{iconCls}"></i></a></div><div class="title">{title}</div></li>',
                '</tpl></ul>'
            ],
            itemSelector: 'a.x-dashboard-item',
            emptyText: '<div style="padding: 10px">Ваша рабочая область не содержит элементов ...</div>'
        };
        this.callParent(arguments);
    }
});
