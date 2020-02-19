/**
 * Ext.cubes.controller.RouteController
 *
 * @author mixam
 * @date 18/02/2020
 */
Ext.define('Ext.cubes.controller.RouteController', {
    extend: 'Ext.app.Controller',
    requires: [],

    routes: {
        '*': {
            before: 'onBeforeToken'
        }
        // 'udoc-:id': 'openDoc',
        // 'template-:id': 'openTemplate'
    },

    refs: [{
        ref: 'workspace',
        selector: 'workspace'
    }],

    init() {
        Ext.route.Router.suspend();
        if (Ext.isEmpty(location.hash)) location.hash = localStorage.lastToken || 'dashboard';
    },

    control: {
        'workspace': {
            afterRender: () => Ext.route.Router.resume(),
            tabchange(tabs, newTab, oldTab) {
                Ext.route.Router.clearLastTokens();
                // location.hash = '#' + newTab.itemId;
                Ext.History.add(newTab.itemId || '', true);
            }
        }
    },

    listen: {
        global: {
            unmatchedroute(token) {
                this.openXType(token);
            }
        }
    },

    onBeforeToken(a) {
        const token = location.hash;
        const tabs = this.getWorkspace();
        if (tabs) {
            const tab = tabs.down(token);
            if (tab) {
                tabs.setActiveTab(tab);
            } else {
                a.resume();
            }
        }
        localStorage.lastToken = token;
    },

    // openDoc(id) {
    //     this.getWorkspace().openTab({
    //         xclass: 'sdd.u4et.view.Doc',
    //         itemId: 'udoc-' + id
    //     });
    // },
    //
    // openTemplate(id) {
    //     this.getWorkspace().openTab({
    //         xclass: 'sdd.settings.templates.TemplatePanel',
    //         itemId: 'template-' + id
    //     });
    // },

    openXType(token) {
        const tabs = this.getWorkspace();
        tabs.openTab(token);
    }
});
