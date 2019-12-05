Ext.define('overrides.form.field.Date', {
    override: 'Ext.form.field.Date',

    submitFormat: 'c',
    dateFormat: 'c',
    format: 'd.m.Y',
    startDay: 1,

    initValue: function (value) {
        this.callParent(arguments);
        // fix bag
        this.lastValue = this.rawValue
    },

    setValue: function (v) {
        this.callParent(arguments);
        this.value = this.rawDate;
    }
    // maskRe: /[\d\.]/,

    // enableKeyEvents: true,

    // minValue: new Date('1900-01-01')
});