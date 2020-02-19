/**
 * Test.Application

 * @author mixam
 * @date 27/07/2019
 */
Ext.define('Test.Application', {
    extend: 'Ext.cubes.TabsApplication',
    requires: [],

    controllers: [
        'Ext.cubes.controller.RouteController',
        'Ext.cubes.controller.ExceptionHandler'
        // 'Ext.cubes.controller.SecurityController'
    ],

    launch: function () {
        // this.callParent(arguments);
        this.createWorkspace();
    }
});
