/**
 * overrides.Component
 *
 * @author mixam
 * @date 09/08/2019
 */
Ext.define('overrides.Component', {
    override: 'Ext.Component',

    setEnableFlags: function (flags) {
        for (var n in this.actions) {
            this.actions[n].setEnableFlags(flags);
        }
    },

    initComponent: function () {
        this.callParent(arguments);
        this.setEnableFlags(this.enableFlags);
    }
});
