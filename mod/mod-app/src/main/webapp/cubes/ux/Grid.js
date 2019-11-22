/**
 * Ext.workspace.ux.Grid

 * @author mixam
 * @date 26.09.2018
 */
Ext.define('Ext.cubes.ux.Grid', {
    extend: 'Ext.grid.Panel',

    defaultListenerScope: true,

    doCreateRecord() {
        this.store.add({});
    },

    doRemoveRecord() {
        const view = this.view;
        Ext.MessageBox.confirm('Внимание!', 'Вы действительно хотите удалить выбранные записи?', b => {
            if (b === 'yes') view.store.remove(view.selModel.getSelection());
        });
    },
    doReload() {
        this.view.store.loadPage(1);
    },

    selModel: {
        selType: 'rowmodel',
        mode: 'MULTI'
    },

    actions: {
        create: {
            text: 'Создать',
            iconCls: 'x-fa fa-plus',
            handler: 'doCreateRecord'
        },
        edit: {
            text: 'Редактировать',
            iconCls: 'x-fa fa-pencil',
            enableOn: ['singleSelect'],
            handler: 'doEditRecord'
        },
        remove: {
            text: 'Удалить',
            iconCls: 'x-fa fa-trash',
            enableOn: ['singleSelect'],
            handler: 'doRemoveRecord'
        },
        reload: {
            text: 'Перезагрузить',
            iconCls: 'x-fa fa-refresh',
            handler: 'doReload'
        }
    },
    tbar: [
        '@create',
        // '@edit',
        '@remove',
        '|',
        "@reload"
    ],

    bbar: {
        xtype: 'pagingtoolbar',
        displayInfo: true
    }
});
