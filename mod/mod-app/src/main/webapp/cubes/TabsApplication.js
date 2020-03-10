/**
 * Ext.cubes.TabsApplication
 *
 * @author mixam
 * @date 18/02/2020
 */
Ext.define('Ext.cubes.TabsApplication', {
    extend: 'Ext.cubes.Application',
    requires: [],

    init() {
        this.on('viewready', user => this.onViewReady(user));
        this.callParent(arguments);
    },

    onViewReady(user) {
    },

    openWorkspace() {
        if (!this.workspace) {
            this.workspace = this.getMainView().add({
                border: false,
                layout: 'fit',
                tbar: [{
                    text: 'Пуск',
                    itemId: 'start',
                    iconCls: 'x-fa fa-home',
                    menu: {
                        xclass: 'Ext.cubes.view.Menu',
                        items: this.menu
                    }
                }, '->'],
                items: {
                    xclass: 'Ext.cubes.view.Workspace',
                    stateful: true,
                    stateId: 'workspace',
                    border: false,
                    items: this.tabs
                },
                listeners: {
                    scope: this,
                    render: panel => {
                        this.tbar = panel.down('toolbar[dock=top]');
                        this.tabs = panel.down('workspace');
                        this.fireEvent('viewready', panel);
                        Ext.fireEvent('workspaceready', this, panel);
                    }
                }
            });
        }
    }
});
