/**
 * Ext.cubes.view.TabMenu
 *
 * @author mixam
 * @date 12/02/2020
 */
Ext.define('Ext.cubes.view.TabMenu', {
    extend: 'Ext.ux.TabCloseMenu',
    alias: ['plugin.tabmenu'],
    requires: [],
    createMenu() {
        let menu = this.callParent(arguments);
        if (!menu.down('#permanent')) {
            menu.add({
                itemId: 'permanent',
                text: 'Постоянная вкладка',
                checked: false,
                handler: this.doPermanent,
                scope: this
            });
        }
        return menu;
    },
    listeners: {
        beforemenu: function (menu, item, me) {
            let mi = menu.down('#permanent');
            mi.setDisabled(item.itemId === 'dashboard');
            mi.setChecked(!item.closable);
        }
    },
    doPermanent() {
        this.item.setClosable(!this.item.getClosable());
        this.tabPanel.fireEvent('tabstatechange', this.item);
    }
});
