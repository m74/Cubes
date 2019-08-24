/**
 */
Ext.define('overrides.Action', {
    override: 'Ext.Action',

    /**
     * Установить tooltip на связанных контролах. Предполагается, что в них есть метод setTooltip.
     * @param {String/Object} tooltip Описание см. в Ext.button.Button.setTooltip
     */
    setTooltip: function (tooltip) {
        this.callEach('setTooltip', [tooltip]);
    },

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