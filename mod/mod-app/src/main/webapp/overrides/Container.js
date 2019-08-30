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
            return Ext.hasRoles(item.roles);
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


Ext.define('overrides.cubes.Application', {
    override: 'Ext.cubes.Application',
    init: function () {
        var me = this;
        this.callParent(arguments);
        this.on('login', function () {
            me.shortcuts = me.shortcuts.filterBy(function (itm) {
                return Ext.hasRoles(itm.roles);
            });
        })
    }
});