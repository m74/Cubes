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

    openTab: function (token) {
        var tab = this.down('#' + token);
        if (!tab) {
            var cfg = Ext.getApplication().shortcuts.get(token);
            if (!cfg) {
                Ext.each(Ext.getApplication().registry, function (item) {
                    if (item.regexp.test(token)) {
                        cfg = item;
                        return false;
                    }
                });
            }
            if (cfg) {
                cfg.itemId = token;
                tab = this.add(cfg);
            }
            else console.log('config not found: ', token);
        }
        if (tab) this.setActiveTab(tab);

    }
});
