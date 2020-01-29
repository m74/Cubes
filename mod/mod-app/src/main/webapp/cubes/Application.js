/**
 * Ext.cubes.Application
 *
 * @author mixam
 * @date 26/07/2019
 */
Ext.define('Ext.cubes.Application', {
    extend: 'Ext.app.Application',

    requires: [
        'overrides.Element',
        'overrides.util.Format', 'overrides.Date', 'overrides.grid.column.Boolean', 'overrides.form.field.Date',
        'overrides.form.field.Time', 'overrides.form.Panel',
        'overrides.data.field.Date', 'overrides.Action', 'overrides.EnableFlags', 'overrides.Container',
        'Ext.cubes.data.field.Array',
        'Ext.cubes.model.Shortcut',
        'Ext.cubes.view.MenuItem', 'overrides.data.Model',
        'Ext.cubes.features.HotKeys', 'overrides.DefaultAction', 'overrides.data.field.Field', 'overrides.form.field.ComboBox',
        'overrides.form.field.Checkbox',
        'overrides.window.Window', 'overrides.grid.ContextMenu', 'overrides.toolbar.Toolbar', 'overrides.ux.TabCloseMenu'
    ],

    quickTips: false,

    paths: {
        'Ext.ux': 'ux'
    },

    platformConfig: {
        desktop: {
            quickTips: true
        }
    },

    mainView: {
        xclass: 'Ext.container.Viewport',
        layout: 'card'
    },


    init() {
        Ext.app.Application.instance = this;

        this.params = Ext.Object.fromQueryString(location.search);

        Ext.StoreManager.lookup({
            type: 'array',
            storeId: 'shortcuts',
            fields: ['id', 'text']
        });
    },

    onAppUpdate() {
        Ext.Msg.confirm('Приложение обновлено', 'Это приложение нуждается в обновлении, перезагрузить?',
            function (choice) {
                if (choice === 'yes') {
                    window.location.reload();
                }
            }
        );
    }
});

Ext.grid.filters.filter.Date.prototype.config.fields.lt.text = "Меньше";
Ext.grid.filters.filter.Date.prototype.config.fields.eq.text = "Равно";
Ext.grid.filters.filter.Date.prototype.config.fields.gt.text = "Больше";
Ext.grid.filters.Filters.prototype.menuFilterText = "Фильры";
Ext.grid.filters.filter.String.prototype.emptyText = "Введите строку ...";
Ext.grid.filters.filter.Number.prototype.emptyText = "Введите номер ...";