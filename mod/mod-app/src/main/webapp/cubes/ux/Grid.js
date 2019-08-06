/**
 * Ext.workspace.ux.Grid

 * @author mixam
 * @date 26.09.2018
 */
Ext.define('Ext.cubes.ux.Grid', {
    extend: 'Ext.grid.Panel',

    controller: {
        create: function () {
            this.view.store.add({});
        },
        // edit: function () {
        //     console.log('doEdit: ', arguments, this);
        // },
        remove: function () {
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
        reload: function () {
            this.view.store.loadPage(1);
        }
    },

    selModel: {
        selType: 'rowmodel',
        mode: 'MULTI'
    },

    actions: {
        create: {
            tooltip: 'Создать',
            glyph: 'xf067@FontAwesome',
            handler: 'create'
        },
        // edit: {
        //     tooltip: 'Редактировать',
        //     glyph: 'xf044@FontAwesome',
        //     handler: 'edit'
        // },
        remove: {
            tooltip: 'Удалить',
            glyph: 'xf1f8@FontAwesome',
            handler: 'remove'
        },
        reload: {
            tooltip: 'Перезагрузить',
            glyph: 'xf021@FontAwesome',
            handler: 'reload'
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
    },

    initComponent: function () {
        Ext.apply(this, {
            store: {
                model: this.model,
                remoteFilter: true,
                remoteSort: true,
                autoSync: true,
                autoLoad: true
            }
        });
        this.callParent(arguments);
    }
});
