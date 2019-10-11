/**
 * Ext.workspace.ux.desktop.MenuItem
 *
 * @author mixam
 * @date 27.09.2018
 */
Ext.define('Ext.cubes.view.MenuItem', {
    extend: 'Ext.menu.Item',
    alias: 'widget.opentabmenuitem',

    initComponent: function () {
        if (Ext.isArray(this.menu)) {
            this.menu = {
                xclass: 'Ext.cubes.view.Menu',
                items: this.menu
            }
        }
        this.callParent(arguments);
    },

    handler: function () {
        Ext.util.History.add(this.itemId);
    }
});