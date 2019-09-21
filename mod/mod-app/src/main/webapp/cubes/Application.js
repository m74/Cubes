/**
 * Ext.cubes.Application
 *
 * @author mixam
 * @date 26/07/2019
 */
Ext.define('Ext.cubes.Application', {
    extend: 'Ext.app.Application',

    requires: [
        'overrides.util.Format', 'overrides.Date', 'overrides.grid.column.Boolean', 'overrides.form.field.Date',
        'overrides.data.field.Date', 'overrides.Action', 'overrides.EnableFlags', 'overrides.Container',
        'Ext.cubes.model.Shortcut',
        'Ext.cubes.view.MenuItem', 'overrides.data.Model',
        'Ext.cubes.features.HotKeys', 'overrides.DefaultAction', 'overrides.data.field.Field', 'overrides.form.field.ComboBox',
        'overrides.window.Window', 'overrides.grid.ContextMenu', 'overrides.toolbar.Toolbar'
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

        for (var id in this.shortcuts) {
            var data = this.shortcuts[id];
            data.itemId = id;
        }

        var shortcuts = new Ext.util.MixedCollection();
        shortcuts.addAll(this.shortcuts);
        this.shortcuts = shortcuts;
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
