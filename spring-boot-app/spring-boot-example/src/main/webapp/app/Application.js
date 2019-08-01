/**
 * Test.Application

 * @author mixam
 * @date 27/07/2019
 */
Ext.define('Test.Application', {
    extend: 'Ext.cubes.Application',
    requires: [],

    launch: function () {
        this.callParent(arguments);

        // this.on('login', function () {
        this.getMainView().add({
            border: false,
            layout: 'fit',
            tbar: [{
                text: 'Пуск',
                iconCls: 'x-fa fa-home',
                menu: {
                    xclass: 'Ext.cubes.view.ShortcutsMenu',
                    items: this.menu
                }
            }, '->', {
                text: 'Выход',
                action: 'logout'
            }],
            items: {
                xclass: 'Ext.cubes.view.Workspace',
                border: false,
                items: this.tabs
            }
        });
        // }, this);
    }
});
