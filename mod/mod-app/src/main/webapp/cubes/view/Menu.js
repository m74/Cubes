/**
 * Ext.cubes.view.Menu
 *
 * @author mixam
 * @date 10/10/2019
 */
Ext.define('Ext.cubes.view.Menu', {
    extend: 'Ext.menu.Menu',
    defaultType: 'opentabmenuitem',
    requires: [
        'Ext.cubes.view.MenuItem'
    ],
    lookupComponent: function (c) {
        if (Ext.isString(c)) {
            var cls = Ext.ClassManager.getByAlias('widget.' + c);
            if (cls) {
                c = {
                    // arguments[0] = c = {
                    itemId: c,
                    text: cls.prototype.config.title,
                    iconCls: cls.prototype.config.iconCls,
                    // canAccessible: cls.prototype.canAccessible,
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
