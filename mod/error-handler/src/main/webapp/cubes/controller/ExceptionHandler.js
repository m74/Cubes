/**
 * Ext.cubes.controller.ExceptionHandler
 *
 * @author mixam
 * @date 27/07/2019
 */
Ext.define('Ext.cubes.controller.ExceptionHandler', {
    extend: 'Ext.app.Controller',
    init: function () {

        function str2obj(str) {
            return Ext.isEmpty(str) ? {} : Ext.decode(str);
        }

        function showException(e) {
            Ext.Msg.show({
                title: 'Ошибка выполнения запроса',
                msg: e.message,
                buttons: Ext.Msg.OK,
                icon: Ext.Msg.ERROR
            });
        }

        Ext.Ajax.on('requestcomplete', (conn, resp, opts) => {
            resp.responseObject = str2obj(resp.responseText);
            // upload exception
            if (resp.responseObject.success === false) showException(resp.responseObject);
        });

        Ext.Ajax.on('requestexception', function (conn, resp, opts) {
            switch (resp.status) {
                case 0:
                    Ext.Msg.show({
                        title: `Превышен интервал ожидания (${resp.statusText}).`,
                        msg:
                            `Сервер не ответил в течении ${settings['vsrf.ajax.request.timeout'] / 1000}  сек. 
                            <br>
                            Возможно проблема заключается в отстутствии сокдинения с сетью или база данных очень долго формирует ответ по Вашему запросу.
                            <p>
                            Попробуйте повторить Ваш запрос еще раз. Если ошибка повторится обратитесь в тех. поддержку`,
                        width: 600,
                        buttons: Ext.Msg.OK,
                        icon: Ext.Msg.ERROR
                    });
                    break;
                case -1:
                    // Aborted by customer
                    break;
                case 401:
                    // Unauthorized
                    break;
                case 500:
                    showException(str2obj(resp.responseText));
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
