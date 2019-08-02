/**
 * Test.model.User
 *
 * @author mixam
 * @date 24.09.2018
 */
Ext.define('Test.model.User', {
    extend: 'Ext.cubes.model.Base',
    fields: [
        {name: 'id', type: 'int'},
        'login',
        'password',
        {name: 'createAt', type: 'date'},
        {name: 'lastAccess', type: 'date'},
        {name: 'active', type: 'boolean'}
    ]
});
