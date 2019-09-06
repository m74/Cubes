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
        var me = this, app = Ext.getApplication();

        tabs.on('tabchange', function (tabs, newTab, oldTab) {
            Ext.History.add(newTab.itemId || '', true);
        }, this);

        Ext.History.on('change', this.onChange, this);
        tabs.on('afterrender', function () {
            me.onChange(Ext.History.currentToken);
        });

        Ext.apply(this, {
            handleToken: function (token) {
                var tab = tabs.down('#' + token);
                if (!tab) {
                    var cfg = app.shortcuts.get(token);
                    if (!cfg) {
                        Ext.each(app.registry, function (item) {
                            if (item.regexp.test(token)) {
                                // если handler отработал нормально
                                if (item.fn(tabs, token) !== false) {
                                    return false;
                                }
                            }
                        });
                    }
                    if (cfg) {
                        cfg.itemId = token;
                        tab = tabs.add(cfg);
                    } else {
                        console.log('config not found: ', token);
                    }
                }
                if (tab) tabs.setActiveTab(tab);
            }
        });
    },

    onChange: function (token) {
        Ext.WindowManager.each(function (win) {
            if (win.modal) {
                win.close();
            }
        });

        token = token.replace(/\//, '-');
        if (!Ext.isEmpty(token)) this.handleToken(token);
    }
});