/**
 * Ext.cubes.view.Workspace

 * @author mixam
 * @date 27/07/2019
 */
Ext.define('Ext.cubes.view.Workspace', {
    extend: 'Ext.tab.Panel',
    xtype: 'workspace',

    defaults: {
        border: false,
        closable: true
    },

    plugins:[
        'tabclosemenu',
        'tabreorderer'
    ],
    docTitle: document.title,

    listeners: {
        tabchange: function (tp, tab) {
            document.title = this.docTitle + " - " + tab.title;
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
            success: sukccess,
            failure: function () {
                Ext.Msg.show({
                    title: 'Ошибка',
                    msg: 'Не удалось загрузить документ: ' + id,
                    buttons: Ext.Msg.OK,
                    icon: Ext.Msg.ERROR
                });
            }
        });
    },

    openTab: function (cfg) {
        var tab = this.down('#' + cfg.itemId);
        if (!tab) tab = this.add(cfg);
        this.setActiveItem(tab);
    }
});
