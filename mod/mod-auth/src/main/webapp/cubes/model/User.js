/**
 * Ext.workspace.model.User

 * @author mixam
 * @date 24.09.2018
 */
Ext.define('Ext.cubes.model.User', {
    extend: 'Ext.cubes.model.Base',
    fields: [
        {name: 'id', type: 'int'},
        'login',
        'uuid',
        'fullName',
        'password',
        'comments',
        {name: 'createAt', type: 'date'},
        {name: 'lastAccess', type: 'date'},
        {name: 'active', type: 'boolean'},
        'roles'
    ]
});
