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
        Ext.WindowManager.each(function (win) {
            if (win.modal) {
                win.close();
            }
        });

        this.tabs.openTab(token);
    }
});