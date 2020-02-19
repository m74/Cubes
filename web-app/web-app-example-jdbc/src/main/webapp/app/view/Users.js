/**
 * Ext.workspace.view.Users

 * @author mixam
 * @date 25.09.2018
 */
Ext.define('Test.view.Users', {
    extend: 'Ext.cubes.ux.Grid',
    requires: ['Test.model.User'],

    xtype: 'users',
    title: 'Пользователи',
    store: {
        autoSync: true,
        autoLoad: true,
        model: 'Test.model.User'
    },

    plugins: {
        cellediting: true
    },

    columns: [{
        text: '#',
        width: 50,
        dataIndex: 'id'
    }, {
        text: 'Логин',
        editor: 'textfield',
        dataIndex: 'login'
    }, {
        text: 'Имя',
        editor: 'textfield',
        dataIndex: 'name'
    }, {
        text: 'Пароль',
        editor: 'textfield',
        renderer: function () {
            return '********';
        },
        dataIndex: 'password'
    }, {
        text: 'Тип',
        renderer: function (v) {
            return v && v.title;
        },
        dataIndex: 'type'
    }, {
        text: 'Создан',
        xtype: 'datecolumn',
        dataIndex: 'createAt'
    }, {
        text: 'Последнее обращение',
        xtype: 'datecolumn',
        dataIndex: 'lastAccess'
    }, {
        text: 'Действующий',
        xtype: 'booleancolumn',
        editor: 'checkbox',
        dataIndex: 'active'
    }, {
        text: 'Комментарии',
        flex: 1,
        editor: 'textfield',
        dataIndex: 'comments'
    }]
});
