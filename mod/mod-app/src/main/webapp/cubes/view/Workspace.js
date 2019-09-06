/**
 * Ext.cubes.view.Workspace

 * @author mixam
 * @date 27/07/2019
 */
Ext.define('Ext.cubes.view.Workspace', {
    extend: 'Ext.tab.Panel',
    requires: [
        'Ext.cubes.view.HistoryPlugin'
    ],
    xtype: 'workspace',
    plugins: ['history'],

    defaults: {
        border: false,
        enableHistory: true,
        closable: true
    },

    add: function () {
        function handle(items) {
            return Ext.Array.map(items, function (item) {
                if (Ext.isString(item) && item[0] === '@') {
                    item = Ext.getApplication().shortcuts.get(item.substr(1));
                }
                return item;
            })
        }

        if (arguments.length === 1 && Ext.isArray(arguments[0])) {
            return this.callParent(handle(arguments[0]));
        } else {
            return this.callParent(handle(Ext.Array.slice(arguments)));
        }
    },

    loadRecord: function (model, id, success) {
        var me = this;

        me.mask('Идет загрузка ...');
        if (Ext.isString(model)) {
            model = Ext.Loader.syncRequire(model);
        }
        model.load(id, {
            callback: function () {
                me.unmask();
            },
            success: success
        });
    },

    openTab: function (cfg) {
        var tab = this.down('#' + cfg.itemId);
        if (!tab) tab = this.add(cfg);
        this.setActiveItem(tab);
    }
});
