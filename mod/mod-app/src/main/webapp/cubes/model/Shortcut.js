/**
 * Ext.cubes.model.Shortcut
 *
 * @author mixam
 * @date 26.09.2018
 */
Ext.define('Ext.cubes.model.Shortcut', {
    extend: 'Ext.data.Model',
    idProperty: 'itemId',
    fields: [
        'itemId',
        'title',
        'xtype',
        'xclass'
    ],
    identifier: {
        type: 'sequential',
        prefix: 'shortcut_'
    }
});
