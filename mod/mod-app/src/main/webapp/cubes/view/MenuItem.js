/**
 * Ext.workspace.ux.desktop.MenuItem
 *
 * @author mixam
 * @date 27.09.2018
 */
Ext.define('Ext.cubes.view.MenuItem', {
    extend: 'Ext.menu.Item',
    alias: 'widget.opentabmenuitem',

    initComponent() {
        if (Ext.isArray(this.menu)) {
            this.menu = {
                xclass: 'Ext.cubes.view.Menu',
                items: this.menu
            }
        }
        this.callParent(arguments);
    },

    handler() {
        Ext.util.History.add(this.itemId);
    }
});