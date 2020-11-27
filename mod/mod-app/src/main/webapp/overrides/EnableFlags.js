/**
 */
Ext.define('overrides.EnableFlags', {
    override: 'Ext.Action',

    setEnableFlags: function (flags) {
        this.setDisabled((() => {
            let arr = this.initialConfig.enableOn;
            if (arr) {
                if (Ext.isString(arr)) arr = [arr];
                for (let i = 0; i < arr.length; i++) {
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

        for (let n in this.actions) {
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

Ext.define('overrides.enableFlags.TablePanel', {
    override: 'Ext.panel.Table',

    setEnableFlagsFromRecords(recs) {
        this.setEnableFlags(this.createEnableFlags(recs));
    },

    setEnableFlagsFromSelectedRecords() {
        this.setEnableFlagsFromRecords(this.getSelection());
    },

    createEnableFlags(recs) {
        return {
            multiSelect: recs.length > 0,
            singleSelect: recs.length === 1
        };
    }
});

Ext.define('overrides.enableFlags.Table', {
    override: 'Ext.view.Table',
    applySelectionModel: function () {
        let sm = this.callParent(arguments);
        sm.on('selectionchange', (sm, recs) => this.ownerGrid.setEnableFlagsFromRecords(recs));
        return sm;
    }
});

Ext.define('overrides.enableFlags.FormPanel', {
    override: 'Ext.form.Panel',
    initComponent: function () {
        this.callParent(arguments);
        this.setEnableFlags({
            valid: this.form.isValid()
        });
        this.on('validitychange', (form, valid) => {
            this.setEnableFlags({
                valid: valid
            });
        });
    }
});