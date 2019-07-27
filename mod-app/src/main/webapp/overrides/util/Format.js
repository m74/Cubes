Ext.define('overrides.util.Format', {
    override: 'Ext.util.Format',
    array: function (arr, propertyName, delimiter) {
        delimiter = delimiter || ', ';

        var items = [];
        Ext.each(arr, function (item) {
            items.push(propertyName ? item[propertyName] : item)
        });

        return items.join(delimiter);
    }
});