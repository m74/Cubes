/**
 */
Ext.define('overrides.EnableFlags', {
    override: 'Ext.Action',

    setEnableFlags: function (flags) {
        var me = this;
        this.setDisabled((function () {
            var arr = me.initialConfig.enableOn;
            if (arr) {
                if (Ext.isString(arr)) arr = [arr];
                for (var i = 0; i < arr.length; i++) {
                    if (!flags || flags[arr[i]] !== true) {
                        return true;
                    }
                }
            }
            return false;
        })());
    }
});

Ext.define('overrides.enableFlags.Component', {
    override: 'Ext.Component',
    // enableFlags: {},

    setEnableFlags: function (flags) {
        this.enableFlags = Ext.apply(this.enableFlags || {}, flags);

        for (var n in this.actions) {
            this.actions[n].setEnableFlags(this.enableFlags);
        }
        if (this.ownerCt && this.ownerCt.setEnableFlags) {
            this.ownerCt.setEnableFlags(flags);
        }
    },

    initComponent: function () {
        this.callParent(arguments);
        if (this.actions) this.setEnableFlags(this.enableFlags || {});
    }
});

Ext.define('overrides.enableFlags.Table', {
    override: 'Ext.view.Table',
    applySelectionModel: function () {
        var grid = this.ownerGrid;
        var sm = this.callParent(arguments);
        sm.on('selectionchange', function (sm, recs) {
            grid.setEnableFlags({
                multiSelect: recs.length > 0,
                singleSelect: recs.length === 1
            });
        });
        return sm;
    }
});

Ext.define('overrides.enableFlags.FormPanel', {
    override: 'Ext.form.Panel',
    initComponent: function () {
        var me = this;
        this.callParent(arguments);
        this.on('validitychange', function (form, valid) {
            me.setEnableFlags({
                valid: valid
            });
        });
    }
});