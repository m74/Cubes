/**
 * overrides.grid.Panel
 *
 * @author mixam
 * @date 09/08/2019
 */
Ext.define('overrides.view.Table', {
    override: 'Ext.view.Table',
    applySelectionModel: function () {
        var grid = this.ownerGrid;
        var sm = this.callParent(arguments);
        sm.on('selectionchange', function (sm, recs) {
            grid.setEnableFlags({
                multiSelect: recs.length > 0,
                singleSelect: recs.length === 1
            });
        });
        return sm;
    }

});
