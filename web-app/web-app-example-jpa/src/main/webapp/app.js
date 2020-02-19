/**
 *
 */
Ext.application({
    extend: 'Test.Application',

    paths: {
        'Ext.cubes': 'cubes'
    },

    name: 'Test',

    requires: [
        'Ext.cubes.view.Users',
        'Ext.cubes.view.Roles',
        'Test.TestPanel'
    ],

    menu: [{
        text: 'Администрирование',
        menu: ['users', 'roles']
    }],

    tabs: [{
        title: 'Dashboard',
        itemId: 'dashboard',
        closable: false,
        xclass: 'Ext.cubes.view.Dashboard',
        data: ['users', 'roles', 'testpanel']
    }]

});