/**
 * Ext.workspace.ux.Grid

 * @author mixam
 * @date 26.09.2018
 */
Ext.define('Ext.cubes.ux.Grid', {
    extend: 'Ext.grid.Panel',

    defaultListenerScope: true,

    doCreateRecord: function () {
        this.store.add({});
    },

    doRemoveRecord: function () {
        var view = this.view;
        Ext.MessageBox.confirm('Внимание!', 'Вы действительно хотите удалить выбранные записи?', function (b) {
            if (b === 'yes') {
                var recs = view.selModel.getSelection();
                view.store.remove(recs);
                //
                // var ids = [];
                // Ext.each(recs, function (rec) {
                //     ids.push(rec.id);
                // });
                //
                // Ext.Ajax.request({
                //     url: view.store.model.getProxy().getUrl(),
                //     jsonData: {
                //         ids: ids
                //     },
                //     params: {
                //         ids: ids
                //     },
                //     method: 'DELETE',
                //     success: function () {
                //         view.store.remove(recs);
                //     }
                // });
            }
        });
    },
    doReload: function () {
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
            enableOn:['singleSelect'],
            handler: 'doEditRecord'
        },
        remove: {
            text: 'Удалить',
            iconCls: 'x-fa fa-trash',
            enableOn:['singleSelect'],
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
