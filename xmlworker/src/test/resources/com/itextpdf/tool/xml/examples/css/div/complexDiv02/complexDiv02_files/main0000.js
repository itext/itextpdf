/* See W3C-specific code uncompressed at http://www.w3.org/2008/site/js/core.js */
/*
 * jQuery JavaScript Library v1.4.4
 * http://jquery.com/
 *
 * Copyright 2010, John Resig
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * http://jquery.org/license
 *
 * Includes Sizzle.js
 * http://sizzlejs.com/
 * Copyright 2010, The Dojo Foundation
 * Released under the MIT, BSD, and GPL Licenses.
 *
 * Date: Thu Nov 11 19:04:53 2010 -0500
 */
(function(AS,AW){function J(E,B,S){if(S===AW&&E.nodeType===1){S=E.getAttribute("data-"+B);
if(typeof S==="string"){try{S=S==="true"?true:S==="false"?false:S==="null"?null:!x.isNaN(S)?parseFloat(S):y.test(S)?x.parseJSON(S):S
}catch(P){}x.data(E,B,S)
}else{S=AW
}}return S
}function AI(){return false
}function q(){return true
}function Av(E,B,P){P[0].type=E;
return x.event.handle.apply(B,P)
}function j(Aa){var Z,Y,X,W,V,T,U,S,t,B,P,E=[];
W=[];
V=x.data(this,this.nodeType?"events":"__events__");
if(typeof V==="function"){V=V.events
}if(!(Aa.liveFired===this||!V||!V.live||Aa.button&&Aa.type==="click")){if(Aa.namespace){P=RegExp("(^|\\.)"+Aa.namespace.split(".").join("\\.(?:.*\\.)?")+"(\\.|$)")
}Aa.liveFired=this;
var c=V.live.slice(0);
for(U=0;
U<c.length;
U++){V=c[U];
V.origType.replace(AF,"")===Aa.type?W.push(V.selector):c.splice(U--,1)
}W=x(Aa.target).closest(W,Aa.currentTarget);
S=0;
for(t=W.length;
S<t;
S++){B=W[S];
for(U=0;
U<c.length;
U++){V=c[U];
if(B.selector===V.selector&&(!P||P.test(V.namespace))){T=B.elem;
X=null;
if(V.preType==="mouseenter"||V.preType==="mouseleave"){Aa.type=V.preType;
X=x(Aa.relatedTarget).closest(V.selector)[0]
}if(!X||X!==T){E.push({elem:T,handleObj:V,level:B.level})
}}}}S=0;
for(t=E.length;
S<t;
S++){W=E[S];
if(Y&&W.level>Y){break
}Aa.currentTarget=W.elem;
Aa.data=W.handleObj.data;
Aa.handleObj=W.handleObj;
P=W.handleObj.origHandler.apply(W.elem,arguments);
if(P===false||Aa.isPropagationStopped()){Y=W.level;
if(P===false){Z=false
}if(Aa.isImmediatePropagationStopped()){break
}}}return Z
}}function AE(E,B){return(E&&E!=="*"?E+".":"")+B.replace(N,"`").replace(Az,"&")
}function Ai(E,B,S){if(x.isFunction(B)){return x.grep(E,function(U,T){return !!B.call(U,T,U)===S
})
}else{if(B.nodeType){return x.grep(E,function(T){return T===B===S
})
}else{if(typeof B==="string"){var P=x.grep(E,function(T){return T.nodeType===1
});
if(An.test(B)){return x.filter(B,P,!S)
}else{B=x.filter(B,P)
}}}}return x.grep(E,function(T){return x.inArray(T,B)>=0===S
})
}function AT(E,B){var P=0;
B.each(function(){if(this.nodeName===(E[P]&&E[P].nodeName)){var V=x.data(E[P++]),U=x.data(this,V);
if(V=V&&V.events){delete U.handle;
U.events={};
for(var T in V){for(var S in V[T]){x.event.add(this,T,V[T][S],V[T][S].data)
}}}}})
}function AZ(E,B){B.src?x.ajax({url:B.src,async:false,dataType:"script"}):x.globalEval(B.text||B.textContent||B.innerHTML||"");
B.parentNode&&B.parentNode.removeChild(B)
}function w(E,B,S){var P=B==="width"?E.offsetWidth:E.offsetHeight;
if(S==="border"){return P
}x.each(B==="width"?AB:m,function(){S||(P-=parseFloat(x.css(E,"padding"+this))||0);
if(S==="margin"){P+=parseFloat(x.css(E,"margin"+this))||0
}else{P-=parseFloat(x.css(E,"border"+this+"Width"))||0
}});
return P
}function d(E,B,S,P){if(x.isArray(B)&&B.length){x.each(B,function(U,T){S||R.test(E)?P(E,T):d(E+"["+(typeof T==="object"||x.isArray(T)?U:"")+"]",T,S,P)
})
}else{if(!S&&B!=null&&typeof B==="object"){x.isEmptyObject(B)?P(E,""):x.each(B,function(U,T){d(E+"["+U+"]",T,S,P)
})
}else{P(E,B)
}}}function AK(E,B){var P={};
x.each(i.concat.apply([],i.slice(0,B)),function(){P[this]=E
});
return P
}function M(E){if(!H[E]){var B=x("<"+E+">").appendTo("body"),P=B.css("display");
B.remove();
if(P==="none"||P===""){P="block"
}H[E]=P
}return H[E]
}function As(B){return x.isWindow(B)?B:B.nodeType===9?B.defaultView||B.parentWindow:false
}var o=AS.document,x=function(){function BG(){if(!BF.isReady){try{o.documentElement.doScroll("left")
}catch(BH){setTimeout(BG,1);
return 
}BF.ready()
}}var BF=function(BH,BI){return new BF.fn.init(BH,BI)
},BE=AS.jQuery,BD=AS.$,BC,BA=/^(?:[^<]*(<[\w\W]+>)[^>]*$|#([\w\-]+)$)/,A7=/\S/,A8=/^\s+/,A4=/\s+$/,X=/\W/,A0=/\d/,A3=/^<(\w+)\s*\/?>(?:<\/\1>)?$/,Aa=/^[\],:{}\s]*$/,V=/\\(?:["\\\/bfnrt]|u[0-9a-fA-F]{4})/g,Y=/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g,W=/(?:^|:|,)(?:\s*\[)+/g,T=/(webkit)[ \/]([\w.]+)/,BB=/(opera)(?:.*version)?[ \/]([\w.]+)/,A9=/(msie) ([\w.]+)/,A5=/(mozilla)(?:.*? rv:([\w.]+))?/,A6=navigator.userAgent,A2=false,A1=[],c,U=Object.prototype.toString,Z=Object.prototype.hasOwnProperty,S=Array.prototype.push,P=Array.prototype.slice,E=String.prototype.trim,t=Array.prototype.indexOf,B={};
BF.fn=BF.prototype={init:function(BI,BK){var BH,BL,BJ;
if(!BI){return this
}if(BI.nodeType){this.context=this[0]=BI;
this.length=1;
return this
}if(BI==="body"&&!BK&&o.body){this.context=o;
this[0]=o.body;
this.selector="body";
this.length=1;
return this
}if(typeof BI==="string"){if((BH=BA.exec(BI))&&(BH[1]||!BK)){if(BH[1]){BJ=BK?BK.ownerDocument||BK:o;
if(BL=A3.exec(BI)){if(BF.isPlainObject(BK)){BI=[o.createElement(BL[1])];
BF.fn.attr.call(BI,BK,true)
}else{BI=[BJ.createElement(BL[1])]
}}else{BL=BF.buildFragment([BH[1]],[BJ]);
BI=(BL.cacheable?BL.fragment.cloneNode(true):BL.fragment).childNodes
}return BF.merge(this,BI)
}else{if((BL=o.getElementById(BH[2]))&&BL.parentNode){if(BL.id!==BH[2]){return BC.find(BI)
}this.length=1;
this[0]=BL
}this.context=o;
this.selector=BI;
return this
}}else{if(!BK&&!X.test(BI)){this.selector=BI;
this.context=o;
BI=o.getElementsByTagName(BI);
return BF.merge(this,BI)
}else{return !BK||BK.jquery?(BK||BC).find(BI):BF(BK).find(BI)
}}}else{if(BF.isFunction(BI)){return BC.ready(BI)
}}if(BI.selector!==AW){this.selector=BI.selector;
this.context=BI.context
}return BF.makeArray(BI,this)
},selector:"",jquery:"1.4.4",length:0,size:function(){return this.length
},toArray:function(){return P.call(this,0)
},get:function(BH){return BH==null?this.toArray():BH<0?this.slice(BH)[0]:this[BH]
},pushStack:function(BI,BJ,BH){var BK=BF();
BF.isArray(BI)?S.apply(BK,BI):BF.merge(BK,BI);
BK.prevObject=this;
BK.context=this.context;
if(BJ==="find"){BK.selector=this.selector+(this.selector?" ":"")+BH
}else{if(BJ){BK.selector=this.selector+"."+BJ+"("+BH+")"
}}return BK
},each:function(BH,BI){return BF.each(this,BH,BI)
},ready:function(BH){BF.bindReady();
if(BF.isReady){BH.call(o,BF)
}else{A1&&A1.push(BH)
}return this
},eq:function(BH){return BH===-1?this.slice(BH):this.slice(BH,+BH+1)
},first:function(){return this.eq(0)
},last:function(){return this.eq(-1)
},slice:function(){return this.pushStack(P.apply(this,arguments),"slice",P.call(arguments).join(","))
},map:function(BH){return this.pushStack(BF.map(this,function(BJ,BI){return BH.call(BJ,BI,BJ)
}))
},end:function(){return this.prevObject||BF(null)
},push:S,sort:[].sort,splice:[].splice};
BF.fn.init.prototype=BF.fn;
BF.extend=BF.fn.extend=function(){var BH,BP,BM,BJ,BL,BN=arguments[0]||{},BK=1,BI=arguments.length,BO=false;
if(typeof BN==="boolean"){BO=BN;
BN=arguments[1]||{};
BK=2
}if(typeof BN!=="object"&&!BF.isFunction(BN)){BN={}
}if(BI===BK){BN=this;
--BK
}for(;
BK<BI;
BK++){if((BH=arguments[BK])!=null){for(BP in BH){BM=BN[BP];
BJ=BH[BP];
if(BN!==BJ){if(BO&&BJ&&(BF.isPlainObject(BJ)||(BL=BF.isArray(BJ)))){if(BL){BL=false;
BM=BM&&BF.isArray(BM)?BM:[]
}else{BM=BM&&BF.isPlainObject(BM)?BM:{}
}BN[BP]=BF.extend(BO,BM,BJ)
}else{if(BJ!==AW){BN[BP]=BJ
}}}}}}return BN
};
BF.extend({noConflict:function(BH){AS.$=BD;
if(BH){AS.jQuery=BE
}return BF
},isReady:false,readyWait:1,ready:function(BI){BI===true&&BF.readyWait--;
if(!BF.readyWait||BI!==true&&!BF.isReady){if(!o.body){return setTimeout(BF.ready,1)
}BF.isReady=true;
if(!(BI!==true&&--BF.readyWait>0)){if(A1){var BJ=0,BH=A1;
for(A1=null;
BI=BH[BJ++];
){BI.call(o,BF)
}BF.fn.trigger&&BF(o).trigger("ready").unbind("ready")
}}}},bindReady:function(){if(!A2){A2=true;
if(o.readyState==="complete"){return setTimeout(BF.ready,1)
}if(o.addEventListener){o.addEventListener("DOMContentLoaded",c,false);
AS.addEventListener("load",BF.ready,false)
}else{if(o.attachEvent){o.attachEvent("onreadystatechange",c);
AS.attachEvent("onload",BF.ready);
var BH=false;
try{BH=AS.frameElement==null
}catch(BI){}o.documentElement.doScroll&&BH&&BG()
}}}},isFunction:function(BH){return BF.type(BH)==="function"
},isArray:Array.isArray||function(BH){return BF.type(BH)==="array"
},isWindow:function(BH){return BH&&typeof BH==="object"&&"setInterval" in BH
},isNaN:function(BH){return BH==null||!A0.test(BH)||isNaN(BH)
},type:function(BH){return BH==null?String(BH):B[U.call(BH)]||"object"
},isPlainObject:function(BH){if(!BH||BF.type(BH)!=="object"||BH.nodeType||BF.isWindow(BH)){return false
}if(BH.constructor&&!Z.call(BH,"constructor")&&!Z.call(BH.constructor.prototype,"isPrototypeOf")){return false
}for(var BI in BH){}return BI===AW||Z.call(BH,BI)
},isEmptyObject:function(BH){for(var BI in BH){return false
}return true
},error:function(BH){throw BH
},parseJSON:function(BH){if(typeof BH!=="string"||!BH){return null
}BH=BF.trim(BH);
if(Aa.test(BH.replace(V,"@").replace(Y,"]").replace(W,""))){return AS.JSON&&AS.JSON.parse?AS.JSON.parse(BH):(new Function("return "+BH))()
}else{BF.error("Invalid JSON: "+BH)
}},noop:function(){},globalEval:function(BI){if(BI&&A7.test(BI)){var BJ=o.getElementsByTagName("head")[0]||o.documentElement,BH=o.createElement("script");
BH.type="text/javascript";
if(BF.support.scriptEval){BH.appendChild(o.createTextNode(BI))
}else{BH.text=BI
}BJ.insertBefore(BH,BJ.firstChild);
BJ.removeChild(BH)
}},nodeName:function(BH,BI){return BH.nodeName&&BH.nodeName.toUpperCase()===BI.toUpperCase()
},each:function(BJ,BM,BI){var BN,BK=0,BL=BJ.length,BH=BL===AW||BF.isFunction(BJ);
if(BI){if(BH){for(BN in BJ){if(BM.apply(BJ[BN],BI)===false){break
}}}else{for(;
BK<BL;
){if(BM.apply(BJ[BK++],BI)===false){break
}}}}else{if(BH){for(BN in BJ){if(BM.call(BJ[BN],BN,BJ[BN])===false){break
}}}else{for(BI=BJ[0];
BK<BL&&BM.call(BI,BK,BI)!==false;
BI=BJ[++BK]){}}}return BJ
},trim:E?function(BH){return BH==null?"":E.call(BH)
}:function(BH){return BH==null?"":BH.toString().replace(A8,"").replace(A4,"")
},makeArray:function(BI,BJ){var BH=BJ||[];
if(BI!=null){var BK=BF.type(BI);
BI.length==null||BK==="string"||BK==="function"||BK==="regexp"||BF.isWindow(BI)?S.call(BH,BI):BF.merge(BH,BI)
}return BH
},inArray:function(BI,BJ){if(BJ.indexOf){return BJ.indexOf(BI)
}for(var BH=0,BK=BJ.length;
BH<BK;
BH++){if(BJ[BH]===BI){return BH
}}return -1
},merge:function(BI,BK){var BH=BI.length,BL=0;
if(typeof BK.length==="number"){for(var BJ=BK.length;
BL<BJ;
BL++){BI[BH++]=BK[BL]
}}else{for(;
BK[BL]!==AW;
){BI[BH++]=BK[BL++]
}}BI.length=BH;
return BI
},grep:function(BJ,BM,BI){var BN=[],BK;
BI=!!BI;
for(var BL=0,BH=BJ.length;
BL<BH;
BL++){BK=!!BM(BJ[BL],BL);
BI!==BK&&BN.push(BJ[BL])
}return BN
},map:function(BJ,BM,BI){for(var BN=[],BK,BL=0,BH=BJ.length;
BL<BH;
BL++){BK=BM(BJ[BL],BL,BI);
if(BK!=null){BN[BN.length]=BK
}}return BN.concat.apply([],BN)
},guid:1,proxy:function(BI,BJ,BH){if(arguments.length===2){if(typeof BJ==="string"){BH=BI;
BI=BH[BJ];
BJ=AW
}else{if(BJ&&!BF.isFunction(BJ)){BH=BJ;
BJ=AW
}}}if(!BJ&&BI){BJ=function(){return BI.apply(BH||this,arguments)
}
}if(BI){BJ.guid=BI.guid=BI.guid||BJ.guid||BF.guid++
}return BJ
},access:function(BJ,BM,BI,BO,BK,BL){var BH=BJ.length;
if(typeof BM==="object"){for(var BN in BM){BF.access(BJ,BN,BM[BN],BO,BK,BI)
}return BJ
}if(BI!==AW){BO=!BL&&BO&&BF.isFunction(BI);
for(BN=0;
BN<BH;
BN++){BK(BJ[BN],BM,BO?BI.call(BJ[BN],BN,BK(BJ[BN],BM)):BI,BL)
}return BJ
}return BH?BK(BJ[0],BM):AW
},now:function(){return(new Date).getTime()
},uaMatch:function(BH){BH=BH.toLowerCase();
BH=T.exec(BH)||BB.exec(BH)||A9.exec(BH)||BH.indexOf("compatible")<0&&A5.exec(BH)||[];
return{browser:BH[1]||"",version:BH[2]||"0"}
},browser:{}});
BF.each("Boolean Number String Function Array Date RegExp Object".split(" "),function(BH,BI){B["[object "+BI+"]"]=BI.toLowerCase()
});
A6=BF.uaMatch(A6);
if(A6.browser){BF.browser[A6.browser]=true;
BF.browser.version=A6.version
}if(BF.browser.webkit){BF.browser.safari=true
}if(t){BF.inArray=function(BH,BI){return t.call(BI,BH)
}
}if(!/\s/.test("\u00a0")){A8=/^[\s\xA0]+/;
A4=/[\s\xA0]+$/
}BC=BF(o);
if(o.addEventListener){c=function(){o.removeEventListener("DOMContentLoaded",c,false);
BF.ready()
}
}else{if(o.attachEvent){c=function(){if(o.readyState==="complete"){o.detachEvent("onreadystatechange",c);
BF.ready()
}}
}}return AS.jQuery=AS.$=BF
}();
(function(){x.support={};
var Z=o.documentElement,X=o.createElement("script"),W=o.createElement("div"),V="script"+x.now();
W.style.display="none";
W.innerHTML="   <link/><table></table><a href='/a' style='color:red;float:left;opacity:.55;'>a</a><input type='checkbox'/>";
var U=W.getElementsByTagName("*"),T=W.getElementsByTagName("a")[0],P=o.createElement("select"),S=P.appendChild(o.createElement("option"));
if(!(!U||!U.length||!T)){x.support={leadingWhitespace:W.firstChild.nodeType===3,tbody:!W.getElementsByTagName("tbody").length,htmlSerialize:!!W.getElementsByTagName("link").length,style:/red/.test(T.getAttribute("style")),hrefNormalized:T.getAttribute("href")==="/a",opacity:/^0.55$/.test(T.style.opacity),cssFloat:!!T.style.cssFloat,checkOn:W.getElementsByTagName("input")[0].value==="on",optSelected:S.selected,deleteExpando:true,optDisabled:false,checkClone:false,scriptEval:false,noCloneEvent:true,boxModel:null,inlineBlockNeedsLayout:false,shrinkWrapBlocks:false,reliableHiddenOffsets:true};
P.disabled=true;
x.support.optDisabled=!S.disabled;
X.type="text/javascript";
try{X.appendChild(o.createTextNode("window."+V+"=1;"))
}catch(E){}Z.insertBefore(X,Z.firstChild);
if(AS[V]){x.support.scriptEval=true;
delete AS[V]
}try{delete X.test
}catch(Y){x.support.deleteExpando=false
}Z.removeChild(X);
if(W.attachEvent&&W.fireEvent){W.attachEvent("onclick",function B(){x.support.noCloneEvent=false;
W.detachEvent("onclick",B)
});
W.cloneNode(true).fireEvent("onclick")
}W=o.createElement("div");
W.innerHTML="<input type='radio' name='radiotest' checked='checked'/>";
Z=o.createDocumentFragment();
Z.appendChild(W.firstChild);
x.support.checkClone=Z.cloneNode(true).cloneNode(true).lastChild.checked;
x(function(){var t=o.createElement("div");
t.style.width=t.style.paddingLeft="1px";
o.body.appendChild(t);
x.boxModel=x.support.boxModel=t.offsetWidth===2;
if("zoom" in t.style){t.style.display="inline";
t.style.zoom=1;
x.support.inlineBlockNeedsLayout=t.offsetWidth===2;
t.style.display="";
t.innerHTML="<div style='width:4px;'></div>";
x.support.shrinkWrapBlocks=t.offsetWidth!==2
}t.innerHTML="<table><tr><td style='padding:0;display:none'></td><td>t</td></tr></table>";
var c=t.getElementsByTagName("td");
x.support.reliableHiddenOffsets=c[0].offsetHeight===0;
c[0].style.display="";
c[1].style.display="none";
x.support.reliableHiddenOffsets=x.support.reliableHiddenOffsets&&c[0].offsetHeight===0;
t.innerHTML="";
o.body.removeChild(t).style.display="none"
});
Z=function(t){var c=o.createElement("div");
t="on"+t;
var Aa=t in c;
if(!Aa){c.setAttribute(t,"return;");
Aa=typeof c[t]==="function"
}return Aa
};
x.support.submitBubbles=Z("submit");
x.support.changeBubbles=Z("change");
Z=X=W=U=T=null
}})();
var Ay={},y=/^(?:\{.*\}|\[.*\])$/;
x.extend({cache:{},uuid:0,expando:"jQuery"+x.now(),noData:{embed:true,object:"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000",applet:true},data:function(E,B,U){if(x.acceptData(E)){E=E==AS?Ay:E;
var T=E.nodeType,S=T?E[x.expando]:null,P=x.cache;
if(!(T&&!S&&typeof B==="string"&&U===AW)){if(T){S||(E[x.expando]=S=++x.uuid)
}else{P=E
}if(typeof B==="object"){if(T){P[S]=x.extend(P[S],B)
}else{x.extend(P,B)
}}else{if(T&&!P[S]){P[S]={}
}}E=T?P[S]:P;
if(U!==AW){E[B]=U
}return typeof B==="string"?E[B]:E
}}},removeData:function(P,B){if(x.acceptData(P)){P=P==AS?Ay:P;
var V=P.nodeType,U=V?P[x.expando]:P,T=x.cache,S=V?T[U]:U;
if(B){if(S){delete S[B];
V&&x.isEmptyObject(S)&&x.removeData(P)
}}else{if(V&&x.support.deleteExpando){delete P[x.expando]
}else{if(P.removeAttribute){P.removeAttribute(x.expando)
}else{if(V){delete T[U]
}else{for(var E in P){delete P[E]
}}}}}}},acceptData:function(E){if(E.nodeName){var B=x.noData[E.nodeName.toLowerCase()];
if(B){return !(B===true||E.getAttribute("classid")!==B)
}}return true
}});
x.fn.extend({data:function(P,B){var W=null;
if(typeof P==="undefined"){if(this.length){var V=this[0].attributes,U;
W=x.data(this[0]);
for(var T=0,E=V.length;
T<E;
T++){U=V[T].name;
if(U.indexOf("data-")===0){U=U.substr(5);
J(this[0],U,W[U])
}}}return W
}else{if(typeof P==="object"){return this.each(function(){x.data(this,P)
})
}}var S=P.split(".");
S[1]=S[1]?"."+S[1]:"";
if(B===AW){W=this.triggerHandler("getData"+S[1]+"!",[S[0]]);
if(W===AW&&this.length){W=x.data(this[0],P);
W=J(this[0],P,W)
}return W===AW&&S[1]?this.data(S[0]):W
}else{return this.each(function(){var Y=x(this),X=[S[0],B];
Y.triggerHandler("setData"+S[1]+"!",X);
x.data(this,P,B);
Y.triggerHandler("changeData"+S[1]+"!",X)
})
}},removeData:function(B){return this.each(function(){x.removeData(this,B)
})
}});
x.extend({queue:function(E,B,S){if(E){B=(B||"fx")+"queue";
var P=x.data(E,B);
if(!S){return P||[]
}if(!P||x.isArray(S)){P=x.data(E,B,x.makeArray(S))
}else{P.push(S)
}return P
}},dequeue:function(E,B){B=B||"fx";
var S=x.queue(E,B),P=S.shift();
if(P==="inprogress"){P=S.shift()
}if(P){B==="fx"&&S.unshift("inprogress");
P.call(E,function(){x.dequeue(E,B)
})
}}});
x.fn.extend({queue:function(E,B){if(typeof E!=="string"){B=E;
E="fx"
}if(B===AW){return x.queue(this[0],E)
}return this.each(function(){var P=x.queue(this,E,B);
E==="fx"&&P[0]!=="inprogress"&&x.dequeue(this,E)
})
},dequeue:function(B){return this.each(function(){x.dequeue(this,B)
})
},delay:function(E,B){E=x.fx?x.fx.speeds[E]||E:E;
B=B||"fx";
return this.queue(B,function(){var P=this;
setTimeout(function(){x.dequeue(P,B)
},E)
})
},clearQueue:function(B){return this.queue(B||"fx",[])
}});
var Am=/[\n\t]/g,AP=/\s+/,D=/\r/g,Ap=/^(?:href|src|style)$/,Ac=/^(?:button|input)$/i,AD=/^(?:button|input|object|select|textarea)$/i,n=/^a(?:rea)?$/i,AY=/^(?:radio|checkbox)$/i;
x.props={"for":"htmlFor","class":"className",readonly:"readOnly",maxlength:"maxLength",cellspacing:"cellSpacing",rowspan:"rowSpan",colspan:"colSpan",tabindex:"tabIndex",usemap:"useMap",frameborder:"frameBorder"};
x.fn.extend({attr:function(E,B){return x.access(this,E,B,true,x.attr)
},removeAttr:function(B){return this.each(function(){x.attr(this,B,"");
this.nodeType===1&&this.removeAttribute(B)
})
},addClass:function(X){if(x.isFunction(X)){return this.each(function(Y){var Z=x(this);
Z.addClass(X.call(this,Y,Z.attr("class")))
})
}if(X&&typeof X==="string"){for(var W=(X||"").split(AP),V=0,U=this.length;
V<U;
V++){var T=this[V];
if(T.nodeType===1){if(T.className){for(var S=" "+T.className+" ",E=T.className,P=0,B=W.length;
P<B;
P++){if(S.indexOf(" "+W[P]+" ")<0){E+=" "+W[P]
}}T.className=x.trim(E)
}else{T.className=X
}}}}return this
},removeClass:function(P){if(x.isFunction(P)){return this.each(function(Y){var X=x(this);
X.removeClass(P.call(this,Y,X.attr("class")))
})
}if(P&&typeof P==="string"||P===AW){for(var B=(P||"").split(AP),W=0,V=this.length;
W<V;
W++){var U=this[W];
if(U.nodeType===1&&U.className){if(P){for(var T=(" "+U.className+" ").replace(Am," "),E=0,S=B.length;
E<S;
E++){T=T.replace(" "+B[E]+" "," ")
}U.className=x.trim(T)
}else{U.className=""
}}}}return this
},toggleClass:function(E,B){var S=typeof E,P=typeof B==="boolean";
if(x.isFunction(E)){return this.each(function(U){var T=x(this);
T.toggleClass(E.call(this,U,T.attr("class"),B),B)
})
}return this.each(function(){if(S==="string"){for(var W,V=0,T=x(this),U=B,X=E.split(AP);
W=X[V++];
){U=P?U:!T.hasClass(W);
T[U?"addClass":"removeClass"](W)
}}else{if(S==="undefined"||S==="boolean"){this.className&&x.data(this,"__className__",this.className);
this.className=this.className||E===false?"":x.data(this,"__className__")||""
}}})
},hasClass:function(E){E=" "+E+" ";
for(var B=0,P=this.length;
B<P;
B++){if((" "+this[B].className+" ").replace(Am," ").indexOf(E)>-1){return true
}}return false
},val:function(P){if(!arguments.length){var B=this[0];
if(B){if(x.nodeName(B,"option")){var W=B.attributes.value;
return !W||W.specified?B.value:B.text
}if(x.nodeName(B,"select")){var V=B.selectedIndex;
W=[];
var U=B.options;
B=B.type==="select-one";
if(V<0){return null
}var T=B?V:0;
for(V=B?V+1:U.length;
T<V;
T++){var E=U[T];
if(E.selected&&(x.support.optDisabled?!E.disabled:E.getAttribute("disabled")===null)&&(!E.parentNode.disabled||!x.nodeName(E.parentNode,"optgroup"))){P=x(E).val();
if(B){return P
}W.push(P)
}}return W
}if(AY.test(B.type)&&!x.support.checkOn){return B.getAttribute("value")===null?"on":B.value
}return(B.value||"").replace(D,"")
}return AW
}var S=x.isFunction(P);
return this.each(function(c){var Y=x(this),Z=P;
if(this.nodeType===1){if(S){Z=P.call(this,c,Y.val())
}if(Z==null){Z=""
}else{if(typeof Z==="number"){Z+=""
}else{if(x.isArray(Z)){Z=x.map(Z,function(t){return t==null?"":t+""
})
}}}if(x.isArray(Z)&&AY.test(this.type)){this.checked=x.inArray(Y.val(),Z)>=0
}else{if(x.nodeName(this,"select")){var X=x.makeArray(Z);
x("option",this).each(function(){this.selected=x.inArray(x(this).val(),X)>=0
});
if(!X.length){this.selectedIndex=-1
}}else{this.value=Z
}}}})
}});
x.extend({attrFn:{val:true,css:true,html:true,text:true,data:true,width:true,height:true,offset:true},attr:function(E,B,U,T){if(!E||E.nodeType===3||E.nodeType===8){return AW
}if(T&&B in x.attrFn){return x(E)[B](U)
}T=E.nodeType!==1||!x.isXMLDoc(E);
var S=U!==AW;
B=T&&x.props[B]||B;
var P=Ap.test(B);
if((B in E||E[B]!==AW)&&T&&!P){if(S){B==="type"&&Ac.test(E.nodeName)&&E.parentNode&&x.error("type property can't be changed");
if(U===null){E.nodeType===1&&E.removeAttribute(B)
}else{E[B]=U
}}if(x.nodeName(E,"form")&&E.getAttributeNode(B)){return E.getAttributeNode(B).nodeValue
}if(B==="tabIndex"){return(B=E.getAttributeNode("tabIndex"))&&B.specified?B.value:AD.test(E.nodeName)||n.test(E.nodeName)&&E.href?0:AW
}return E[B]
}if(!x.support.style&&T&&B==="style"){if(S){E.style.cssText=""+U
}return E.style.cssText
}S&&E.setAttribute(B,""+U);
if(!E.attributes[B]&&E.hasAttribute&&!E.hasAttribute(B)){return AW
}E=!x.support.hrefNormalized&&T&&P?E.getAttribute(B,2):E.getAttribute(B);
return E===null?AW:E
}});
var AF=/\.(.*)$/,s=/^(?:textarea|input|select)$/i,N=/\./g,Az=/ /g,a=/[^\w\s.|`]/g,F=function(B){return B.replace(a,"\\$&")
},AA={focusin:0,focusout:0};
x.event={add:function(t,Z,Y,X){if(!(t.nodeType===3||t.nodeType===8)){if(x.isWindow(t)&&t!==AS&&!t.frameElement){t=AS
}if(Y===false){Y=AI
}else{if(!Y){return 
}}var W,V;
if(Y.handler){W=Y;
Y=W.handler
}if(!Y.guid){Y.guid=x.guid++
}if(V=x.data(t)){var T=t.nodeType?"events":"__events__",U=V[T],S=V.handle;
if(typeof U==="function"){S=U.handle;
U=U.events
}else{if(!U){t.nodeType||(V[T]=V=function(){});
V.events=U={}
}}if(!S){V.handle=S=function(){return typeof x!=="undefined"&&!x.event.triggered?x.event.handle.apply(S.elem,arguments):AW
}
}S.elem=t;
Z=Z.split(" ");
for(var c=0,B;
T=Z[c++];
){V=W?x.extend({},W):{handler:Y,data:X};
if(T.indexOf(".")>-1){B=T.split(".");
T=B.shift();
V.namespace=B.slice(0).sort().join(".")
}else{B=[];
V.namespace=""
}V.type=T;
if(!V.guid){V.guid=Y.guid
}var P=U[T],E=x.event.special[T]||{};
if(!P){P=U[T]=[];
if(!E.setup||E.setup.call(t,X,B,S)===false){if(t.addEventListener){t.addEventListener(T,S,false)
}else{t.attachEvent&&t.attachEvent("on"+T,S)
}}}if(E.add){E.add.call(t,V);
if(!V.handler.guid){V.handler.guid=Y.guid
}}P.push(V);
x.event.global[T]=true
}t=null
}}},global:{},remove:function(Aa,Z,Y,X){if(!(Aa.nodeType===3||Aa.nodeType===8)){if(Y===false){Y=AI
}var W,V,T=0,U,S,t,B,P,E,c=Aa.nodeType?"events":"__events__",A1=x.data(Aa),A0=A1&&A1[c];
if(A1&&A0){if(typeof A0==="function"){A1=A0;
A0=A0.events
}if(Z&&Z.type){Y=Z.handler;
Z=Z.type
}if(!Z||typeof Z==="string"&&Z.charAt(0)==="."){Z=Z||"";
for(W in A0){x.event.remove(Aa,W+Z)
}}else{for(Z=Z.split(" ");
W=Z[T++];
){B=W;
U=W.indexOf(".")<0;
S=[];
if(!U){S=W.split(".");
W=S.shift();
t=RegExp("(^|\\.)"+x.map(S.slice(0).sort(),F).join("\\.(?:.*\\.)?")+"(\\.|$)")
}if(P=A0[W]){if(Y){B=x.event.special[W]||{};
for(V=X||0;
V<P.length;
V++){E=P[V];
if(Y.guid===E.guid){if(U||t.test(E.namespace)){X==null&&P.splice(V--,1);
B.remove&&B.remove.call(Aa,E)
}if(X!=null){break
}}}if(P.length===0||X!=null&&P.length===1){if(!B.teardown||B.teardown.call(Aa,S)===false){x.removeEvent(Aa,W,A1.handle)
}delete A0[W]
}}else{for(V=0;
V<P.length;
V++){E=P[V];
if(U||t.test(E.namespace)){x.event.remove(Aa,B,E.handler,V);
P.splice(V--,1)
}}}}}if(x.isEmptyObject(A0)){if(Z=A1.handle){Z.elem=null
}delete A1.events;
delete A1.handle;
if(typeof A1==="function"){x.removeData(Aa,c)
}else{x.isEmptyObject(A1)&&x.removeData(Aa)
}}}}}},trigger:function(Z,X,W,V){var U=Z.type||Z;
if(!V){Z=typeof Z==="object"?Z[x.expando]?Z:x.extend(x.Event(U),Z):x.Event(U);
if(U.indexOf("!")>=0){Z.type=U=U.slice(0,-1);
Z.exclusive=true
}if(!W){Z.stopPropagation();
x.event.global[U]&&x.each(x.cache,function(){this.events&&this.events[U]&&x.event.trigger(Z,X,this.handle.elem)
})
}if(!W||W.nodeType===3||W.nodeType===8){return AW
}Z.result=AW;
Z.target=W;
X=x.makeArray(X);
X.unshift(Z)
}Z.currentTarget=W;
(V=W.nodeType?x.data(W,"handle"):(x.data(W,"__events__")||{}).handle)&&V.apply(W,X);
V=W.parentNode||W.ownerDocument;
try{if(!(W&&W.nodeName&&x.noData[W.nodeName.toLowerCase()])){if(W["on"+U]&&W["on"+U].apply(W,X)===false){Z.result=false;
Z.preventDefault()
}}}catch(T){}if(!Z.isPropagationStopped()&&V){x.event.trigger(Z,X,V,true)
}else{if(!Z.isDefaultPrevented()){var P;
V=Z.target;
var S=U.replace(AF,""),E=x.nodeName(V,"a")&&S==="click",Y=x.event.special[S]||{};
if((!Y._default||Y._default.call(W,Z)===false)&&!E&&!(V&&V.nodeName&&x.noData[V.nodeName.toLowerCase()])){try{if(V[S]){if(P=V["on"+S]){V["on"+S]=null
}x.event.triggered=true;
V[S]()
}}catch(B){}if(P){V["on"+S]=P
}x.event.triggered=false
}}}},handle:function(P){var B,W,V,U;
W=[];
var T=x.makeArray(arguments);
P=T[0]=x.event.fix(P||AS.event);
P.currentTarget=this;
B=P.type.indexOf(".")<0&&!P.exclusive;
if(!B){V=P.type.split(".");
P.type=V.shift();
W=V.slice(0).sort();
V=RegExp("(^|\\.)"+W.join("\\.(?:.*\\.)?")+"(\\.|$)")
}P.namespace=P.namespace||W.join(".");
U=x.data(this,this.nodeType?"events":"__events__");
if(typeof U==="function"){U=U.events
}W=(U||{})[P.type];
if(U&&W){W=W.slice(0);
U=0;
for(var E=W.length;
U<E;
U++){var S=W[U];
if(B||V.test(S.namespace)){P.handler=S.handler;
P.data=S.data;
P.handleObj=S;
S=S.handler.apply(this,T);
if(S!==AW){P.result=S;
if(S===false){P.preventDefault();
P.stopPropagation()
}}if(P.isImmediatePropagationStopped()){break
}}}}return P.result
},props:"altKey attrChange attrName bubbles button cancelable charCode clientX clientY ctrlKey currentTarget data detail eventPhase fromElement handler keyCode layerX layerY metaKey newValue offsetX offsetY pageX pageY prevValue relatedNode relatedTarget screenX screenY shiftKey srcElement target toElement view wheelDelta which".split(" "),fix:function(E){if(E[x.expando]){return E
}var B=E;
E=x.Event(B);
for(var S=this.props.length,P;
S;
){P=this.props[--S];
E[P]=B[P]
}if(!E.target){E.target=E.srcElement||o
}if(E.target.nodeType===3){E.target=E.target.parentNode
}if(!E.relatedTarget&&E.fromElement){E.relatedTarget=E.fromElement===E.target?E.toElement:E.fromElement
}if(E.pageX==null&&E.clientX!=null){B=o.documentElement;
S=o.body;
E.pageX=E.clientX+(B&&B.scrollLeft||S&&S.scrollLeft||0)-(B&&B.clientLeft||S&&S.clientLeft||0);
E.pageY=E.clientY+(B&&B.scrollTop||S&&S.scrollTop||0)-(B&&B.clientTop||S&&S.clientTop||0)
}if(E.which==null&&(E.charCode!=null||E.keyCode!=null)){E.which=E.charCode!=null?E.charCode:E.keyCode
}if(!E.metaKey&&E.ctrlKey){E.metaKey=E.ctrlKey
}if(!E.which&&E.button!==AW){E.which=E.button&1?1:E.button&2?3:E.button&4?2:0
}return E
},guid:100000000,proxy:x.proxy,special:{ready:{setup:x.bindReady,teardown:x.noop},live:{add:function(B){x.event.add(this,AE(B.origType,B.selector),x.extend({},B,{handler:j,guid:B.handler.guid}))
},remove:function(B){x.event.remove(this,AE(B.origType,B.selector),B)
}},beforeunload:{setup:function(E,B,P){if(x.isWindow(this)){this.onbeforeunload=P
}},teardown:function(E,B){if(this.onbeforeunload===B){this.onbeforeunload=null
}}}}};
x.removeEvent=o.removeEventListener?function(E,B,P){E.removeEventListener&&E.removeEventListener(B,P,false)
}:function(E,B,P){E.detachEvent&&E.detachEvent("on"+B,P)
};
x.Event=function(B){if(!this.preventDefault){return new x.Event(B)
}if(B&&B.type){this.originalEvent=B;
this.type=B.type
}else{this.type=B
}this.timeStamp=x.now();
this[x.expando]=true
};
x.Event.prototype={preventDefault:function(){this.isDefaultPrevented=q;
var B=this.originalEvent;
if(B){if(B.preventDefault){B.preventDefault()
}else{B.returnValue=false
}}},stopPropagation:function(){this.isPropagationStopped=q;
var B=this.originalEvent;
if(B){B.stopPropagation&&B.stopPropagation();
B.cancelBubble=true
}},stopImmediatePropagation:function(){this.isImmediatePropagationStopped=q;
this.stopPropagation()
},isDefaultPrevented:AI,isPropagationStopped:AI,isImmediatePropagationStopped:AI};
var l=function(E){var B=E.relatedTarget;
try{for(;
B&&B!==this;
){B=B.parentNode
}if(B!==this){E.type=E.data;
x.event.handle.apply(this,arguments)
}}catch(P){}},Q=function(B){B.type=B.data;
x.event.handle.apply(this,arguments)
};
x.each({mouseenter:"mouseover",mouseleave:"mouseout"},function(E,B){x.event.special[E]={setup:function(P){x.event.add(this,B,P&&P.selector?Q:l,E)
},teardown:function(P){x.event.remove(this,B,P&&P.selector?Q:l)
}}
});
if(!x.support.submitBubbles){x.event.special.submit={setup:function(){if(this.nodeName.toLowerCase()!=="form"){x.event.add(this,"click.specialSubmit",function(E){var B=E.target,P=B.type;
if((P==="submit"||P==="image")&&x(B).closest("form").length){E.liveFired=AW;
return Av("submit",this,arguments)
}});
x.event.add(this,"keypress.specialSubmit",function(E){var B=E.target,P=B.type;
if((P==="text"||P==="password")&&x(B).closest("form").length&&E.keyCode===13){E.liveFired=AW;
return Av("submit",this,arguments)
}})
}else{return false
}},teardown:function(){x.event.remove(this,".specialSubmit")
}}
}if(!x.support.changeBubbles){var AH,C=function(E){var B=E.type,P=E.value;
if(B==="radio"||B==="checkbox"){P=E.checked
}else{if(B==="select-multiple"){P=E.selectedIndex>-1?x.map(E.options,function(S){return S.selected
}).join("-"):""
}else{if(E.nodeName.toLowerCase()==="select"){P=E.selectedIndex
}}}return P
},AC=function(E,B){var T=E.target,S,P;
if(!(!s.test(T.nodeName)||T.readOnly)){S=x.data(T,"_change_data");
P=C(T);
if(E.type!=="focusout"||T.type!=="radio"){x.data(T,"_change_data",P)
}if(!(S===AW||P===S)){if(S!=null||P){E.type="change";
E.liveFired=AW;
return x.event.trigger(E,B,T)
}}}};
x.event.special.change={filters:{focusout:AC,beforedeactivate:AC,click:function(E){var B=E.target,P=B.type;
if(P==="radio"||P==="checkbox"||B.nodeName.toLowerCase()==="select"){return AC.call(this,E)
}},keydown:function(E){var B=E.target,P=B.type;
if(E.keyCode===13&&B.nodeName.toLowerCase()!=="textarea"||E.keyCode===32&&(P==="checkbox"||P==="radio")||P==="select-multiple"){return AC.call(this,E)
}},beforeactivate:function(B){B=B.target;
x.data(B,"_change_data",C(B))
}},setup:function(){if(this.type==="file"){return false
}for(var B in AH){x.event.add(this,B+".specialChange",AH[B])
}return s.test(this.nodeName)
},teardown:function(){x.event.remove(this,".specialChange");
return s.test(this.nodeName)
}};
AH=x.event.special.change.filters;
AH.focus=AH.beforeactivate
}o.addEventListener&&x.each({focus:"focusin",blur:"focusout"},function(E,B){function P(S){S=x.event.fix(S);
S.type=B;
return x.event.trigger(S,null,S.target)
}x.event.special[B]={setup:function(){AA[B]++===0&&o.addEventListener(E,P,true)
},teardown:function(){--AA[B]===0&&o.removeEventListener(E,P,true)
}}
});
x.each(["bind","one"],function(E,B){x.fn[B]=function(W,V,U){if(typeof W==="object"){for(var T in W){this[B](T,V,W[T],U)
}return this
}if(x.isFunction(V)||V===false){U=V;
V=AW
}var P=B==="one"?x.proxy(U,function(X){x(this).unbind(X,P);
return U.apply(this,arguments)
}):U;
if(W==="unload"&&B!=="one"){this.one(W,V,U)
}else{T=0;
for(var S=this.length;
T<S;
T++){x.event.add(this[T],W,P,V)
}}return this
}
});
x.fn.extend({unbind:function(E,B){if(typeof E==="object"&&!E.preventDefault){for(var S in E){this.unbind(S,E[S])
}}else{S=0;
for(var P=this.length;
S<P;
S++){x.event.remove(this[S],E,B)
}}return this
},delegate:function(E,B,S,P){return this.live(B,S,P,E)
},undelegate:function(E,B,P){return arguments.length===0?this.unbind("live"):this.die(B,null,P,E)
},trigger:function(E,B){return this.each(function(){x.event.trigger(E,B,this)
})
},triggerHandler:function(E,B){if(this[0]){var P=x.Event(E);
P.preventDefault();
P.stopPropagation();
x.event.trigger(P,B,this[0]);
return P.result
}},toggle:function(E){for(var B=arguments,P=1;
P<B.length;
){x.proxy(E,B[P++])
}return this.click(x.proxy(E,function(T){var S=(x.data(this,"lastToggle"+E.guid)||0)%P;
x.data(this,"lastToggle"+E.guid,S+1);
T.preventDefault();
return B[S].apply(this,arguments)||false
}))
},hover:function(E,B){return this.mouseenter(E).mouseleave(B||E)
}});
var Ao={focus:"focusin",blur:"focusout",mouseenter:"mouseover",mouseleave:"mouseout"};
x.each(["live","die"],function(E,B){x.fn[B]=function(Z,Y,X,W){var U,V=0,T,c,P=W||this.selector;
W=W?this:x(this.context);
if(typeof Z==="object"&&!Z.preventDefault){for(U in Z){W[B](U,Y,Z[U],P)
}return this
}if(x.isFunction(Y)){X=Y;
Y=AW
}for(Z=(Z||"").split(" ");
(U=Z[V++])!=null;
){T=AF.exec(U);
c="";
if(T){c=T[0];
U=U.replace(AF,"")
}if(U==="hover"){Z.push("mouseenter"+c,"mouseleave"+c)
}else{T=U;
if(U==="focus"||U==="blur"){Z.push(Ao[U]+c);
U+=c
}else{U=(Ao[U]||U)+c
}if(B==="live"){c=0;
for(var S=W.length;
c<S;
c++){x.event.add(W[c],"live."+AE(U,P),{data:Y,selector:P,handler:X,origType:U,origHandler:X,preType:T})
}}else{W.unbind("live."+AE(U,P),X)
}}}return this
}
});
x.each("blur focus focusin focusout load resize scroll unload click dblclick mousedown mouseup mousemove mouseover mouseout mouseenter mouseleave change select submit keydown keypress keyup error".split(" "),function(E,B){x.fn[B]=function(S,P){if(P==null){P=S;
S=null
}return arguments.length>0?this.bind(B,S,P):this.trigger(B)
};
if(x.attrFn){x.attrFn[B]=true
}});
AS.attachEvent&&!AS.addEventListener&&x(AS).bind("unload",function(){for(var E in x.cache){if(x.cache[E].handle){try{x.event.remove(x.cache[E].handle.elem)
}catch(B){}}}});
(function(){function A0(A8,A7,A5,A6,A4,A3){A4=0;
for(var BA=A6.length;
A4<BA;
A4++){var A9=A6[A4];
if(A9){var BB=false;
for(A9=A9[A8];
A9;
){if(A9.sizcache===A5){BB=A6[A9.sizset];
break
}if(A9.nodeType===1&&!A3){A9.sizcache=A5;
A9.sizset=A4
}if(A9.nodeName.toLowerCase()===A7){BB=A9;
break
}A9=A9[A8]
}A6[A4]=BB
}}}function c(A8,A7,A5,A6,A4,A3){A4=0;
for(var BA=A6.length;
A4<BA;
A4++){var A9=A6[A4];
if(A9){var BB=false;
for(A9=A9[A8];
A9;
){if(A9.sizcache===A5){BB=A6[A9.sizset];
break
}if(A9.nodeType===1){if(!A3){A9.sizcache=A5;
A9.sizset=A4
}if(typeof A7!=="string"){if(A9===A7){BB=true;
break
}}else{if(U.filter(A7,[A9]).length>0){BB=A9;
break
}}}A9=A9[A8]
}A6[A4]=BB
}}}var Y=/((?:\((?:\([^()]+\)|[^()]+)+\)|\[(?:\[[^\[\]]*\]|['"][^'"]*['"]|[^\[\]'"]+)+\]|\\.|[^ >+~,(\[\\]+)+|[>+~])(\s*,\s*)?((?:.|\r|\n)*)/g,X=0,W=Object.prototype.toString,V=false,T=true;
[0,0].sort(function(){T=false;
return 0
});
var U=function(BA,A9,A6,A7){A6=A6||[];
var A5=A9=A9||o;
if(A9.nodeType!==1&&A9.nodeType!==9){return[]
}if(!BA||typeof BA!=="string"){return A6
}var A4,BF,BE,BG,BD,BC=true,BB=U.isXML(A9),A3=[],A8=BA;
do{Y.exec("");
if(A4=Y.exec(A8)){A8=A4[3];
A3.push(A4[1]);
if(A4[2]){BG=A4[3];
break
}}}while(A4);
if(A3.length>1&&Aa.exec(BA)){if(A3.length===2&&S.relative[A3[0]]){BF=Z(A3[0]+A3[1],A9)
}else{for(BF=S.relative[A3[0]]?[A9]:U(A3.shift(),A9);
A3.length;
){BA=A3.shift();
if(S.relative[BA]){BA+=A3.shift()
}BF=Z(BA,BF)
}}}else{if(!A7&&A3.length>1&&A9.nodeType===9&&!BB&&S.match.ID.test(A3[0])&&!S.match.ID.test(A3[A3.length-1])){A4=U.find(A3.shift(),A9,BB);
A9=A4.expr?U.filter(A4.expr,A4.set)[0]:A4.set[0]
}if(A9){A4=A7?{expr:A3.pop(),set:E(A7)}:U.find(A3.pop(),A3.length===1&&(A3[0]==="~"||A3[0]==="+")&&A9.parentNode?A9.parentNode:A9,BB);
BF=A4.expr?U.filter(A4.expr,A4.set):A4.set;
if(A3.length>0){BE=E(BF)
}else{BC=false
}for(;
A3.length;
){A4=BD=A3.pop();
if(S.relative[BD]){A4=A3.pop()
}else{BD=""
}if(A4==null){A4=A9
}S.relative[BD](BE,A4,BB)
}}else{BE=[]
}}BE||(BE=BF);
BE||U.error(BD||BA);
if(W.call(BE)==="[object Array]"){if(BC){if(A9&&A9.nodeType===1){for(BA=0;
BE[BA]!=null;
BA++){if(BE[BA]&&(BE[BA]===true||BE[BA].nodeType===1&&U.contains(A9,BE[BA]))){A6.push(BF[BA])
}}}else{for(BA=0;
BE[BA]!=null;
BA++){BE[BA]&&BE[BA].nodeType===1&&A6.push(BF[BA])
}}}else{A6.push.apply(A6,BE)
}}else{E(BE,A6)
}if(BG){U(BG,A5,A6,A7);
U.uniqueSort(A6)
}return A6
};
U.uniqueSort=function(A4){if(A2){V=T;
A4.sort(A2);
if(V){for(var A3=1;
A3<A4.length;
A3++){A4[A3]===A4[A3-1]&&A4.splice(A3--,1)
}}}return A4
};
U.matches=function(A4,A3){return U(A4,null,null,A3)
};
U.matchesSelector=function(A4,A3){return U(A3,null,null,[A4]).length>0
};
U.find=function(A8,A7,A5){var A6;
if(!A8){return[]
}for(var A4=0,A3=S.order.length;
A4<A3;
A4++){var BA,A9=S.order[A4];
if(BA=S.leftMatch[A9].exec(A8)){var BB=BA[1];
BA.splice(1,1);
if(BB.substr(BB.length-1)!=="\\"){BA[1]=(BA[1]||"").replace(/\\/g,"");
A6=S.find[A9](BA,A7,A5);
if(A6!=null){A8=A8.replace(S.match[A9],"");
break
}}}}A6||(A6=A7.getElementsByTagName("*"));
return{set:A6,expr:A8}
};
U.filter=function(BB,BA,A6,A7){for(var A5,A4,BG=BB,BF=[],BH=BA,BE=BA&&BA[0]&&U.isXML(BA[0]);
BB&&BA.length;
){for(var BD in S.filter){if((A5=S.leftMatch[BD].exec(BB))!=null&&A5[2]){var BC,A3,A9=S.filter[BD];
A3=A5[1];
A4=false;
A5.splice(1,1);
if(A3.substr(A3.length-1)!=="\\"){if(BH===BF){BF=[]
}if(S.preFilter[BD]){if(A5=S.preFilter[BD](A5,BH,A6,BF,A7,BE)){if(A5===true){continue
}}else{A4=BC=true
}}if(A5){for(var A8=0;
(A3=BH[A8])!=null;
A8++){if(A3){BC=A9(A3,A5,A8,BH);
var BI=A7^!!BC;
if(A6&&BC!=null){if(BI){A4=true
}else{BH[A8]=false
}}else{if(BI){BF.push(A3);
A4=true
}}}}}if(BC!==AW){A6||(BH=BF);
BB=BB.replace(S.match[BD],"");
if(!A4){return[]
}break
}}}}if(BB===BG){if(A4==null){U.error(BB)
}else{break
}}BG=BB
}return BH
};
U.error=function(A3){throw"Syntax error, unrecognized expression: "+A3
};
var S=U.selectors={order:["ID","NAME","TAG"],match:{ID:/#((?:[\w\u00c0-\uFFFF\-]|\\.)+)/,CLASS:/\.((?:[\w\u00c0-\uFFFF\-]|\\.)+)/,NAME:/\[name=['"]*((?:[\w\u00c0-\uFFFF\-]|\\.)+)['"]*\]/,ATTR:/\[\s*((?:[\w\u00c0-\uFFFF\-]|\\.)+)\s*(?:(\S?=)\s*(['"]*)(.*?)\3|)\s*\]/,TAG:/^((?:[\w\u00c0-\uFFFF\*\-]|\\.)+)/,CHILD:/:(only|nth|last|first)-child(?:\((even|odd|[\dn+\-]*)\))?/,POS:/:(nth|eq|gt|lt|first|last|even|odd)(?:\((\d*)\))?(?=[^\-]|$)/,PSEUDO:/:((?:[\w\u00c0-\uFFFF\-]|\\.)+)(?:\((['"]?)((?:\([^\)]+\)|[^\(\)]*)+)\2\))?/},leftMatch:{},attrMap:{"class":"className","for":"htmlFor"},attrHandle:{href:function(A3){return A3.getAttribute("href")
}},relative:{"+":function(A5,A4){var A8=typeof A4==="string",A3=A8&&!/\W/.test(A4);
A8=A8&&!A3;
if(A3){A4=A4.toLowerCase()
}A3=0;
for(var A7=A5.length,A6;
A3<A7;
A3++){if(A6=A5[A3]){for(;
(A6=A6.previousSibling)&&A6.nodeType!==1;
){}A5[A3]=A8||A6&&A6.nodeName.toLowerCase()===A4?A6||false:A6===A4
}}A8&&U.filter(A4,A5,true)
},">":function(A5,A4){var A8,A3=typeof A4==="string",A7=0,A6=A5.length;
if(A3&&!/\W/.test(A4)){for(A4=A4.toLowerCase();
A7<A6;
A7++){if(A8=A5[A7]){A8=A8.parentNode;
A5[A7]=A8.nodeName.toLowerCase()===A4?A8:false
}}}else{for(;
A7<A6;
A7++){if(A8=A5[A7]){A5[A7]=A3?A8.parentNode:A8.parentNode===A4
}}A3&&U.filter(A4,A5,true)
}},"":function(A5,A4,A8){var A3,A7=X++,A6=c;
if(typeof A4==="string"&&!/\W/.test(A4)){A3=A4=A4.toLowerCase();
A6=A0
}A6("parentNode",A4,A7,A5,A3,A8)
},"~":function(A5,A4,A8){var A3,A7=X++,A6=c;
if(typeof A4==="string"&&!/\W/.test(A4)){A3=A4=A4.toLowerCase();
A6=A0
}A6("previousSibling",A4,A7,A5,A3,A8)
}},find:{ID:function(A4,A3,A5){if(typeof A3.getElementById!=="undefined"&&!A5){return(A4=A3.getElementById(A4[1]))&&A4.parentNode?[A4]:[]
}},NAME:function(A5,A4){if(typeof A4.getElementsByName!=="undefined"){for(var A8=[],A3=A4.getElementsByName(A5[1]),A7=0,A6=A3.length;
A7<A6;
A7++){A3[A7].getAttribute("name")===A5[1]&&A8.push(A3[A7])
}return A8.length===0?null:A8
}},TAG:function(A4,A3){return A3.getElementsByTagName(A4[1])
}},preFilter:{CLASS:function(A6,A5,A9,A3,A8,A7){A6=" "+A6[1].replace(/\\/g,"")+" ";
if(A7){return A6
}A7=0;
for(var A4;
(A4=A5[A7])!=null;
A7++){if(A4){if(A8^(A4.className&&(" "+A4.className+" ").replace(/[\t\n]/g," ").indexOf(A6)>=0)){A9||A3.push(A4)
}else{if(A9){A5[A7]=false
}}}}return false
},ID:function(A3){return A3[1].replace(/\\/g,"")
},TAG:function(A3){return A3[1].toLowerCase()
},CHILD:function(A4){if(A4[1]==="nth"){var A3=/(-?)(\d*)n((?:\+|-)?\d*)/.exec(A4[2]==="even"&&"2n"||A4[2]==="odd"&&"2n+1"||!/\D/.test(A4[2])&&"0n+"+A4[2]||A4[2]);
A4[2]=A3[1]+(A3[2]||1)-0;
A4[3]=A3[3]-0
}A4[0]=X++;
return A4
},ATTR:function(A5,A4,A8,A3,A7,A6){A4=A5[1].replace(/\\/g,"");
if(!A6&&S.attrMap[A4]){A5[1]=S.attrMap[A4]
}if(A5[2]==="~="){A5[4]=" "+A5[4]+" "
}return A5
},PSEUDO:function(A5,A4,A7,A3,A6){if(A5[1]==="not"){if((Y.exec(A5[3])||"").length>1||/^\w/.test(A5[3])){A5[3]=U(A5[3],null,null,A4)
}else{A5=U.filter(A5[3],A4,A7,true^A6);
A7||A3.push.apply(A3,A5);
return false
}}else{if(S.match.POS.test(A5[0])||S.match.CHILD.test(A5[0])){return true
}}return A5
},POS:function(A3){A3.unshift(true);
return A3
}},filters:{enabled:function(A3){return A3.disabled===false&&A3.type!=="hidden"
},disabled:function(A3){return A3.disabled===true
},checked:function(A3){return A3.checked===true
},selected:function(A3){return A3.selected===true
},parent:function(A3){return !!A3.firstChild
},empty:function(A3){return !A3.firstChild
},has:function(A4,A3,A5){return !!U(A5[3],A4).length
},header:function(A3){return/h\d/i.test(A3.nodeName)
},text:function(A3){return"text"===A3.type
},radio:function(A3){return"radio"===A3.type
},checkbox:function(A3){return"checkbox"===A3.type
},file:function(A3){return"file"===A3.type
},password:function(A3){return"password"===A3.type
},submit:function(A3){return"submit"===A3.type
},image:function(A3){return"image"===A3.type
},reset:function(A3){return"reset"===A3.type
},button:function(A3){return"button"===A3.type||A3.nodeName.toLowerCase()==="button"
},input:function(A3){return/input|select|textarea|button/i.test(A3.nodeName)
}},setFilters:{first:function(A4,A3){return A3===0
},last:function(A5,A4,A6,A3){return A4===A3.length-1
},even:function(A4,A3){return A3%2===0
},odd:function(A4,A3){return A3%2===1
},lt:function(A4,A3,A5){return A3<A5[3]-0
},gt:function(A4,A3,A5){return A3>A5[3]-0
},nth:function(A4,A3,A5){return A5[3]-0===A3
},eq:function(A4,A3,A5){return A5[3]-0===A3
}},filter:{PSEUDO:function(A5,A4,A8,A3){var A7=A4[1],A6=S.filters[A7];
if(A6){return A6(A5,A8,A4,A3)
}else{if(A7==="contains"){return(A5.textContent||A5.innerText||U.getText([A5])||"").indexOf(A4[3])>=0
}else{if(A7==="not"){A4=A4[3];
A8=0;
for(A3=A4.length;
A8<A3;
A8++){if(A4[A8]===A5){return false
}}return true
}else{U.error("Syntax error, unrecognized expression: "+A7)
}}}},CHILD:function(A6,A5){var BA=A5[1],A3=A6;
switch(BA){case"only":case"first":for(;
A3=A3.previousSibling;
){if(A3.nodeType===1){return false
}}if(BA==="first"){return true
}A3=A6;
case"last":for(;
A3=A3.nextSibling;
){if(A3.nodeType===1){return false
}}return true;
case"nth":BA=A5[2];
var A8=A5[3];
if(BA===1&&A8===0){return true
}var A7=A5[0],A4=A6.parentNode;
if(A4&&(A4.sizcache!==A7||!A6.nodeIndex)){var A9=0;
for(A3=A4.firstChild;
A3;
A3=A3.nextSibling){if(A3.nodeType===1){A3.nodeIndex=++A9
}}A4.sizcache=A7
}A3=A6.nodeIndex-A8;
return BA===0?A3===0:A3%BA===0&&A3/BA>=0
}},ID:function(A4,A3){return A4.nodeType===1&&A4.getAttribute("id")===A3
},TAG:function(A4,A3){return A3==="*"&&A4.nodeType===1||A4.nodeName.toLowerCase()===A3
},CLASS:function(A4,A3){return(" "+(A4.className||A4.getAttribute("class"))+" ").indexOf(A3)>-1
},ATTR:function(A5,A4){var A8=A4[1];
A8=S.attrHandle[A8]?S.attrHandle[A8](A5):A5[A8]!=null?A5[A8]:A5.getAttribute(A8);
var A3=A8+"",A7=A4[2],A6=A4[4];
return A8==null?A7==="!=":A7==="="?A3===A6:A7==="*="?A3.indexOf(A6)>=0:A7==="~="?(" "+A3+" ").indexOf(A6)>=0:!A6?A3&&A8!==false:A7==="!="?A3!==A6:A7==="^="?A3.indexOf(A6)===0:A7==="$="?A3.substr(A3.length-A6.length)===A6:A7==="|="?A3===A6||A3.substr(0,A6.length+1)===A6+"-":false
},POS:function(A5,A4,A7,A3){var A6=S.setFilters[A4[2]];
if(A6){return A6(A5,A7,A4,A3)
}}}},Aa=S.match.POS,B=function(A4,A3){return"\\"+(A3-0+1)
},P;
for(P in S.match){S.match[P]=RegExp(S.match[P].source+/(?![^\[]*\])(?![^\(]*\))/.source);
S.leftMatch[P]=RegExp(/(^(?:.|\r|\n)*?)/.source+S.match[P].source.replace(/\\(\d+)/g,B))
}var E=function(A4,A3){A4=Array.prototype.slice.call(A4,0);
if(A3){A3.push.apply(A3,A4);
return A3
}return A4
};
try{Array.prototype.slice.call(o.documentElement.childNodes,0)
}catch(t){E=function(A5,A4){var A7=0,A3=A4||[];
if(W.call(A5)==="[object Array]"){Array.prototype.push.apply(A3,A5)
}else{if(typeof A5.length==="number"){for(var A6=A5.length;
A7<A6;
A7++){A3.push(A5[A7])
}}else{for(;
A5[A7];
A7++){A3.push(A5[A7])
}}}return A3
}
}var A2,A1;
if(o.documentElement.compareDocumentPosition){A2=function(A4,A3){if(A4===A3){V=true;
return 0
}if(!A4.compareDocumentPosition||!A3.compareDocumentPosition){return A4.compareDocumentPosition?-1:1
}return A4.compareDocumentPosition(A3)&4?-1:1
}
}else{A2=function(A6,A5){var A9,A3,A8=[],A7=[];
A9=A6.parentNode;
A3=A5.parentNode;
var A4=A9;
if(A6===A5){V=true;
return 0
}else{if(A9===A3){return A1(A6,A5)
}else{if(A9){if(!A3){return 1
}}else{return -1
}}}for(;
A4;
){A8.unshift(A4);
A4=A4.parentNode
}for(A4=A3;
A4;
){A7.unshift(A4);
A4=A4.parentNode
}A9=A8.length;
A3=A7.length;
for(A4=0;
A4<A9&&A4<A3;
A4++){if(A8[A4]!==A7[A4]){return A1(A8[A4],A7[A4])
}}return A4===A9?A1(A6,A7[A4],-1):A1(A8[A4],A5,1)
};
A1=function(A4,A3,A5){if(A4===A3){return A5
}for(A4=A4.nextSibling;
A4;
){if(A4===A3){return -1
}A4=A4.nextSibling
}return 1
}
}U.getText=function(A5){for(var A4="",A6,A3=0;
A5[A3];
A3++){A6=A5[A3];
if(A6.nodeType===3||A6.nodeType===4){A4+=A6.nodeValue
}else{if(A6.nodeType!==8){A4+=U.getText(A6.childNodes)
}}}return A4
};
(function(){var A4=o.createElement("div"),A3="script"+(new Date).getTime(),A5=o.documentElement;
A4.innerHTML="<a name='"+A3+"'/>";
A5.insertBefore(A4,A5.firstChild);
if(o.getElementById(A3)){S.find.ID=function(A6,A8,A7){if(typeof A8.getElementById!=="undefined"&&!A7){return(A8=A8.getElementById(A6[1]))?A8.id===A6[1]||typeof A8.getAttributeNode!=="undefined"&&A8.getAttributeNode("id").nodeValue===A6[1]?[A8]:AW:[]
}};
S.filter.ID=function(A6,A8){var A7=typeof A6.getAttributeNode!=="undefined"&&A6.getAttributeNode("id");
return A6.nodeType===1&&A7&&A7.nodeValue===A8
}
}A5.removeChild(A4);
A5=A4=null
})();
(function(){var A3=o.createElement("div");
A3.appendChild(o.createComment(""));
if(A3.getElementsByTagName("*").length>0){S.find.TAG=function(A5,A8){var A4=A8.getElementsByTagName(A5[1]);
if(A5[1]==="*"){for(var A7=[],A6=0;
A4[A6];
A6++){A4[A6].nodeType===1&&A7.push(A4[A6])
}A4=A7
}return A4
}
}A3.innerHTML="<a href='#'></a>";
if(A3.firstChild&&typeof A3.firstChild.getAttribute!=="undefined"&&A3.firstChild.getAttribute("href")!=="#"){S.attrHandle.href=function(A4){return A4.getAttribute("href",2)
}
}A3=null
})();
o.querySelectorAll&&function(){var A4=U,A3=o.createElement("div");
A3.innerHTML="<p class='TEST'></p>";
if(!(A3.querySelectorAll&&A3.querySelectorAll(".TEST").length===0)){U=function(A6,BA,A9,A7){BA=BA||o;
A6=A6.replace(/\=\s*([^'"\]]*)\s*\]/g,"='$1']");
if(!A7&&!U.isXML(BA)){if(BA.nodeType===9){try{return E(BA.querySelectorAll(A6),A9)
}catch(BD){}}else{if(BA.nodeType===1&&BA.nodeName.toLowerCase()!=="object"){var A8=BA.getAttribute("id"),BC=A8||"__sizzle__";
A8||BA.setAttribute("id",BC);
try{return E(BA.querySelectorAll("#"+BC+" "+A6),A9)
}catch(BB){}finally{A8||BA.removeAttribute("id")
}}}}return A4(A6,BA,A9,A7)
};
for(var A5 in A4){U[A5]=A4[A5]
}A3=null
}}();
(function(){var A5=o.documentElement,A4=A5.matchesSelector||A5.mozMatchesSelector||A5.webkitMatchesSelector||A5.msMatchesSelector,A6=false;
try{A4.call(o.documentElement,"[test!='']:sizzle")
}catch(A3){A6=true
}if(A4){U.matchesSelector=function(A9,A8){A8=A8.replace(/\=\s*([^'"\]]*)\s*\]/g,"='$1']");
if(!U.isXML(A9)){try{if(A6||!S.match.PSEUDO.test(A8)&&!/!=/.test(A8)){return A4.call(A9,A8)
}}catch(A7){}}return U(A8,null,null,[A9]).length>0
}
}})();
(function(){var A3=o.createElement("div");
A3.innerHTML="<div class='test e'></div><div class='test'></div>";
if(!(!A3.getElementsByClassName||A3.getElementsByClassName("e").length===0)){A3.lastChild.className="e";
if(A3.getElementsByClassName("e").length!==1){S.order.splice(1,0,"CLASS");
S.find.CLASS=function(A5,A6,A4){if(typeof A6.getElementsByClassName!=="undefined"&&!A4){return A6.getElementsByClassName(A5[1])
}};
A3=null
}}})();
U.contains=o.documentElement.contains?function(A4,A3){return A4!==A3&&(A4.contains?A4.contains(A3):true)
}:o.documentElement.compareDocumentPosition?function(A4,A3){return !!(A4.compareDocumentPosition(A3)&16)
}:function(){return false
};
U.isXML=function(A3){return(A3=(A3?A3.ownerDocument||A3:0).documentElement)?A3.nodeName!=="HTML":false
};
var Z=function(A6,A5){for(var A9,A3=[],A8="",A7=A5.nodeType?[A5]:A5;
A9=S.match.PSEUDO.exec(A6);
){A8+=A9[0];
A6=A6.replace(S.match.PSEUDO,"")
}A6=S.relative[A6]?A6+"*":A6;
A9=0;
for(var A4=A7.length;
A9<A4;
A9++){U(A6,A7[A9],A3)
}return U.filter(A8,A3)
};
x.find=U;
x.expr=U.selectors;
x.expr[":"]=x.expr.filters;
x.unique=U.uniqueSort;
x.text=U.getText;
x.isXMLDoc=U.isXML;
x.contains=U.contains
})();
var Aq=/Until$/,AV=/^(?:parents|prevUntil|prevAll)/,Ad=/,/,An=/^.[^:#\[\.,]*$/,AL=Array.prototype.slice,p=x.expr.match.POS;
x.fn.extend({find:function(P){for(var B=this.pushStack("","find",P),V=0,U=0,T=this.length;
U<T;
U++){V=B.length;
x.find(P,this[U],B);
if(U>0){for(var S=V;
S<B.length;
S++){for(var E=0;
E<V;
E++){if(B[E]===B[S]){B.splice(S--,1);
break
}}}}}return B
},has:function(E){var B=x(E);
return this.filter(function(){for(var S=0,P=B.length;
S<P;
S++){if(x.contains(this,B[S])){return true
}}})
},not:function(B){return this.pushStack(Ai(this,B,false),"not",B)
},filter:function(B){return this.pushStack(Ai(this,B,true),"filter",B)
},is:function(B){return !!B&&x.filter(B,this).length>0
},closest:function(X,W){var V=[],U,T,S=this[0];
if(x.isArray(X)){var E,P={},B=1;
if(S&&X.length){U=0;
for(T=X.length;
U<T;
U++){E=X[U];
P[E]||(P[E]=x.expr.match.POS.test(E)?x(E,W||this.context):E)
}for(;
S&&S.ownerDocument&&S!==W;
){for(E in P){U=P[E];
if(U.jquery?U.index(S)>-1:x(S).is(U)){V.push({selector:E,elem:S,level:B})
}}S=S.parentNode;
B++
}}return V
}E=p.test(X)?x(X,W||this.context):null;
U=0;
for(T=this.length;
U<T;
U++){for(S=this[U];
S;
){if(E?E.index(S)>-1:x.find.matchesSelector(S,X)){V.push(S);
break
}else{S=S.parentNode;
if(!S||!S.ownerDocument||S===W){break
}}}}V=V.length>1?x.unique(V):V;
return this.pushStack(V,"closest",X)
},index:function(B){if(!B||typeof B==="string"){return x.inArray(this[0],B?x(B):this.parent().children())
}return x.inArray(B.jquery?B[0]:B,this)
},add:function(E,B){var S=typeof E==="string"?x(E,B||this.context):x.makeArray(E),P=x.merge(this.get(),S);
return this.pushStack(!S[0]||!S[0].parentNode||S[0].parentNode.nodeType===11||!P[0]||!P[0].parentNode||P[0].parentNode.nodeType===11?P:x.unique(P))
},andSelf:function(){return this.add(this.prevObject)
}});
x.each({parent:function(B){return(B=B.parentNode)&&B.nodeType!==11?B:null
},parents:function(B){return x.dir(B,"parentNode")
},parentsUntil:function(E,B,P){return x.dir(E,"parentNode",P)
},next:function(B){return x.nth(B,2,"nextSibling")
},prev:function(B){return x.nth(B,2,"previousSibling")
},nextAll:function(B){return x.dir(B,"nextSibling")
},prevAll:function(B){return x.dir(B,"previousSibling")
},nextUntil:function(E,B,P){return x.dir(E,"nextSibling",P)
},prevUntil:function(E,B,P){return x.dir(E,"previousSibling",P)
},siblings:function(B){return x.sibling(B.parentNode.firstChild,B)
},children:function(B){return x.sibling(B.firstChild)
},contents:function(B){return x.nodeName(B,"iframe")?B.contentDocument||B.contentWindow.document:x.makeArray(B.childNodes)
}},function(E,B){x.fn[E]=function(T,S){var P=x.map(this,B,T);
Aq.test(E)||(S=T);
if(S&&typeof S==="string"){P=x.filter(S,P)
}P=this.length>1?x.unique(P):P;
if((this.length>1||Ad.test(S))&&AV.test(E)){P=P.reverse()
}return this.pushStack(P,E,AL.call(arguments).join(","))
}
});
x.extend({filter:function(E,B,P){if(P){E=":not("+E+")"
}return B.length===1?x.find.matchesSelector(B[0],E)?[B[0]]:[]:x.find.matches(E,B)
},dir:function(E,B,S){var P=[];
for(E=E[B];
E&&E.nodeType!==9&&(S===AW||E.nodeType!==1||!x(E).is(S));
){E.nodeType===1&&P.push(E);
E=E[B]
}return P
},nth:function(E,B,S){B=B||1;
for(var P=0;
E;
E=E[S]){if(E.nodeType===1&&++P===B){break
}}return E
},sibling:function(E,B){for(var P=[];
E;
E=E.nextSibling){E.nodeType===1&&E!==B&&P.push(E)
}return P
}});
var Ab=/ jQuery\d+="(?:\d+|null)"/g,Ak=/^\s+/,At=/<(?!area|br|col|embed|hr|img|input|link|meta|param)(([\w:]+)[^>]*)\/>/ig,Ag=/<([\w:]+)/,b=/<tbody/i,G=/<|&#?\w+;/,AQ=/<(?:script|object|embed|option|style)/i,u=/checked\s*(?:[^=]|=\s*.checked.)/i,Ar=/\=([^="'>\s]+\/)>/g,AM={option:[1,"<select multiple='multiple'>","</select>"],legend:[1,"<fieldset>","</fieldset>"],thead:[1,"<table>","</table>"],tr:[2,"<table><tbody>","</tbody></table>"],td:[3,"<table><tbody><tr>","</tr></tbody></table>"],col:[2,"<table><tbody></tbody><colgroup>","</colgroup></table>"],area:[1,"<map>","</map>"],_default:[0,"",""]};
AM.optgroup=AM.option;
AM.tbody=AM.tfoot=AM.colgroup=AM.caption=AM.thead;
AM.th=AM.td;
if(!x.support.htmlSerialize){AM._default=[1,"div<div>","</div>"]
}x.fn.extend({text:function(B){if(x.isFunction(B)){return this.each(function(E){var P=x(this);
P.text(B.call(this,E,P.text()))
})
}if(typeof B!=="object"&&B!==AW){return this.empty().append((this[0]&&this[0].ownerDocument||o).createTextNode(B))
}return x.text(this)
},wrapAll:function(E){if(x.isFunction(E)){return this.each(function(P){x(this).wrapAll(E.call(this,P))
})
}if(this[0]){var B=x(E,this[0].ownerDocument).eq(0).clone(true);
this[0].parentNode&&B.insertBefore(this[0]);
B.map(function(){for(var P=this;
P.firstChild&&P.firstChild.nodeType===1;
){P=P.firstChild
}return P
}).append(this)
}return this
},wrapInner:function(B){if(x.isFunction(B)){return this.each(function(E){x(this).wrapInner(B.call(this,E))
})
}return this.each(function(){var E=x(this),P=E.contents();
P.length?P.wrapAll(B):E.append(B)
})
},wrap:function(B){return this.each(function(){x(this).wrapAll(B)
})
},unwrap:function(){return this.parent().each(function(){x.nodeName(this,"body")||x(this).replaceWith(this.childNodes)
}).end()
},append:function(){return this.domManip(arguments,true,function(B){this.nodeType===1&&this.appendChild(B)
})
},prepend:function(){return this.domManip(arguments,true,function(B){this.nodeType===1&&this.insertBefore(B,this.firstChild)
})
},before:function(){if(this[0]&&this[0].parentNode){return this.domManip(arguments,false,function(E){this.parentNode.insertBefore(E,this)
})
}else{if(arguments.length){var B=x(arguments[0]);
B.push.apply(B,this.toArray());
return this.pushStack(B,"before",arguments)
}}},after:function(){if(this[0]&&this[0].parentNode){return this.domManip(arguments,false,function(E){this.parentNode.insertBefore(E,this.nextSibling)
})
}else{if(arguments.length){var B=this.pushStack(this,"after",arguments);
B.push.apply(B,x(arguments[0]).toArray());
return B
}}},remove:function(E,B){for(var S=0,P;
(P=this[S])!=null;
S++){if(!E||x.filter(E,[P]).length){if(!B&&P.nodeType===1){x.cleanData(P.getElementsByTagName("*"));
x.cleanData([P])
}P.parentNode&&P.parentNode.removeChild(P)
}}return this
},empty:function(){for(var E=0,B;
(B=this[E])!=null;
E++){for(B.nodeType===1&&x.cleanData(B.getElementsByTagName("*"));
B.firstChild;
){B.removeChild(B.firstChild)
}}return this
},clone:function(E){var B=this.map(function(){if(!x.support.noCloneEvent&&!x.isXMLDoc(this)){var S=this.outerHTML,P=this.ownerDocument;
if(!S){S=P.createElement("div");
S.appendChild(this.cloneNode(true));
S=S.innerHTML
}return x.clean([S.replace(Ab,"").replace(Ar,'="$1">').replace(Ak,"")],P)[0]
}else{return this.cloneNode(true)
}});
if(E===true){AT(this,B);
AT(this.find("*"),B.find("*"))
}return B
},html:function(E){if(E===AW){return this[0]&&this[0].nodeType===1?this[0].innerHTML.replace(Ab,""):null
}else{if(typeof E==="string"&&!AQ.test(E)&&(x.support.leadingWhitespace||!Ak.test(E))&&!AM[(Ag.exec(E)||["",""])[1].toLowerCase()]){E=E.replace(At,"<$1></$2>");
try{for(var B=0,S=this.length;
B<S;
B++){if(this[B].nodeType===1){x.cleanData(this[B].getElementsByTagName("*"));
this[B].innerHTML=E
}}}catch(P){this.empty().append(E)
}}else{x.isFunction(E)?this.each(function(U){var T=x(this);
T.html(E.call(this,U,T.html()))
}):this.empty().append(E)
}}return this
},replaceWith:function(B){if(this[0]&&this[0].parentNode){if(x.isFunction(B)){return this.each(function(E){var S=x(this),P=S.html();
S.replaceWith(B.call(this,E,P))
})
}if(typeof B!=="string"){B=x(B).detach()
}return this.each(function(){var E=this.nextSibling,P=this.parentNode;
x(this).remove();
E?x(E).before(B):x(P).append(B)
})
}else{return this.pushStack(x(x.isFunction(B)?B():B),"replaceWith",B)
}},detach:function(B){return this.remove(B,true)
},domManip:function(X,W,V){var U,T,S,E=X[0],P=[];
if(!x.support.checkClone&&arguments.length===3&&typeof E==="string"&&u.test(E)){return this.each(function(){x(this).domManip(X,W,V,true)
})
}if(x.isFunction(E)){return this.each(function(Y){var Z=x(this);
X[0]=E.call(this,Y,W?Z.html():AW);
Z.domManip(X,W,V)
})
}if(this[0]){U=E&&E.parentNode;
U=x.support.parentNode&&U&&U.nodeType===11&&U.childNodes.length===this.length?{fragment:U}:x.buildFragment(X,this,P);
S=U.fragment;
if(T=S.childNodes.length===1?S=S.firstChild:S.firstChild){W=W&&x.nodeName(T,"tr");
T=0;
for(var B=this.length;
T<B;
T++){V.call(W?x.nodeName(this[T],"table")?this[T].getElementsByTagName("tbody")[0]||this[T].appendChild(this[T].ownerDocument.createElement("tbody")):this[T]:this[T],T>0||U.cacheable||this.length>1?S.cloneNode(true):S)
}}P.length&&x.each(P,AZ)
}return this
}});
x.buildFragment=function(E,B,U){var T,S,P;
B=B&&B[0]?B[0].ownerDocument||B[0]:o;
if(E.length===1&&typeof E[0]==="string"&&E[0].length<512&&B===o&&!AQ.test(E[0])&&(x.support.checkClone||!u.test(E[0]))){S=true;
if(P=x.fragments[E[0]]){if(P!==1){T=P
}}}if(!T){T=B.createDocumentFragment();
x.clean(E,B,T,U)
}if(S){x.fragments[E[0]]=P?T:1
}return{fragment:T,cacheable:S}
};
x.fragments={};
x.each({appendTo:"append",prependTo:"prepend",insertBefore:"before",insertAfter:"after",replaceAll:"replaceWith"},function(E,B){x.fn[E]=function(V){var U=[];
V=x(V);
var T=this.length===1&&this[0].parentNode;
if(T&&T.nodeType===11&&T.childNodes.length===1&&V.length===1){V[B](this[0]);
return this
}else{T=0;
for(var S=V.length;
T<S;
T++){var P=(T>0?this.clone(true):this).get();
x(V[T])[B](P);
U=U.concat(P)
}return this.pushStack(U,E,V.selector)
}}
});
x.extend({clean:function(Z,X,W,V){X=X||o;
if(typeof X.createElement==="undefined"){X=X.ownerDocument||X[0]&&X[0].ownerDocument||o
}for(var U=[],T=0,P;
(P=Z[T])!=null;
T++){if(typeof P==="number"){P+=""
}if(P){if(typeof P==="string"&&!G.test(P)){P=X.createTextNode(P)
}else{if(typeof P==="string"){P=P.replace(At,"<$1></$2>");
var S=(Ag.exec(P)||["",""])[1].toLowerCase(),E=AM[S]||AM._default,Y=E[0],B=X.createElement("div");
for(B.innerHTML=E[1]+P+E[2];
Y--;
){B=B.lastChild
}if(!x.support.tbody){Y=b.test(P);
S=S==="table"&&!Y?B.firstChild&&B.firstChild.childNodes:E[1]==="<table>"&&!Y?B.childNodes:[];
for(E=S.length-1;
E>=0;
--E){x.nodeName(S[E],"tbody")&&!S[E].childNodes.length&&S[E].parentNode.removeChild(S[E])
}}!x.support.leadingWhitespace&&Ak.test(P)&&B.insertBefore(X.createTextNode(Ak.exec(P)[0]),B.firstChild);
P=B.childNodes
}}if(P.nodeType){U.push(P)
}else{U=x.merge(U,P)
}}}if(W){for(T=0;
U[T];
T++){if(V&&x.nodeName(U[T],"script")&&(!U[T].type||U[T].type.toLowerCase()==="text/javascript")){V.push(U[T].parentNode?U[T].parentNode.removeChild(U[T]):U[T])
}else{U[T].nodeType===1&&U.splice.apply(U,[T+1,0].concat(x.makeArray(U[T].getElementsByTagName("script"))));
W.appendChild(U[T])
}}}return U
},cleanData:function(X){for(var W,V,U=x.cache,T=x.event.special,S=x.support.deleteExpando,E=0,P;
(P=X[E])!=null;
E++){if(!(P.nodeName&&x.noData[P.nodeName.toLowerCase()])){if(V=P[x.expando]){if((W=U[V])&&W.events){for(var B in W.events){T[B]?x.event.remove(P,B):x.removeEvent(P,B,W.handle)
}}if(S){delete P[x.expando]
}else{P.removeAttribute&&P.removeAttribute(x.expando)
}delete U[V]
}}}}});
var g=/alpha\([^)]*\)/i,Af=/opacity=([^)]*)/,AO=/-([a-z])/ig,r=/([A-Z])/g,K=/^-?\d+(?:px)?$/i,e=/^-?\d/,I={position:"absolute",visibility:"hidden",display:"block"},AB=["Left","Right"],m=["Top","Bottom"],AG,Aw,Ae,Au=function(E,B){return B.toUpperCase()
};
x.fn.css=function(E,B){if(arguments.length===2&&B===AW){return this
}return x.access(this,E,B,true,function(T,S,P){return P!==AW?x.style(T,S,P):x.css(T,S)
})
};
x.extend({cssHooks:{opacity:{get:function(E,B){if(B){var P=AG(E,"opacity","opacity");
return P===""?"1":P
}else{return E.style.opacity
}}}},cssNumber:{zIndex:true,fontWeight:true,opacity:true,zoom:true,lineHeight:true},cssProps:{"float":x.support.cssFloat?"cssFloat":"styleFloat"},style:function(X,W,V,U){if(!(!X||X.nodeType===3||X.nodeType===8||!X.style)){var T,S=x.camelCase(W),E=X.style,P=x.cssHooks[S];
W=x.cssProps[S]||S;
if(V!==AW){if(!(typeof V==="number"&&isNaN(V)||V==null)){if(typeof V==="number"&&!x.cssNumber[S]){V+="px"
}if(!P||!("set" in P)||(V=P.set(X,V))!==AW){try{E[W]=V
}catch(B){}}}}else{if(P&&"get" in P&&(T=P.get(X,false,U))!==AW){return T
}return E[W]
}}},css:function(E,B,U){var T,S=x.camelCase(B),P=x.cssHooks[S];
B=x.cssProps[S]||S;
if(P&&"get" in P&&(T=P.get(E,true,U))!==AW){return T
}else{if(AG){return AG(E,B,S)
}}},swap:function(E,B,T){var S={},P;
for(P in B){S[P]=E.style[P];
E.style[P]=B[P]
}T.call(E);
for(P in B){E.style[P]=S[P]
}},camelCase:function(B){return B.replace(AO,Au)
}});
x.curCSS=x.css;
x.each(["height","width"],function(E,B){x.cssHooks[B]={get:function(U,T,S){var P;
if(T){if(U.offsetWidth!==0){P=w(U,B,S)
}else{x.swap(U,I,function(){P=w(U,B,S)
})
}if(P<=0){P=AG(U,B,B);
if(P==="0px"&&Ae){P=Ae(U,B,B)
}if(P!=null){return P===""||P==="auto"?"0px":P
}}if(P<0||P==null){P=U.style[B];
return P===""||P==="auto"?"0px":P
}return typeof P==="string"?P:P+"px"
}},set:function(S,P){if(K.test(P)){P=parseFloat(P);
if(P>=0){return P+"px"
}}else{return P
}}}
});
if(!x.support.opacity){x.cssHooks.opacity={get:function(E,B){return Af.test((B&&E.currentStyle?E.currentStyle.filter:E.style.filter)||"")?parseFloat(RegExp.$1)/100+"":B?"1":""
},set:function(E,B){var T=E.style;
T.zoom=1;
var S=x.isNaN(B)?"":"alpha(opacity="+B*100+")",P=T.filter||"";
T.filter=g.test(P)?P.replace(g,S):T.filter+" "+S
}}
}if(o.defaultView&&o.defaultView.getComputedStyle){Aw=function(E,B,S){var P;
S=S.replace(r,"-$1").toLowerCase();
if(!(B=E.ownerDocument.defaultView)){return AW
}if(B=B.getComputedStyle(E,null)){P=B.getPropertyValue(S);
if(P===""&&!x.contains(E.ownerDocument.documentElement,E)){P=x.style(E,S)
}}return P
}
}if(o.documentElement.currentStyle){Ae=function(E,B){var U,T,S=E.currentStyle&&E.currentStyle[B],P=E.style;
if(!K.test(S)&&e.test(S)){U=P.left;
T=E.runtimeStyle.left;
E.runtimeStyle.left=E.currentStyle.left;
P.left=B==="fontSize"?"1em":S||0;
S=P.pixelLeft+"px";
P.left=U;
E.runtimeStyle.left=T
}return S===""?"auto":S
}
}AG=Aw||Ae;
if(x.expr&&x.expr.filters){x.expr.filters.hidden=function(E){var B=E.offsetHeight;
return E.offsetWidth===0&&B===0||!x.support.reliableHiddenOffsets&&(E.style.display||x.css(E,"display"))==="none"
};
x.expr.filters.visible=function(B){return !x.expr.filters.hidden(B)
}
}var Ah=x.now(),AR=/<script\b[^<]*(?:(?!<\/script>)<[^<]*)*<\/script>/gi,v=/^(?:select|textarea)/i,h=/^(?:color|date|datetime|email|hidden|month|number|password|range|search|tel|text|time|url|week)$/i,L=/^(?:GET|HEAD)$/,R=/\[\]$/,AJ=/\=\?(&|$)/,f=/\?/,Ax=/([?&])_=[^&]*/,Al=/^(\w+:)?\/\/([^\/?#]+)/,AX=/%20/g,z=/#.*$/,Aj=x.fn.load;
x.fn.extend({load:function(E,B,U){if(typeof E!=="string"&&Aj){return Aj.apply(this,arguments)
}else{if(!this.length){return this
}}var T=E.indexOf(" ");
if(T>=0){var S=E.slice(T,E.length);
E=E.slice(0,T)
}T="GET";
if(B){if(x.isFunction(B)){U=B;
B=null
}else{if(typeof B==="object"){B=x.param(B,x.ajaxSettings.traditional);
T="POST"
}}}var P=this;
x.ajax({url:E,type:T,dataType:"html",data:B,complete:function(V,W){if(W==="success"||W==="notmodified"){P.html(S?x("<div>").append(V.responseText.replace(AR,"")).find(S):V.responseText)
}U&&P.each(U,[V.responseText,W,V])
}});
return this
},serialize:function(){return x.param(this.serializeArray())
},serializeArray:function(){return this.map(function(){return this.elements?x.makeArray(this.elements):this
}).filter(function(){return this.name&&!this.disabled&&(this.checked||v.test(this.nodeName)||h.test(this.type))
}).map(function(E,B){var P=x(this).val();
return P==null?null:x.isArray(P)?x.map(P,function(S){return{name:B.name,value:S}
}):{name:B.name,value:P}
}).get()
}});
x.each("ajaxStart ajaxStop ajaxComplete ajaxError ajaxSuccess ajaxSend".split(" "),function(E,B){x.fn[B]=function(P){return this.bind(B,P)
}
});
x.extend({get:function(E,B,S,P){if(x.isFunction(B)){P=P||S;
S=B;
B=null
}return x.ajax({type:"GET",url:E,data:B,success:S,dataType:P})
},getScript:function(E,B){return x.get(E,null,B,"script")
},getJSON:function(E,B,P){return x.get(E,B,P,"json")
},post:function(E,B,S,P){if(x.isFunction(B)){P=P||S;
S=B;
B={}
}return x.ajax({type:"POST",url:E,data:B,success:S,dataType:P})
},ajaxSetup:function(B){x.extend(x.ajaxSettings,B)
},ajaxSettings:{url:location.href,global:true,type:"GET",contentType:"application/x-www-form-urlencoded",processData:true,async:true,xhr:function(){return new AS.XMLHttpRequest
},accepts:{xml:"application/xml, text/xml",html:"text/html",script:"text/javascript, application/javascript",json:"application/json, text/javascript",text:"text/plain",_default:"*/*"}},ajax:function(A5){var A4=x.extend(true,{},x.ajaxSettings,A5),A3,A2,A1,Aa=A4.type.toUpperCase(),Z=L.test(Aa);
A4.url=A4.url.replace(z,"");
A4.context=A5&&A5.context!=null?A5.context:A4;
if(A4.data&&A4.processData&&typeof A4.data!=="string"){A4.data=x.param(A4.data,A4.traditional)
}if(A4.dataType==="jsonp"){if(Aa==="GET"){AJ.test(A4.url)||(A4.url+=(f.test(A4.url)?"&":"?")+(A4.jsonp||"callback")+"=?")
}else{if(!A4.data||!AJ.test(A4.data)){A4.data=(A4.data?A4.data+"&":"")+(A4.jsonp||"callback")+"=?"
}}A4.dataType="json"
}if(A4.dataType==="json"&&(A4.data&&AJ.test(A4.data)||AJ.test(A4.url))){A3=A4.jsonpCallback||"jsonp"+Ah++;
if(A4.data){A4.data=(A4.data+"").replace(AJ,"="+A3+"$1")
}A4.url=A4.url.replace(AJ,"="+A3+"$1");
A4.dataType="script";
var c=AS[A3];
AS[A3]=function(A6){if(x.isFunction(c)){c(A6)
}else{AS[A3]=AW;
try{delete AS[A3]
}catch(A7){}}A1=A6;
x.handleSuccess(A4,T,A2,A1);
x.handleComplete(A4,T,A2,A1);
V&&V.removeChild(W)
}
}if(A4.dataType==="script"&&A4.cache===null){A4.cache=false
}if(A4.cache===false&&Z){var X=x.now(),S=A4.url.replace(Ax,"$1_="+X);
A4.url=S+(S===A4.url?(f.test(A4.url)?"&":"?")+"_="+X:"")
}if(A4.data&&Z){A4.url+=(f.test(A4.url)?"&":"?")+A4.data
}A4.global&&x.active++===0&&x.event.trigger("ajaxStart");
X=(X=Al.exec(A4.url))&&(X[1]&&X[1].toLowerCase()!==location.protocol||X[2].toLowerCase()!==location.host);
if(A4.dataType==="script"&&Aa==="GET"&&X){var V=o.getElementsByTagName("head")[0]||o.documentElement,W=o.createElement("script");
if(A4.scriptCharset){W.charset=A4.scriptCharset
}W.src=A4.url;
if(!A3){var U=false;
W.onload=W.onreadystatechange=function(){if(!U&&(!this.readyState||this.readyState==="loaded"||this.readyState==="complete")){U=true;
x.handleSuccess(A4,T,A2,A1);
x.handleComplete(A4,T,A2,A1);
W.onload=W.onreadystatechange=null;
V&&W.parentNode&&V.removeChild(W)
}}
}V.insertBefore(W,V.firstChild);
return AW
}var E=false,T=A4.xhr();
if(T){A4.username?T.open(Aa,A4.url,A4.async,A4.username,A4.password):T.open(Aa,A4.url,A4.async);
try{if(A4.data!=null&&!Z||A5&&A5.contentType){T.setRequestHeader("Content-Type",A4.contentType)
}if(A4.ifModified){x.lastModified[A4.url]&&T.setRequestHeader("If-Modified-Since",x.lastModified[A4.url]);
x.etag[A4.url]&&T.setRequestHeader("If-None-Match",x.etag[A4.url])
}X||T.setRequestHeader("X-Requested-With","XMLHttpRequest");
T.setRequestHeader("Accept",A4.dataType&&A4.accepts[A4.dataType]?A4.accepts[A4.dataType]+", */*; q=0.01":A4.accepts._default)
}catch(P){}if(A4.beforeSend&&A4.beforeSend.call(A4.context,T,A4)===false){A4.global&&x.active--===1&&x.event.trigger("ajaxStop");
T.abort();
return false
}A4.global&&x.triggerGlobal(A4,"ajaxSend",[T,A4]);
var B=T.onreadystatechange=function(A6){if(!T||T.readyState===0||A6==="abort"){E||x.handleComplete(A4,T,A2,A1);
E=true;
if(T){T.onreadystatechange=x.noop
}}else{if(!E&&T&&(T.readyState===4||A6==="timeout")){E=true;
T.onreadystatechange=x.noop;
A2=A6==="timeout"?"timeout":!x.httpSuccess(T)?"error":A4.ifModified&&x.httpNotModified(T,A4.url)?"notmodified":"success";
var A8;
if(A2==="success"){try{A1=x.httpData(T,A4.dataType,A4)
}catch(A7){A2="parsererror";
A8=A7
}}if(A2==="success"||A2==="notmodified"){A3||x.handleSuccess(A4,T,A2,A1)
}else{x.handleError(A4,T,A2,A8)
}A3||x.handleComplete(A4,T,A2,A1);
A6==="timeout"&&T.abort();
if(A4.async){T=null
}}}};
try{var A0=T.abort;
T.abort=function(){T&&Function.prototype.call.call(A0,T);
B("abort")
}
}catch(t){}A4.async&&A4.timeout>0&&setTimeout(function(){T&&!E&&B("timeout")
},A4.timeout);
try{T.send(Z||A4.data==null?null:A4.data)
}catch(Y){x.handleError(A4,T,null,Y);
x.handleComplete(A4,T,A2,A1)
}A4.async||B();
return T
}},param:function(E,B){var T=[],S=function(V,U){U=x.isFunction(U)?U():U;
T[T.length]=encodeURIComponent(V)+"="+encodeURIComponent(U)
};
if(B===AW){B=x.ajaxSettings.traditional
}if(x.isArray(E)||E.jquery){x.each(E,function(){S(this.name,this.value)
})
}else{for(var P in E){d(P,E[P],B,S)
}}return T.join("&").replace(AX,"+")
}});
x.extend({active:0,lastModified:{},etag:{},handleError:function(E,B,S,P){E.error&&E.error.call(E.context,B,S,P);
E.global&&x.triggerGlobal(E,"ajaxError",[B,E,P])
},handleSuccess:function(E,B,S,P){E.success&&E.success.call(E.context,P,S,B);
E.global&&x.triggerGlobal(E,"ajaxSuccess",[B,E])
},handleComplete:function(E,B,P){E.complete&&E.complete.call(E.context,B,P);
E.global&&x.triggerGlobal(E,"ajaxComplete",[B,E]);
E.global&&x.active--===1&&x.event.trigger("ajaxStop")
},triggerGlobal:function(E,B,P){(E.context&&E.context.url==null?x(E.context):x.event).trigger(B,P)
},httpSuccess:function(E){try{return !E.status&&location.protocol==="file:"||E.status>=200&&E.status<300||E.status===304||E.status===1223
}catch(B){}return false
},httpNotModified:function(E,B){var S=E.getResponseHeader("Last-Modified"),P=E.getResponseHeader("Etag");
if(S){x.lastModified[B]=S
}if(P){x.etag[B]=P
}return E.status===304
},httpData:function(E,B,T){var S=E.getResponseHeader("content-type")||"",P=B==="xml"||!B&&S.indexOf("xml")>=0;
E=P?E.responseXML:E.responseText;
P&&E.documentElement.nodeName==="parsererror"&&x.error("parsererror");
if(T&&T.dataFilter){E=T.dataFilter(E,B)
}if(typeof E==="string"){if(B==="json"||!B&&S.indexOf("json")>=0){E=x.parseJSON(E)
}else{if(B==="script"||!B&&S.indexOf("javascript")>=0){x.globalEval(E)
}}}return E
}});
if(AS.ActiveXObject){x.ajaxSettings.xhr=function(){if(AS.location.protocol!=="file:"){try{return new AS.XMLHttpRequest
}catch(E){}}try{return new AS.ActiveXObject("Microsoft.XMLHTTP")
}catch(B){}}
}x.support.ajax=!!x.ajaxSettings.xhr();
var H={},k=/^(?:toggle|show|hide)$/,O=/^([+\-]=)?([\d+.\-]+)(.*)$/,AN,i=[["height","marginTop","marginBottom","paddingTop","paddingBottom"],["width","marginLeft","marginRight","paddingLeft","paddingRight"],["opacity"]];
x.fn.extend({show:function(E,B,S){if(E||E===0){return this.animate(AK("show",3),E,B,S)
}else{S=0;
for(var P=this.length;
S<P;
S++){E=this[S];
B=E.style.display;
if(!x.data(E,"olddisplay")&&B==="none"){B=E.style.display=""
}B===""&&x.css(E,"display")==="none"&&x.data(E,"olddisplay",M(E.nodeName))
}for(S=0;
S<P;
S++){E=this[S];
B=E.style.display;
if(B===""||B==="none"){E.style.display=x.data(E,"olddisplay")||""
}}return this
}},hide:function(E,B,P){if(E||E===0){return this.animate(AK("hide",3),E,B,P)
}else{E=0;
for(B=this.length;
E<B;
E++){P=x.css(this[E],"display");
P!=="none"&&x.data(this[E],"olddisplay",P)
}for(E=0;
E<B;
E++){this[E].style.display="none"
}return this
}},_toggle:x.fn.toggle,toggle:function(E,B,S){var P=typeof E==="boolean";
if(x.isFunction(E)&&x.isFunction(B)){this._toggle.apply(this,arguments)
}else{E==null||P?this.each(function(){var T=P?E:x(this).is(":hidden");
x(this)[T?"show":"hide"]()
}):this.animate(AK("toggle",3),E,B,S)
}return this
},fadeTo:function(E,B,S,P){return this.filter(":hidden").css("opacity",0).show().end().animate({opacity:B},E,S,P)
},animate:function(E,B,T,S){var P=x.speed(B,T,S);
if(x.isEmptyObject(E)){return this.each(P.complete)
}return this[P.queue===false?"each":"queue"](function(){var X=x.extend({},P),V,W=this.nodeType===1,Z=W&&x(this).is(":hidden"),U=this;
for(V in E){var Y=x.camelCase(V);
if(V!==Y){E[Y]=E[V];
delete E[V];
V=Y
}if(E[V]==="hide"&&Z||E[V]==="show"&&!Z){return X.complete.call(this)
}if(W&&(V==="height"||V==="width")){X.overflow=[this.style.overflow,this.style.overflowX,this.style.overflowY];
if(x.css(this,"display")==="inline"&&x.css(this,"float")==="none"){if(x.support.inlineBlockNeedsLayout){if(M(this.nodeName)==="inline"){this.style.display="inline-block"
}else{this.style.display="inline";
this.style.zoom=1
}}else{this.style.display="inline-block"
}}}if(x.isArray(E[V])){(X.specialEasing=X.specialEasing||{})[V]=E[V][1];
E[V]=E[V][0]
}}if(X.overflow!=null){this.style.overflow="hidden"
}X.curAnim=x.extend({},E);
x.each(E,function(t,A3){var A0=new x.fx(U,X,t);
if(k.test(A3)){A0[A3==="toggle"?Z?"show":"hide":A3](E)
}else{var Aa=O.exec(A3),A1=A0.cur()||0;
if(Aa){var c=parseFloat(Aa[2]),A2=Aa[3]||"px";
if(A2!=="px"){x.style(U,t,(c||1)+A2);
A1=(c||1)/A0.cur()*A1;
x.style(U,t,A1+A2)
}if(Aa[1]){c=(Aa[1]==="-="?-1:1)*c+A1
}A0.custom(A1,c,A2)
}else{A0.custom(A1,A3,"")
}}});
return true
})
},stop:function(E,B){var P=x.timers;
E&&this.queue([]);
this.each(function(){for(var S=P.length-1;
S>=0;
S--){if(P[S].elem===this){B&&P[S](true);
P.splice(S,1)
}}});
B||this.dequeue();
return this
}});
x.each({slideDown:AK("show",1),slideUp:AK("hide",1),slideToggle:AK("toggle",1),fadeIn:{opacity:"show"},fadeOut:{opacity:"hide"},fadeToggle:{opacity:"toggle"}},function(E,B){x.fn[E]=function(T,S,P){return this.animate(B,T,S,P)
}
});
x.extend({speed:function(E,B,S){var P=E&&typeof E==="object"?x.extend({},E):{complete:S||!S&&B||x.isFunction(E)&&E,duration:E,easing:S&&B||B&&!x.isFunction(B)&&B};
P.duration=x.fx.off?0:typeof P.duration==="number"?P.duration:P.duration in x.fx.speeds?x.fx.speeds[P.duration]:x.fx.speeds._default;
P.old=P.complete;
P.complete=function(){P.queue!==false&&x(this).dequeue();
x.isFunction(P.old)&&P.old.call(this)
};
return P
},easing:{linear:function(E,B,S,P){return S+P*E
},swing:function(E,B,S,P){return(-Math.cos(E*Math.PI)/2+0.5)*P+S
}},timers:[],fx:function(E,B,P){this.options=B;
this.elem=E;
this.prop=P;
if(!B.orig){B.orig={}
}}});
x.fx.prototype={update:function(){this.options.step&&this.options.step.call(this.elem,this.now,this);
(x.fx.step[this.prop]||x.fx.step._default)(this)
},cur:function(){if(this.elem[this.prop]!=null&&(!this.elem.style||this.elem.style[this.prop]==null)){return this.elem[this.prop]
}var B=parseFloat(x.css(this.elem,this.prop));
return B&&B>-10000?B:0
},custom:function(E,B,U){function T(V){return S.step(V)
}var S=this,P=x.fx;
this.startTime=x.now();
this.start=E;
this.end=B;
this.unit=U||this.unit||"px";
this.now=this.start;
this.pos=this.state=0;
T.elem=this.elem;
if(T()&&x.timers.push(T)&&!AN){AN=setInterval(P.tick,P.interval)
}},show:function(){this.options.orig[this.prop]=x.style(this.elem,this.prop);
this.options.show=true;
this.custom(this.prop==="width"||this.prop==="height"?1:0,this.cur());
x(this.elem).show()
},hide:function(){this.options.orig[this.prop]=x.style(this.elem,this.prop);
this.options.hide=true;
this.custom(this.cur(),0)
},step:function(P){var B=x.now(),V=true;
if(P||B>=this.options.duration+this.startTime){this.now=this.end;
this.pos=this.state=1;
this.update();
this.options.curAnim[this.prop]=true;
for(var U in this.options.curAnim){if(this.options.curAnim[U]!==true){V=false
}}if(V){if(this.options.overflow!=null&&!x.support.shrinkWrapBlocks){var T=this.elem,S=this.options;
x.each(["","X","Y"],function(W,X){T.style["overflow"+X]=S.overflow[W]
})
}this.options.hide&&x(this.elem).hide();
if(this.options.hide||this.options.show){for(var E in this.options.curAnim){x.style(this.elem,E,this.options.orig[E])
}}this.options.complete.call(this.elem)
}return false
}else{P=B-this.startTime;
this.state=P/this.options.duration;
B=this.options.easing||(x.easing.swing?"swing":"linear");
this.pos=x.easing[this.options.specialEasing&&this.options.specialEasing[this.prop]||B](this.state,P,0,1,this.options.duration);
this.now=this.start+(this.end-this.start)*this.pos;
this.update()
}return true
}};
x.extend(x.fx,{tick:function(){for(var E=x.timers,B=0;
B<E.length;
B++){E[B]()||E.splice(B--,1)
}E.length||x.fx.stop()
},interval:13,stop:function(){clearInterval(AN);
AN=null
},speeds:{slow:600,fast:200,_default:400},step:{opacity:function(B){x.style(B.elem,"opacity",B.now)
},_default:function(B){if(B.elem.style&&B.elem.style[B.prop]!=null){B.elem.style[B.prop]=(B.prop==="width"||B.prop==="height"?Math.max(0,B.now):B.now)+B.unit
}else{B.elem[B.prop]=B.now
}}}});
if(x.expr&&x.expr.filters){x.expr.filters.animated=function(B){return x.grep(x.timers,function(E){return B===E.elem
}).length
}
}var A=/^t(?:able|d|h)$/i,AU=/^(?:body|html)$/i;
x.fn.offset="getBoundingClientRect" in o.documentElement?function(E){var B=this[0],U;
if(E){return this.each(function(V){x.offset.setOffset(this,E,V)
})
}if(!B||!B.ownerDocument){return null
}if(B===B.ownerDocument.body){return x.offset.bodyOffset(B)
}try{U=B.getBoundingClientRect()
}catch(T){}var S=B.ownerDocument,P=S.documentElement;
if(!U||!x.contains(P,B)){return U||{top:0,left:0}
}B=S.body;
S=As(S);
return{top:U.top+(S.pageYOffset||x.support.boxModel&&P.scrollTop||B.scrollTop)-(P.clientTop||B.clientTop||0),left:U.left+(S.pageXOffset||x.support.boxModel&&P.scrollLeft||B.scrollLeft)-(P.clientLeft||B.clientLeft||0)}
}:function(X){var W=this[0];
if(X){return this.each(function(Y){x.offset.setOffset(this,X,Y)
})
}if(!W||!W.ownerDocument){return null
}if(W===W.ownerDocument.body){return x.offset.bodyOffset(W)
}x.offset.initialize();
var V,U=W.offsetParent,T=W.ownerDocument,S=T.documentElement,E=T.body;
V=(T=T.defaultView)?T.getComputedStyle(W,null):W.currentStyle;
for(var P=W.offsetTop,B=W.offsetLeft;
(W=W.parentNode)&&W!==E&&W!==S;
){if(x.offset.supportsFixedPosition&&V.position==="fixed"){break
}V=T?T.getComputedStyle(W,null):W.currentStyle;
P-=W.scrollTop;
B-=W.scrollLeft;
if(W===U){P+=W.offsetTop;
B+=W.offsetLeft;
if(x.offset.doesNotAddBorder&&!(x.offset.doesAddBorderForTableAndCells&&A.test(W.nodeName))){P+=parseFloat(V.borderTopWidth)||0;
B+=parseFloat(V.borderLeftWidth)||0
}U=W.offsetParent
}if(x.offset.subtractsBorderForOverflowNotVisible&&V.overflow!=="visible"){P+=parseFloat(V.borderTopWidth)||0;
B+=parseFloat(V.borderLeftWidth)||0
}V=V
}if(V.position==="relative"||V.position==="static"){P+=E.offsetTop;
B+=E.offsetLeft
}if(x.offset.supportsFixedPosition&&V.position==="fixed"){P+=Math.max(S.scrollTop,E.scrollTop);
B+=Math.max(S.scrollLeft,E.scrollLeft)
}return{top:P,left:B}
};
x.offset={initialize:function(){var E=o.body,B=o.createElement("div"),U,T,S,P=parseFloat(x.css(E,"marginTop"))||0;
x.extend(B.style,{position:"absolute",top:0,left:0,margin:0,border:0,width:"1px",height:"1px",visibility:"hidden"});
B.innerHTML="<div style='position:absolute;top:0;left:0;margin:0;border:5px solid #000;padding:0;width:1px;height:1px;'><div></div></div><table style='position:absolute;top:0;left:0;margin:0;border:5px solid #000;padding:0;width:1px;height:1px;' cellpadding='0' cellspacing='0'><tr><td></td></tr></table>";
E.insertBefore(B,E.firstChild);
U=B.firstChild;
T=U.firstChild;
S=U.nextSibling.firstChild.firstChild;
this.doesNotAddBorder=T.offsetTop!==5;
this.doesAddBorderForTableAndCells=S.offsetTop===5;
T.style.position="fixed";
T.style.top="20px";
this.supportsFixedPosition=T.offsetTop===20||T.offsetTop===15;
T.style.position=T.style.top="";
U.style.overflow="hidden";
U.style.position="relative";
this.subtractsBorderForOverflowNotVisible=T.offsetTop===-5;
this.doesNotIncludeMarginInBodyOffset=E.offsetTop!==P;
E.removeChild(B);
x.offset.initialize=x.noop
},bodyOffset:function(E){var B=E.offsetTop,P=E.offsetLeft;
x.offset.initialize();
if(x.offset.doesNotIncludeMarginInBodyOffset){B+=parseFloat(x.css(E,"marginTop"))||0;
P+=parseFloat(x.css(E,"marginLeft"))||0
}return{top:B,left:P}
},setOffset:function(Y,W,V){var U=x.css(Y,"position");
if(U==="static"){Y.style.position="relative"
}var T=x(Y),S=T.offset(),E=x.css(Y,"top"),P=x.css(Y,"left"),B=U==="absolute"&&x.inArray("auto",[E,P])>-1;
U={};
var X={};
if(B){X=T.position()
}E=B?X.top:parseInt(E,10)||0;
P=B?X.left:parseInt(P,10)||0;
if(x.isFunction(W)){W=W.call(Y,V,S)
}if(W.top!=null){U.top=W.top-S.top+E
}if(W.left!=null){U.left=W.left-S.left+P
}"using" in W?W.using.call(Y,U):T.css(U)
}};
x.fn.extend({position:function(){if(!this[0]){return null
}var E=this[0],B=this.offsetParent(),S=this.offset(),P=AU.test(B[0].nodeName)?{top:0,left:0}:B.offset();
S.top-=parseFloat(x.css(E,"marginTop"))||0;
S.left-=parseFloat(x.css(E,"marginLeft"))||0;
P.top+=parseFloat(x.css(B[0],"borderTopWidth"))||0;
P.left+=parseFloat(x.css(B[0],"borderLeftWidth"))||0;
return{top:S.top-P.top,left:S.left-P.left}
},offsetParent:function(){return this.map(function(){for(var B=this.offsetParent||o.body;
B&&!AU.test(B.nodeName)&&x.css(B,"position")==="static";
){B=B.offsetParent
}return B
})
}});
x.each(["Left","Top"],function(E,B){var P="scroll"+B;
x.fn[P]=function(U){var T=this[0],S;
if(!T){return null
}if(U!==AW){return this.each(function(){if(S=As(this)){S.scrollTo(!E?U:x(S).scrollLeft(),E?U:x(S).scrollTop())
}else{this[P]=U
}})
}else{return(S=As(T))?"pageXOffset" in S?S[E?"pageYOffset":"pageXOffset"]:x.support.boxModel&&S.document.documentElement[P]||S.document.body[P]:T[P]
}}
});
x.each(["Height","Width"],function(E,B){var P=B.toLowerCase();
x.fn["inner"+B]=function(){return this[0]?parseFloat(x.css(this[0],P,"padding")):null
};
x.fn["outer"+B]=function(S){return this[0]?parseFloat(x.css(this[0],P,S?"margin":"border")):null
};
x.fn[P]=function(U){var T=this[0];
if(!T){return U==null?null:this
}if(x.isFunction(U)){return this.each(function(V){var W=x(this);
W[P](U.call(this,V,W[P]()))
})
}if(x.isWindow(T)){return T.document.compatMode==="CSS1Compat"&&T.document.documentElement["client"+B]||T.document.body["client"+B]
}else{if(T.nodeType===9){return Math.max(T.documentElement["client"+B],T.body["scroll"+B],T.documentElement["scroll"+B],T.body["offset"+B],T.documentElement["offset"+B])
}else{if(U===AW){T=x.css(T,P);
var S=parseFloat(T);
return x.isNaN(S)?T:S
}else{return this.css(P,typeof U==="string"?U:U+"px")
}}}}
})
})(window);
/*
 * Cookie plugin
 *
 * Copyright (c) 2006 Klaus Hartl (stilbuero.de)
 * Dual licensed under the MIT and GPL licenses:
 * http://www.opensource.org/licenses/mit-license.php
 * http://www.gnu.org/licenses/gpl.html
 *
 */
jQuery.cookie=function(B,I,L){if(typeof I!="undefined"){L=L||{};
if(I===null){I="";
L=$.extend({},L);
L.expires=-1
}var E="";
if(L.expires&&(typeof L.expires=="number"||L.expires.toUTCString)){var F;
if(typeof L.expires=="number"){F=new Date();
F.setTime(F.getTime()+(L.expires*24*60*60*1000))
}else{F=L.expires
}E="; expires="+F.toUTCString()
}var K=L.path?"; path="+(L.path):"";
var G=L.domain?"; domain="+(L.domain):"";
var A=L.secure?"; secure":"";
document.cookie=[B,"=",encodeURIComponent(I),E,K,G,A].join("")
}else{var D=null;
if(document.cookie&&document.cookie!=""){var J=document.cookie.split(";");
for(var H=0;
H<J.length;
H++){var C=jQuery.trim(J[H]);
if(C.substring(0,B.length+1)==(B+"=")){D=decodeURIComponent(C.substring(B.length+1));
break
}}}return D
}};
var enquire={config:{imgs:"/2008/site/images/"},init:function(){var F=enquire;
appliedMedia=false;
var C=document.getElementsByTagName("style")[0];
jQuery(document).ready(function(G){G("body").addClass("w3c_javascript");
if(G.browser.msie){G("style").attr("media","screen");
if(G.browser.version<=6){G("head").append("<link rel='stylesheet' href='/2008/site/css/IE6.css' />")
}}if(G("body.w3c_home").length){G.getJSON("/2008/site/js/lang/strings.js",function(H){G("select[name=region] option").eq(0).attr("lang",H.lang).text(H.region)
})
}G("#w3c_nav div.w3c_sec_nav").append('<ul class="secondary_nav"><li class="label">Views: </li><li><a href="#" class="desktop" title="All Features Shown - Desktop Style">desktop</a></li><li><a href="#" class="mobile" title="Single Column View - Mobile Style">mobile</a></li><li><a href="#" class="print" title="Wide View - Hide Navigation Bars - Print Style">print</a></li></ul>');
G("#w3c_nav ul.secondary_nav a.mobile").click(function(){C.parentNode.removeChild(C);
D("handheld");
return false
});
G("#w3c_nav ul.secondary_nav a.print").click(function(){D("print");
return false
});
G("#w3c_nav ul.secondary_nav a.desktop").click(function(){if(!document.getElementsByTagName("style")[0]){var H=document.getElementsByTagName("head")[0];
H.appendChild(C);
location.reload();
D("screen")
}return false
});
G("link[rel=stylesheet], style").each(function(){G(this).data("media",this.media)
});
E();
recordedMedia=G.cookie("w3c_style");
if(recordedMedia){D(recordedMedia)
}});
function B(G){$("#w3c_nav ul.secondary_nav a").each(function(){this.href="#"
});
if(G=="handheld"){$("body").addClass("w3c_handheld");
$("body").removeClass("w3c_print");
$("body").removeClass("w3c_screen");
$("#w3c_nav ul.secondary_nav a.mobile").removeAttr("href")
}else{if(G=="print"){$("body").addClass("w3c_print");
$("body").removeClass("w3c_handheld");
$("body").removeClass("w3c_screen");
$("#w3c_nav ul.secondary_nav a.print").removeAttr("href")
}else{$("body").addClass("w3c_screen");
$("body").removeClass("w3c_handheld");
$("body").removeClass("w3c_print");
$("#w3c_nav ul.secondary_nav a.desktop").removeAttr("href")
}}F.tree.init("expand_section",G);
F.toggableInclude.init(G)
}function E(){if($("#w3c_mast h1 a img").css("display")=="none"){if(!appliedMedia||appliedMedia!="screen"){B("screen");
appliedMedia="screen"
}}else{if(!appliedMedia||appliedMedia!="handheld"){B("handheld");
appliedMedia="handheld"
}}}var A=null;
$(window).bind("resize",function(){if(A){clearTimeout(A)
}A=setTimeout(E,100)
});
function D(G){B(G);
$("link[rel=stylesheet], style").each(function(){if($(this).data("media").indexOf(G)>=0||$(this).data("media").indexOf("all")>=0){this.media=="all";
this.disabled=false
}else{this.media=$(this).data("media");
if(this.media!=="print"){this.disabled=true
}}});
recordedMedia=$.cookie("w3c_style");
if(recordedMedia!=G){$.cookie("w3c_style",G,{path:"/"})
}}},tree:{init:function(D,B){var C=enquire;
var A=C.tree;
$(".hierarchy  ."+D).each(function(){if((B!="print")&&(B!="handheld")){if($(this).hasClass("closed")&&$(this).parents("div.expand_block, li.expand_block")){$(this).parents("div.expand_block,  li.expand_block").addClass("closed")
}var F=$(this).parents("div.expand_block, li.expand_block").find("*[id]").eq(0).attr("id");
var E=(F?F:"");
if(!$("img",$(this)).length){if($(this).parents("div.expand_block, li.expand_block").hasClass("closed")){$(this).prepend("<img src='/2008/site/images/ico-plus' width='9' height='9' alt='Expand' />")
}else{$(this).prepend("<img src='/2008/site/images/ico-minus' width='9' height='9' alt='Collapse' />")
}$(this).wrapInner("<a href='#"+E+"'></a>")
}if(!$(this).data("expand_configured")){$(this).click(function(G){var I=$("a[href]",this).eq(0).attr("href");
var H=I.substring(I.indexOf("#"));
if(H!=="#"){window.location.hash=H
}G.preventDefault();
A.toggle($(this));
if($(this).parents("div.expand_block, li.expand_block").hasClass("closed")){$("img",$(this)).attr({src:"/2008/site/images/ico-plus",alt:"Expand"})
}else{$("img",$(this)).attr({src:"/2008/site/images/ico-minus",alt:"Collapse"})
}})
}$(this).data("expand_configured",true)
}else{$(this).text($(this).text())
}})
},toggle:function(A){$(A).parents("div.expand_block,  li.expand_block").toggleClass("closed")
}},toggableInclude:{init:function(C){if((C!="print")&&(C!="handheld")&&$("#w3c_toggle_include")){var A=($("#w3c_toggle_include").hasClass("default_open")||window.location.hash=="#w3c_all");
if(!$("#w3c_toggle_include_show").length){$("#w3c_toggle_include").after("<form class='tMargin lMargin w3c_toggle_form' action=''><div class='noprint'><fieldset><input type='radio' id='w3c_toggle_include_show' name='w3c_toggle_include_radio'"+((A)?" checked='checked'":"")+"/> <label for='w3c_toggle_include_show'>Show details</label> <input type='radio' id='w3c_toggle_include_hide' name='w3c_toggle_include_radio'"+((!A)?" checked='checked'":"")+"/> <label for='w3c_toggle_include_hide'>Hide details</label></fieldset></div></form>")
}$("#w3c_toggle_include_hide").change(function(){$("p.expand_description, div.expand_description, ul.expand_description").parent().addClass("closed");
$(".expand_section img").attr({src:"/2008/site/images/ico-plus",alt:"Expand"});
if(window.location.hash=="#w3c_all"){window.location.hash=""
}});
$("#w3c_toggle_include_show").change(function(){$("p.expand_description, div.expand_description, ul.expand_description").parent().removeClass("closed");
$(".expand_section img").attr({src:"/2008/site/images/ico-minus",alt:"Collapse"});
if(!window.location.hash){window.location.hash="#w3c_all"
}});
if(!A){$("#w3c_toggle_include_hide").change()
}else{$("#w3c_toggle_include_show").change()
}var B=window.location.hash;
if(B&&B!=="#"&&$(B,".expand_block").length){$(B,".expand_block").parents(".expand_block").removeClass("closed")
}}else{$("#w3c_toggle_include").next("form:has(#w3c_toggle_include_show)").replaceWith("")
}}}};
enquire.init();