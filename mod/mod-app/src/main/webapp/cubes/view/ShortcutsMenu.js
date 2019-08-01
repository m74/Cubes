/**
 * Ext.cubes.view.ShortcutsMenu
 *
 * @author mixam
 * @date 27/07/2019
 */
Ext.define('Ext.cubes.view.ShortcutsMenu', {
    extend: 'Ext.menu.Menu',
    defaultType: 'opentabmenuitem',

    lookupComponent: function (cfg) {
        if (Ext.isString(cfg) && cfg[0] === '@') {
            var id = cfg.substr(1);
            var shortcut = Ext.getApplication().shortcuts.get(id);
            arguments[0] = {
                id: id,
                text: shortcut.title
            };
        }
        return this.callParent(arguments);
    }
});
