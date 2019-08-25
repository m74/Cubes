/**
 * Дополняет компонент:
 * <li>Проверкой на соответствие набору прав текущего пользователя.
 * Если компонент имеет атрибут roles и среди перечисленных в нем ролей
 * отсутствуют роли текущего пользователя, то компонент не будет помещен в контейнер.</li>
 * <li>Позволяет в конфиге указывать значение items функицией или строкой с именем функции.</li>
 */
Ext.define('overrides.Container', {
    override: 'Ext.Container',

    initComponent: function () {
        this.on('beforeadd', function (cnt, item) {
            if (Ext.hasRoles(item.roles)) {
                var app = sdd.Application;

                Ext.each(item.stores, function (name) {
                    app.getStore(name);
                }, this);

                Ext.each(item.controllers, function (name) {
                    app.getController(name);
                }, this);

                return true;
            } else {
                console.log('skip: ', item.roles, item);
                return false;
            }
        }, this);
        this.callParent(arguments);
    },

    lookupComponent: function (c) {
        if (Ext.hasRoles(c.roles)) {
            // Не создаем компонент, если нет прав
            c = this.callParent(arguments);
        }
        return c;
    },

    // @override
    initItems: function () {
        if (Ext.isFunction(this.items) || Ext.isString(this.items)) {
            // Позволяет в конфиге указывать значение items функицией или строкой с именем функции
            this.items = Ext.callback(this.items, this.scope || 'this', [], 0, this);
        }

        this.callParent(arguments);
    }
});

Ext.hasRoles = function () {
    return true;
};
