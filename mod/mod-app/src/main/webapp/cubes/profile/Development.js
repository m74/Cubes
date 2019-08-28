/**
 * Ext.cubes.profile.Development

 * @author mixam
 * @date 27/07/2019
 */
Ext.define('Ext.cubes.profile.Development', {
    extend: 'Ext.app.Profile',
    requires: [],
    isActive: function () {
        return true;
    },
    launch: function () {
        var app = this.getApplication();
        if (app.params.xclass) {
            var cmp = Ext.create(app.params.xclass, {});
            window.cmp = cmp;

            if (cmp.isXType('window')) {
                cmp.modal = false;
                cmp.show();
            } else {
                if (cmp.isXType('field')) {
                    cmp.setFieldLabel('field');
                    cmp = Ext.create({
                        xtype: 'form',
                        items: [cmp]
                    });
                }
                app.getMainView().add(cmp);
                app.getMainView().setActiveItem(cmp);
            }
        }
    }
});
