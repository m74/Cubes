Ext.define('overrides.tab.Tab', {
    override: 'Ext.tab.Tab',

    initComponent() {
        this.callParent(arguments)
        if (this.badgeText) this.setBadgeText(this.badgeText)
    },

    setBadgeText(text) {
        const empty = Ext.isEmpty(text);
        if (!empty) {
            if (!this.badgeEl) this.badgeEl = this.el.createChild({
                tag: 'div',
                style: {
                    'position': 'absolute',
                    'background-color': 'red',
                    'top': '4px',
                    'right': '2px',
                    'color': 'white',
                    'padding': '0 3px',
                    'font-size': '.76em',
                    'font-weight': '700',
                    'border-radius': '10px',
                    'cursor': 'default',
                    'line-height': '1.5em',
                    'min-width': '1.5em',
                    'text-align': 'center',
                }
            })
            this.badgeEl.setVisible(true)
            this.badgeEl.setText(text)
        } else {
            if (this.badgeEl) this.badgeEl.setVisible(false)
        }
    }
})
