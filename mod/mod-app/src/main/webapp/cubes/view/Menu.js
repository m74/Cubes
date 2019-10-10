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


/**
 * Ext.cubes.view.Menu
 *
 * @author mixam
 * @date 10/10/2019
 */
Ext.define('Ext.cubes.view.Menu', {
    extend: 'Ext.menu.Menu',
    defaultType: 'opentabmenuitem',
    requires: [],
    lookupComponent: function (c) {
        if (Ext.isString(c)) {
            var cls = Ext.ClassManager.getByAlias('widget.' + c);
            if (cls) {
                c = {
                    // arguments[0] = c = {
                    itemId: c,
                    text: cls.prototype.config.title,
                    permissions: cls.prototype.permissions
                };
                if (Ext.hasPermissions(c.permissions)) {
                    Ext.StoreManager.lookup('shortcuts').add({
                        id: c.itemId,
                        text: c.text
                    });
                }
            }
        }
        var cmp = this.callParent(arguments);
        if (cmp.getMenu() && cmp.getMenu().items.getCount() === 0) return null;
        return cmp;
    }
});
