/**
 * Ext.workspace.view.Users

 * @author mixam
 * @date 25.09.2018
 */
Ext.define('Ext.cubes.view.Users', {
    extend: 'Ext.cubes.ux.Grid',
    requires: ['Ext.cubes.model.User'],

    title: 'Пользователи',
    xtype: 'users',

    store: {
        model: 'Ext.cubes.model.User',
        autoSync: true,
        autoLoad: true,
    },

    plugins: {
        cellediting: true
    },

    columns: [{
        text: '#',
        width: 50,
        dataIndex: 'id'
    }, {
        text: 'UUID',
        dataIndex: 'uuid'
    }, {
        text: 'Логин',
        editor: 'textfield',
        dataIndex: 'login'
    }, {
        text: 'Имя',
        editor: 'textfield',
        dataIndex: 'fullName'
    }, {
        text: 'Пароль',
        editor: 'textfield',
        renderer: function () {
            return '********';
        },
        dataIndex: 'password'
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
        text: 'Права',
        // editor: {
        //     xtype: 'multiselectcombo',
        //     store: [{id: 1, text: 'helo'}]
        // },
        renderer: function (v) {
            return Ext.util.Format.array(v, 'title');
        },
        dataIndex: 'permissions'
    }, {
        text: 'Комментарии',
        flex: 1,
        editor: 'textfield',
        dataIndex: 'comments'
    }]
});
