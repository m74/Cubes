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
        'Test.TestPanel'
    ],

    menu: [{
        text: 'Test',
        menu: ['testpanel']
    }],

    tabs: [{
        title: 'Dashboard',
        itemId: 'dashboard',
        xclass: 'Ext.cubes.view.Dashboard',
        closable: false,
        data: ['testpanel']
    }]

});