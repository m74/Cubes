/**
 * Ext.cubes.TabsApplication
 *
 * @author mixam
 * @date 18/02/2020
 */
Ext.define('Ext.cubes.TabsApplication', {
    extend: 'Ext.cubes.Application',
    requires: [],

    createWorkspace() {
        this.getMainView().add({
            border: false,
            layout: 'fit',
            tbar: [{
                text: 'Пуск',
                iconCls: 'x-fa fa-home',
                menu: {
                    xclass: 'Ext.cubes.view.Menu',
                    items: this.menu
                }
            }, '->'],
            items: {
                xclass: 'Ext.cubes.view.Workspace',
                border: false,
                items: this.tabs
            },
            listeners: {
                scope: this,
                render: panel => {
                    this.menu = panel.down('toolbar[dock=top]');
                    this.tabs = panel.down('workspace');
                    this.fireEvent('viewready', panel)
                }
            }
        });
    }

});
