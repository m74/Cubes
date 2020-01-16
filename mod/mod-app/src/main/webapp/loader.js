String.prototype.apply = function (values) {
    return this.replace(/{[_a-zA-Z][_a-zA-Z0-9.:*(" )]*}/g, function (key) {
        key = key.substring(1, key.length - 1);
        const arr = key.split(':');
        key = arr.shift();
        const fn = arr.join(':');
        let val = (values !== undefined && values[key] !== undefined) ? values[key] : '';
        if (fn) {
            const fname = fn.replace(/\(.*/, '');
            const args = (fn === fname) ? [] : eval('[' + fn.replace(/(.*\()|(\).*)/g, '') + ']');
            args.unshift(val);
            val = Ext.util.Format[fname].apply(Ext.util.Format, args);
        }
        return val != null ? val : '';
    });
};


const Loader = new function () {
    function load(c) {
        if (typeof (c) === 'string')
            c = {
                src: c
            };
        // c.src = c.src.apply(params);

        if (c.src.match('\.css$')) {
            document.write(`<link rel="stylesheet" type="text/css" href="${c.src}" />`);
            // const el = document.createElement("link");
            // el.setAttribute("href", str);
            // el.setAttribute("type", "text/css");
            // el.setAttribute("rel", "stylesheet");
            // document.getElementsByTagName("head")[0].appendChild(el);
        } else {
            document.write('<script');
            document.write(' type="');
            document.write(c.type || 'text/javascript');
            document.write('"');
            document.write(' charset="' + (c.charset || 'UTF-8') + '"');
            if (c.src) {
                document.write(' src="');
                document.write(c.src);
                document.write('"');
            }
            document.write('>');
            if (c.code)
                document.write(c.code);
            document.write('</script>');
        }
        // console.log("load: ", c.src);
    }

    this.load = function (arr) {
        for (let i = 0; i < arr.length; i++) {
            load(arr[i]);
        }
    };
};

const params = (function () {
    function getQ() {
        const script = document.getElementsByTagName("script");
        for (let i = 0; i < script.length; i++) {
            const src = script[i].src;
            if (src && src.match(/^.*loader.js.*$/))
                return src.split("?")[1];
        }
        return null;
    }

    function parseQ(query) {
        const params = {};
        const seg = query.replace(/^\?/, '').split('&');
        for (let i = 0; i < seg.length; i++) {
            if (!seg[i])
                continue;
            const s = seg[i].split('=');
            params[s[0]] = s[1] ? s[1] : true;
        }
        return params;
    }

    return Object.assign(parseQ(getQ()), parseQ(location.search));
})();

if (!localStorage.theme) localStorage.theme = 'Triton';

const
    suffix = params.debug ? '-debug' : '',
    theme = localStorage.theme.toLowerCase(),
    lang = params.lang || 'ru';
const dc = new Date().getTime();

Loader.load([
    `ext/ext-all${suffix}.js`,
    `ext/packages/ux/classic/ux${suffix}.js`,
    `ext/classic/theme-${theme}/theme-${theme}${suffix}.js`,
    `ext/classic/theme-${theme}/resources/theme-${theme}-all${suffix}.css`,
    `ext/packages/font-awesome/resources/font-awesome-all${suffix}.css`,
    `ext/classic/locale/locale-${lang}${suffix}.js`,
    `css/theme-${theme}/style.css`,
    `app.js?_dc=${dc}`
]);

