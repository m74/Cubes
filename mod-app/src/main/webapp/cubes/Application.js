/**
 * Ext.cubes.Application
 *
 * @author mixam
 * @date 26/07/2019
 */
Ext.define('Ext.cubes.Application', {
    extend: 'Ext.app.Application',

    requires: [
        'overrides.util.Format', 'overrides.Date', 'overrides.grid.column.Boolean', 'overrides.form.field.Date', 'overrides.data.field.Date',
        'Ext.cubes.model.Shortcut',
        'Ext.cubes.view.MenuItem'
    ],

    quickTips: false,

    platformConfig: {
        desktop: {
            quickTips: true
        }
    },

    mainView: {
        xclass: 'Ext.container.Viewport',
        layout: 'card'
    },


    init: function () {
        Ext.app.Application.instance = this;

        this.params = Ext.Object.fromQueryString(location.search);

        this.store = Ext.StoreMgr.lookup({
            storeId: 'shortcuts',
            type: 'array',
            model: 'Ext.cubes.model.Shortcut'
        });
        for (var id in this.shortcuts) {
            var data = this.shortcuts[id];
            data.itemId = id;
            this.store.add(data);
        }
    },

    onAppUpdate: function () {
        Ext.Msg.confirm('Приложение обновлено', 'Это приложение нуждается в обновлении, перезагрузить?',
            function (choice) {
                if (choice === 'yes') {
                    window.location.reload();
                }
            }
        );
    }
});
