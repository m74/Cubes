Ext.define('overrides.form.field.Date', {
    override: 'Ext.form.field.Date',

    dateFormat: 'c',
    format: 'd.m.Y'
    // startDay: 1,
    // maskRe: /[\d\.]/,

    // enableKeyEvents: true,

    // minValue: new Date('1900-01-01')
});