/**
 *
 */
Ext.History.init();

Ext.define('Ext.cubes.view.HistoryPlugin', {
    alias: ['plugin.history'],
    requires: [
        'Ext.util.History'
    ],
    init: function (tabs) {
        var me = this;
        this.tabs = tabs;

        tabs.on('tabchange', function (tabs, newTab, oldTab) {
            Ext.History.add(newTab.itemId || '', true);
        }, this);

        Ext.History.on('change', this.onChange, this);
        tabs.on('afterrender', function () {
            me.onChange(Ext.History.currentToken);
        });
    },

    onChange: function (token) {
        // var app = Ext.getApplication();
        // if (token !== app.lastToken) {
        //     console.log('change: ', token);

        Ext.WindowManager.each(function (win) {
            if (win.modal) {
                win.close();
            }
        });

        var path = token.split('/');
        if (Ext.isEmpty(path[0])) {
            path.shift();
        }

        if (!Ext.isEmpty(path)) this.tabs.openTab(path);
        // }
    }
});