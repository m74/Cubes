/**
 * Ext.cubes.controller.SecurityController
 *
 * @author mixam
 * @date 27/07/2019
 */
Ext.define('Ext.cubes.controller.SecurityController', {
    extend: 'Ext.app.Controller',
    requires: [],

    api: {
        check: 'remoteUser',
        login: 'j_spring_security_check',
        logout: 'logout'
    },

    control: {
        'viewport': {
            render: 'check'
        },
        'button[action=logout]': {
            click: 'logout'
        }
    },

    init: function (app) {
        // Если не авторизован
        Ext.Ajax.on('requestexception', function (conn, resp) {
            if (resp.status === 401) {
                this.authenticate();
            }
        }, this);

    },

    logout: function () {
        this.mask('Сеанс пользователя завершен, приложение перезагружается...');
        Ext.Ajax.request({
            url: this.api.logout,
            callback: function () {
                location.reload();
            }
        });
    },

    check: function () {
        this.mask("Проверка авторизации ...");
        Ext.Ajax.request({
            scope: this,
            url: this.api.check,
            callback: function () {
                this.unmask();
            },
            failure: function () {
                this.authenticate();
            },
            success: function (r) {
                var resp = Ext.decode(r.responseText);

                if (!resp.success) {
                    this.authenticate();
                } else {
                    this.applyRemoteUser(resp.records);
                    this.login();
                }
            }
        });
    },

    authenticate: function () {
        var me = this;
        this.mask();

        Ext.create('Ext.cubes.view.LoginDialog', {
            handleLogin: function (values) {
                me.form.children[0].value = values.username;
                me.form.children[1].value = values.password;
                me.form.submit();
            }
        });

        this.form = Ext.DomHelper.append(document.body, {
            style: 'display: none;',
            tag: 'form',
            method: 'post',
            action: this.api.login,
            children: [
                {tag: 'input', name: 'username'},
                {tag: 'input', name: 'password', type: 'password'},
                {tag: 'input', type: 'submit'}
            ]
        });
    },
    unmask: function () {
        Ext.getBody().unmask();
    },
    mask: function (msg) {
        var el = Ext.getBody().mask(msg, 'x-center-box mask');
        el.addCls('x-auth-mask');
    },
    applyRemoteUser: function (user) {
        this.application.hasPermissions = Ext.hasPermissions = function (roles) {

            if (Ext.isEmpty(roles)) {
                return true;
            }
            if (!Ext.isArray(roles)) {
                roles = Ext.toArray(arguments);
            }

            for (var i = 0; i < roles.length; i++) {
                var role = roles[i].toLowerCase();
                if (user.roles && user.roles.indexOf(role) !== -1)
                    return true;
            }
            return false;
        };

        this.application.remoteUser = Ext.remoteUser = function () {
            return user;
        };
    },
    login: function () {
        var user = Ext.remoteUser();
        this.mask('Открываем приложение ..');
        this.application.fireEvent('login', this, user);
        this.unmask();
    }
});
