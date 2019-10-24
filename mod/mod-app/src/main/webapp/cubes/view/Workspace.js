/**
 * Ext.cubes.view.Workspace

 * @author mixam
 * @date 27/07/2019
 */
Ext.define('Ext.cubes.view.Workspace', {
    extend: 'Ext.tab.Panel',
    xtype: 'workspace',

    defaults: {
        border: false,
        closable: true
    },

    plugins: [
        'tabclosemenu',
        'tabreorderer'
    ],
    docTitle: document.title,

    stateEvents: ['add', 'remove', 'childmove'],

    listeners: {
        tabchange: function (tp, tab) {
            document.title = this.docTitle + " - " + tab.title;
        }
    },

    openTab: function (c) {
        var tab;
        if (Ext.isString(c)) c = {itemId: c, xtype: c};

        var index = this.items.findIndex('itemId', c.itemId);
        if (index >= 0) {
            tab = this.items.getAt(index);
        } else {
            tab = this.add(c);
        }
        if (!this.inStateRestore)
            this.setActiveItem(tab);
    },

    getState: function () {
        var state = this.callParent(arguments);
        state.tabs = Ext.Array.map(this.items.items, function (item) {
            return item.itemId;
        });
        return state;
    },

    applyState: function (state) {
        this.callParent(arguments);
        this.inStateRestore = true;
        Ext.route.Router.doRun(state.tabs);
        this.inStateRestore = false;
    }
});
