/**
 * Ext.workspace.ux.Grid

 * @author mixam
 * @date 26.09.2018
 */
Ext.define('Ext.cubes.ux.Grid', {
    extend: 'Ext.grid.Panel',

    defaultListenerScope: true,

    doCreateRecord() {
        Ext.MessageBox.confirm('Внимание!', 'Создать новую запись в таблице?', b => {
            if (b === 'yes') {
                this.store.insert(0, {});
                this.selModel.select(0);
            }
        });

    },

    doRemoveRecord() {
        removeSelectedRecord(this, {msg:'Вы действительно хотите удалить выбранные записи?'})
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
            hotkey: {ctrl: true, key: 'N'},
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
            hotkey: 'Delete',
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
        '->',
        '@remove'
        // "@reload"
    ],

    contextMenu: ['@remove'],
    bbar: {
        xtype: 'pagingtoolbar',
        displayInfo: true
    }
});


function removeSelectedRecord(grid, cfg, cb) {
    let rec = grid.selection;
    Ext.MessageBox.show(Ext.apply({
        title: 'Внимание',
        buttons: Ext.MessageBox.YESNO,
        icon: Ext.MessageBox.QUESTION,
        buttonText: {
            yes: 'Удалить',
            no: 'Отмена (Esc)'
        },
        fn: buttonId => {
            if ('yes' === buttonId) {
                grid.mask('Удаляем выбранную запись...');
                // rec.erase({
                //     callback: () => grid.unmask() && cb && cb()
                // });
                Ext.Ajax.request({
                    url: (rec.proxy || rec.store.proxy).url + '/' + rec.id,
                    method: 'DELETE',
                    success: () => {
                        grid.store.setAutoSync(false);
                        grid.store.remove(rec);
                        // rec.parentNode.removeChild(rec)
                        grid.store.setAutoSync(true);
                    },
                    callback: () => grid.unmask() && cb && cb()
                });
            }
        }
    }, cfg));
}