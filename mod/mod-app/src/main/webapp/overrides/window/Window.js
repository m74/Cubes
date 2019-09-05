/**
 * overrides.window.Window
 *
 * @author mixam
 * @date 04/09/2019
 */
Ext.define('overrides.window.Window', {
    override: 'Ext.window.Window',
    initComponent: function () {
        var me = this;
        this.callParent(arguments);

        if (this.layout.type === 'fit' && this.items.length) {
            this.items.get(0).setAutoScroll(true);
        }

        this.on('resize', function (cmp, w, h) {
            var vh = Ext.getBody().getViewSize().height - 20;
            if (vh < h) {
                me.setHeight(vh);
            }
            this.center();
        });
    }
});
