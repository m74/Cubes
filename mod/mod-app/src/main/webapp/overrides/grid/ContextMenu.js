/**
 * overrides.grid.ContextMenu
 *
 * @author mixam
 * @date 05/09/2019
 */
Ext.define('overrides.grid.ContextMenu', {
    override: 'Ext.grid.Panel',

    initComponent: function () {
        this.callParent(arguments);

        if (this.contextMenu) {
            // this.contextMenu = this.applyActions(this.contextMenu);
            this.contextMenu = Ext.create('Ext.menu.Menu', {
                floatParent: this,
                items: this.contextMenu
            });
            this.on('rowcontextmenu', function (g, r, d, i, e) {
                e.stopEvent();
                if (!this.selModel.isSelected(r)) this.setSelection(r);
                this.contextMenu.showAt(e.getXY());
            }, this);
        }
    }
});
