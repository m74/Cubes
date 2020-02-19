/**
 * Ext.workspace.model.User

 * @author mixam
 * @date 24.09.2018
 */
Ext.define('Ext.cubes.model.Role', {
    extend: 'Ext.data.Model',
    identifier: 'sequential',
    fields: [
        'id',
        'title'
    ],
    proxy: {
        type: 'rest',
        url: 'Role'
    }
});
