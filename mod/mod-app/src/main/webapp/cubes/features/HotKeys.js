/**
 * Ext.cubes.features.HotKeys
 *
 * @author mixam
 * @date 26/08/2019
 */
Ext.define('Ext.cubes.features.HotKeys', {

    singleton: true,
    map: {},

    constructor: function () {
        var w = Ext.getWin();
        w.on('keydown', this.onKeyDown, this, {priority: 1000, capture: true});
        // w.on('keyup', this.onKeyUp, this, {priority: -1000});

    },

    onKeyDown: function (e, dom, opts) {
        var me = this;
        Ext.each(this.map[this.key(e)], function (a) {
            // console.log('onKeyDown: ', arguments, this);
            if (me.checkVisible(a.actionTarget)) {
                var isSpecialKey = e.isSpecialKey();
                var tc = Ext.fly(e.target).component;
                if (tc && isSpecialKey) {
                    if (tc instanceof Ext.Button) return;
                    // Если это поле ввода пагинатора то отключаем обработку
                    if (tc.ownerCt instanceof Ext.toolbar.Paging) return;
                    // Не обрабатываем горячие клавиши есть у нас открыт пикер
                    if (tc instanceof Ext.form.field.ComboBox && !tc.picker.hidden) return;
                    // if (isSpecialKey && tc.picker && !tc.picker.hidden) return;

                    // if (isSpecialKey) {
                    //     // Обрабатываем случай когда курсор стоит в поле и нажимает Enter для фильтрации. В тот же момент у формы есть кнопка с Enter.
                    //     // Случай когда надо чтобы работала и фильрация и сохранение по Enter в форме
                    //     Ext.mixin.Observable.prototype.fireEventArgs.apply(tc, ['specialkey', [tc, event]]);
                    //     tc.fireEvent = function (name) {
                    //         if (name === 'specialkey') return;
                    //         Ext.mixin.Observable.prototype.fireEvent.apply(tc, arguments);
                    //     };
                    //
                    //     if (event.isStopped) {
                    //         return;
                    //     }
                    // }
                }

                a.execute(e);
                // if (e.getKey() >= 112 && e.getKey() <= 123)
                e.stopEvent();
            }
        });
    },
    // onKeyUp: function (e, dom, opts) {
    //     console.log('onKeyUp: ', arguments, this);
    // },

    key: function (e) {
        var key = '';
        if (e.ctrl || e.ctrlKey) key += 'Ctrl+';
        if (e.alt || e.altKey) key += 'Alt+';
        if (e.shift || e.shiftKey) key += 'Shit+';
        key += Ext.event.Event.keyCodes[e.keyCode];
        return key;
    },
    reg: function (hk, a) {
        // debugger
        if (!this.map[this.key(hk)]) this.map[this.key(hk)] = [];
        this.map[this.key(hk)].push(a);
    },
    checkVisible: function (cmp) {
        var result = true,
            activeWindow = Ext.WindowManager.getActiveExactWindow();

        if (!cmp.isVisible()) return false;

        if (cmp === activeWindow) {
            return true;
        }
        // Если компонент находится внутри окна то он виден только если окно видимо + оно должно быть активно
        var parentWindow = cmp.up('window') || false;
        if (parentWindow &&
            (!parentWindow.isVisible() || (activeWindow && parentWindow.id !== activeWindow.id))) {
            return false;
        }
        if (activeWindow && activeWindow.modal && parentWindow.id !== activeWindow.id) {
            return false;
        }
        // Если окно модальное активно - то можно совершать только действия кнопками внутри окна
        // if((aw = Ext.WindowManager.getActive()) && aw.modal && (pw = checkHiddenComponent.getParentWindow()) !== 1 && (!pw || (pw && pw.id !== aw.id))) {
        //     return true;
        // }

        cmp.ownerCt.bubble(function (parent) {
            if (parent.isHidden()) {
                result = false;
                return false; // останавливаем обход вверх, если уже не видно не идем еще выше
            }
        });
        return result;
    }
});

Ext.define('Ext.util.Hotkey', {
    constructor: function (c) {
        if (Ext.isString(c)) {
            c = {key: c}
        }
        Ext.apply(this, c);
        Ext.applyIf(this, {
            keyCode: Ext.event.Event[c.key.toUpperCase()]
        });
        this.mods = '';
        if (this.ctrl) this.mods += 'Ctrl+';
        if (this.alt) this.mods += 'Alt+';
        if (this.shift) this.mods += 'Shit+';

    },
    getTitle: function () {
        return this.mods + ({
            space: 'Пробел'
        }[this.key] || this.key);
    }
});

Ext.define('overrides.hotkeys.Action', {
    override: 'Ext.Action',

    constructor: function (c) {
        if (c.hotkey) {
            this.hotkey = new Ext.util.Hotkey(c.hotkey);
            Ext.cubes.features.HotKeys.reg(this.hotkey, this);
        }
        this.callParent(arguments);
        // var c = this.initialConfig;
    },

    execute: function (e) {
        // debugger;
        var a = this;
        var c = a.initialConfig;
        var at = a.actionTarget;
        if (!a.isDisabled()) {
            Ext.callback(c.handler, c.scope || at.resolveListenerScope(null, false), [at, e], 0, at);
            // this.callParent(arguments);
        }
    },

    addComponent: function (cmp) {
        var c = this.initialConfig, hk = this.hotkey;
        this.callParent(arguments);
        if (hk && c.text) {
            cmp.setText(c.text + ' (' + this.hotkey.getTitle() + ')');
        }
    },

    setText: function (text) {
        this.initialConfig.text = text;
        if (this.hotkey) {
            text += ' (' + this.hotkey.getTitle() + ')';
        }
        this.callEach('setText', [text]);
    }
});

Ext.define('overrides.hotkeys.Container', {
    override: 'Ext.container.Container',
    updateActions: function (actions) {
        this.callParent(arguments);
        for (var n in actions) {
            actions[n].actionTarget = this;
        }
    }
});

Ext.define('overrides.ZIndexManager', {
    override: 'Ext.ZIndexManager',

    getActiveExactWindow: function () {
        var win = false;
        Ext.WindowManager.eachTopDown(
            function (cmp) {
                if (cmp.is('window')) {
                    win = cmp;
                    return false;
                }
            });
        return win;
    }
});
