/**
 * Ext.cubes.profile.Development

 * @author mixam
 * @date 27/07/2019
 */
Ext.define('Ext.cubes.profile.Development', {
    extend: 'Ext.app.Profile',
    requires: [],
    isActive: function () {
        var app = this.getApplication();
        return app.params.debug !== undefined;
    },
    init: function () {
        var app = this.getApplication();
        app.registry.push({
            regexp: /.*/,
            fn: function (tabs, token) {
                var cls = Ext.Loader.syncRequire(token);
                if (cls) {
                    var cmp = Ext.create(token, {});
                    window.cmp = cmp;

                    if (cmp.isXType('window')) {
                        // cmp.modal = false;
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
                } else {
                    return false;
                }
            }
        });
    }
});
