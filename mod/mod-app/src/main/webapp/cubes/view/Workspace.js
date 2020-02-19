/**
 * Ext.cubes.view.Workspace

 * @author mixam
 * @date 27/07/2019
 */
Ext.define('Ext.cubes.view.Workspace', {
    extend: 'Ext.tab.Panel',
    requires: [
        'Ext.cubes.view.TabMenu'
    ],
    xtype: 'workspace',

    defaults: {
        border: false,
        closable: true
    },

    plugins: [
        'tabmenu',
        'tabreorderer'
    ],
    docTitle: document.title,

    stateEvents: ['add', 'remove', 'childmove', 'tabstatechange'],

    listeners: {
        tabchange(tp, tab) {
            document.title = this.docTitle + " - " + tab.title;
        }
    },

    openTab(c) {
        let tab;
        if (Ext.isString(c)) c = {xtype: c};
        if (!c.itemId) c.itemId = c.xtype;
        if (!c.itemId) throw new Error('Отсутствует itemId!');

        try {
            const index = this.items.findIndex('itemId', c.itemId);
            if (index >= 0) {
                tab = this.items.getAt(index);
            } else {
                tab = this.add(c);
            }
            if (!this.inStateRestore)
                this.setActiveItem(tab);
        } catch (e) {
            console.log('Invalid config: ', c, e);
        }
    },

    getState() {
        const state = this.callParent(arguments);
        state.tabs = Ext.Array.map(this.items.items, item => {
            return {itemId: item.itemId, xclass: item.$className, closable: item.closable}
        });
        return state;
    },

    applyState(state) {
        this.callParent(arguments);
        this.inStateRestore = true;
        Ext.each(state.tabs, tab => this.openTab(tab));
        this.inStateRestore = false;
    }
});
