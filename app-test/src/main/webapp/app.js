/**
 *
 */
Ext.application({
    extend: 'Test.Application',

    paths: {
        'Ext.cubes': 'cubes'
    },

    name: 'Test',

    profiles: [
        'Ext.cubes.profile.Development'
    ],

    shortcuts: {
        Dashboard: {
            title: 'Dashboard',
            xclass: 'Ext.cubes.view.Dashboard',
            closable: false
        },
        Users: {
            title: 'Пользователи',
            xclass: 'Ext.cubes.view.Users'
        },
        Roles: {
            title: 'Роли',
            xclass: 'Ext.cubes.view.Roles'
        }
    },
    menu: [{
        text: 'Администрирование',
        menu: ['@Users', '@Roles']
    }],

    tabs: ['@Dashboard'],

    controllers: [
        'Ext.cubes.controller.ExceptionHandler',
        'Ext.cubes.controller.SecurityController'
    ]
});