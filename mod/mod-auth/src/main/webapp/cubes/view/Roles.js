/**
 * Ext.workspace.view.Users

 * @author mixam
 * @date 25.09.2018
 */
Ext.define('Ext.cubes.view.Roles', {
    extend: 'Ext.cubes.ux.Grid',
    requires: [
        'Ext.cubes.model.Role'
    ],
    xtype: 'roles',
    title: 'Роли',

    store: {
        autoLoad: true,
        autoSync: true,
        model: 'Ext.cubes.model.Role'
    },

    plugins: {
        cellediting: true
    },

    columns: [{
        text: '#',
        width: 150,
        dataIndex: 'id'
    }, {
        text: 'Наименние',
        editor: 'textfield',
        flex: 1,
        dataIndex: 'title'
    }]
});
