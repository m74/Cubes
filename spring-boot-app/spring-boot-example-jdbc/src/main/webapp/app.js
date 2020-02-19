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
        'Test.view.Users',
        'Test.TestPanel'
    ],

    menu: [{
        text: 'Администрирование',
        menu: ['users']
    }, 'testpanel'],

    tabs: [
        {
            itemId: 'dashboard',
            title: 'Рабочий стол',
            iconCls: 'x-fa fa-dashboard',
            closable: false,
            xclass: 'Ext.cubes.view.Dashboard',
            data: ['users', 'testpanel']
        }
    ]
});