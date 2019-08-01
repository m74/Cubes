/**
 * Ext.cubes.controller.ExceptionHandler
 *
 * @author mixam
 * @date 27/07/2019
 */
Ext.define('Ext.cubes.controller.ExceptionHandler', {
    extend: 'Ext.app.Controller',
    init: function () {
        Ext.Ajax.on('requestexception', function (conn, resp, opts) {
            switch (resp.status) {
                case -1:
                    // Aborted by customer
                    break;
                case 401:
                    // Unauthorized
                    break;
                case 500:
                    var r = Ext.decode(resp.responseText);
                    Ext.Msg.show({
                        title: r.error,
                        msg: r.message,
                        buttons: Ext.Msg.OK,
                        icon: Ext.Msg.ERROR
                    });

                    break;
                default:
                    Ext.Msg.show({
                        title: resp.status + ' ' + resp.statusText,
                        msg: 'url: ' + opts.url,
                        width: 300,
                        buttons: Ext.Msg.OK,
                        icon: Ext.Msg.ERROR
                    });
            }
        }, this);
    }
});
