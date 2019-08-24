String.prototype.apply = function (values) {
    return this.replace(/{[_a-zA-Z][_a-zA-Z0-9.:*(" )]*}/g, function (key) {
        key = key.substring(1, key.length - 1);
        var arr = key.split(':');
        key = arr.shift();
        var fn = arr.join(':');
        var val = (values !== undefined && values[key] !== undefined) ? values[key] : '';
        if (fn) {
            var fname = fn.replace(/\(.*/, '');
            var args = (fn === fname) ? [] : eval('[' + fn.replace(/(.*\()|(\).*)/g, '') + ']');
            args.unshift(val);
            val = Ext.util.Format[fname].apply(Ext.util.Format, args);
        }
        return val != null ? val : '';
    });
};


var Loader = new function () {
    function getQ() {
        var script = document.getElementsByTagName("script");
        for (var i = 0; i < script.length; i++) {
            var src = script[i].src;
            if (src && src.match(/^.*loader.js.*$/))
                return src.split("?")[1];
        }
        return null;
    }

    function parseQ(query) {
        var params = {};
        var seg = query.replace(/^\?/, '').split('&');
        for (var i = 0; i < seg.length; i++) {
            if (!seg[i])
                continue;
            var s = seg[i].split('=');
            params[s[0]] = s[1] ? s[1] : true;
        }
        return params;
    }

    var params =
        Object.assign(parseQ(getQ()), parseQ(location.search));
    if (params.debug) params.suffix = "-debug";

    function load(c) {
        if (typeof (c) === 'string')
            c = {
                src: c
            };
        c.src = c.src.apply(params);

        if (c.src.match('\.css$')) {
            document.write('<link rel="stylesheet" type="text/css" href="' + c.src + '" />');
            // var el = document.createElement("link");
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
        for (var i = 0; i < arr.length; i++) {
            load(arr[i]);
        }
    };
};

Loader.load([
    'ext/ext-all{suffix}.js',
    'ext/classic/theme-{theme}/theme-{theme}{suffix}.js',
    'ext/classic/theme-{theme}/resources/theme-{theme}-all{suffix}.css',
    'ext/packages/font-awesome/resources/font-awesome-all{suffix}.css',
    'ext/classic/locale/locale-{lang}{suffix}.js',
    'app.js'
]);

