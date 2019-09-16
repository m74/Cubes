/**
 * Дополняет компонент:
 * <li>Проверкой на соответствие набору прав текущего пользователя.
 * Если компонент имеет атрибут roles и среди перечисленных в нем ролей
 * отсутствуют роли текущего пользователя, то компонент не будет помещен в контейнер.</li>
 * <li>Позволяет в конфиге указывать значение items функицией или строкой с именем функции.</li>
 */
Ext.define('overrides.Container', {
    override: 'Ext.Container',
    roles: [],

    onBeforeAdd: function (item) {
        if (!Ext.hasRoles(item.roles)) return false;
        this.callParent(arguments);
    },

    lookupComponent: function (c) {
        // если отсутствует действие (action)
        if (typeof c === 'string' && c[0] === '@' && !this.getAction(c.substring(1))) {
            return null;
        }
        // если компонент не доступен пользователю
        if (!Ext.hasRoles(c.roles)) {
            return null;
        }

        return this.callParent(arguments);
    },
    privates: {
        prepareItems: function (items, applyDefaults) {
            return Ext.Array.filter(this.callParent(arguments), function (el) {
                return el !== undefined && el !== null;
            });
        }
    },

    // @override
    initItems: function () {
        if (Ext.isFunction(this.items) || Ext.isString(this.items)) {
            // Позволяет в конфиге указывать значение items функицией или строкой с именем функции
            this.items = Ext.callback(this.items, this.scope || 'this', [], 0, this);
        }

        this.callParent(arguments);
    },

    // @override
    updateActions: function (actions) {
        // Убираем недоступные пользователю действия (actions)
        this.actions = {};
        for (var n in actions) {
            var a = actions[n];
            if (a && Ext.hasRoles(a.roles)) {
                this.actions[n] = a;
            }
        }
        this.callParent([this.actions]);
    }
});

Ext.hasRoles = function () {
    return true;
};


// Ext.define('overrides.cubes.Application', {
//     override: 'Ext.cubes.Application',
//     init: function () {
//         var me = this;
//         this.callParent(arguments);
//         this.on('login', function () {
//             me.shortcuts = me.shortcuts.filterBy(function (itm) {
//                 return Ext.hasRoles(itm.roles);
//             });
//         })
//     }
// });