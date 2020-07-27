/**
 * Ext.cubes.controller.ExceptionHandler
 *
 * @author mixam
 * @date 27/07/2019
 */
Ext.define('Ext.cubes.controller.ExceptionHandler', {
    extend: 'Ext.app.Controller',
    init: function () {

        function showException(e) {
            Ext.Msg.show({
                title: 'Ошибка выполнения запроса',
                msg: e.message,
                buttons: Ext.Msg.OK,
                icon: Ext.Msg.ERROR
            });
        }

        Ext.Ajax.on('requestcomplete', (conn, resp, opts) => {
            resp.responseObject = Ext.decode(resp.responseText);
            // upload exception
            if (resp.responseObject.success === false) showException(resp.responseObject);
        });

        Ext.Ajax.on('requestexception', function (conn, resp, opts) {
            switch (resp.status) {
                case -1:
                    // Aborted by customer
                    break;
                case 401:
                    // Unauthorized
                    break;
                case 500:
                    showException(Ext.decode(resp.responseText));
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
