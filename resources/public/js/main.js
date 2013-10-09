String.prototype.replaceAll = function (s1, s2) {
    return this.replace(new RegExp(s1, "gm"), s2);
};

function dropLast(s) {
    var coll = s.split("/");
    var len = coll.length;
    var prelast = coll[len - 2];
    var last = coll[len - 1].replace(".asciidoc", "");
    if (last === prelast) {
        coll[len - 1] = "";
    } else {
        coll[len - 1] = last;
    }
    return coll.join("/");
}

$(function () {
    $("a[href$='.asciidoc']").each(function (n, item) {
        var $item = $(item);
        var href = $item.attr("href");
        var text = $item.text();
        $item
            .text(dropLast(text))
            .attr("href", dropLast(text));
    });

    var $pre = $("pre > code");
    $pre.each(function (i, item) {
        var $item = $(item);
        var $pre = $("<pre/>");
        var wrap = $item.parent().parent();
        var lang = $item.attr("class");
        switch (lang.substring(0, lang.indexOf(" "))) {
            case "console":
                lang = "lang-basic";
                break;
            case "clojure":
                lang = "lang-clj";
                break;
        }
        $pre.html($item.html()).addClass(lang);
        wrap.html($pre);
    });
    $("code, pre").addClass("prettyprint");
    $("pre").addClass("linenums");
    prettyPrint();

    var pathname = document.location.pathname;
    var pn = pathname.replaceAll("-", "_");
    pn = pn.split("/");
    pn = pn.slice(1, pn.length - 1);
    if (pn.length > 0) {
        pn = pn.join(".");
        $("<blockquote/>").text(pn).prependTo("body");
    }

    var $nav = $("<div id='nav'/>");
    $("<a id='home' href='/'>首页</a>").appendTo($nav);
    var pre = pathname.split("/");
    pre = pre.slice(0, pre.length - 2);
    if (pre.length > 0) {
        pre = pre.join("/");
        $("<span>|</span>").appendTo($nav);
        $("<a>向上</a>").attr("href", pre + "/").appendTo($nav);
        $nav.prependTo("body");
        $nav.clone().appendTo("body");
    }
});