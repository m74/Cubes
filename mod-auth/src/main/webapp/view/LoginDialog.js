/**
 * Панель авторизации пользователя
 */
Ext.define('Ext.cubes.view.LoginDialog', {
    extend: 'Ext.Window',

    // defaultMessage: 'Укажите имя пользователя и пароль для получения доступа к ресурсам системы',
    // padding: '90px 15px 0',

    modal: true,
    closable: false,
    autoShow: true,
    defaultFocus: 'field',

    layout: 'anchor',

    width: 380,
    title: 'Авторизация',
    border: true,
    bodyPadding: 20,
    defaults: {
        anchor: '100%'
    },
    actions: {
        login: {
            text: 'Войти',
            handler: 'doLogin'
        }
    },

    controller: {
        doLogin: function () {
            var values = {};
            Ext.each(this.view.query('textfield[name]'), function (f) {
                values[f.name] = f.getValue();
            });
            this.view.handleLogin(values);
        }
    },

    items: [{
        xtype: 'textfield',
        fieldLabel: 'Пользователь',
        name: 'username',
        value: 'admin',
        allowBlank: false
    }, {
        xtype: 'textfield',
        fieldLabel: 'Пароль',
        name: 'password',
        value: 'admin',
        inputType: 'password',
        allowBlank: false
    }],
    buttons: ['@login'],

    initComponent: function () {
        this.callParent(arguments);

        this.on('afterRender', function () {
            this.down('field[name=username]').inputEl.dom.autocomplete = 'on';
            this.down('field[name=password]').inputEl.dom.autocomplete = 'on';


            // this.keyNav = Ext.create('Ext.util.KeyNav', this.el, {
            //     scope: this,
            //     enter: function (e) {
            //         var f = this.getForm();
            //         if (f.isValid()) {
            //             this.onSubmit();
            //         } else {
            //             f.getFields().each(function (f) {
            //                 if (!f.isValid()) {
            //                     f.focus(true, 100);
            //                     return false;
            //                 }
            //             })
            //         }
            //     }
            // });
        }, this);
    },

    handleLogin: Ext.emptyFn,

    authenticate: function () {
        this.show();
        this.down('textfield[name=username]').focus(true, 500);
    }
});