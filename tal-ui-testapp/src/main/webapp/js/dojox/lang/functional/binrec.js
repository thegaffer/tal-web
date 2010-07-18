/*
	Copyright (c) 2004-2009, The Dojo Foundation All Rights Reserved.
	Available via Academic Free License >= 2.1 OR the modified BSD license.
	see: http://dojotoolkit.org/license for details
*/


if(!dojo._hasResource["dojox.lang.functional.binrec"]){
dojo._hasResource["dojox.lang.functional.binrec"]=true;
dojo.provide("dojox.lang.functional.binrec");
dojo.require("dojox.lang.functional.lambda");
dojo.require("dojox.lang.functional.util");
(function(){
var df=dojox.lang.functional,_1=df.inlineLambda,_2="_x",_3=["_z.r","_r","_z.a"];
df.binrec=function(_4,_5,_6,_7){
var c,t,b,a,cs,ts,bs,as,_8={},_9={},_a=function(x){
_8[x]=1;
};
if(typeof _4=="string"){
cs=_1(_4,_2,_a);
}else{
c=df.lambda(_4);
cs="_c.apply(this, _x)";
_9["_c=_t.c"]=1;
}
if(typeof _5=="string"){
ts=_1(_5,_2,_a);
}else{
t=df.lambda(_5);
ts="_t.apply(this, _x)";
}
if(typeof _6=="string"){
bs=_1(_6,_2,_a);
}else{
b=df.lambda(_6);
bs="_b.apply(this, _x)";
_9["_b=_t.b"]=1;
}
if(typeof _7=="string"){
as=_1(_7,_3,_a);
}else{
a=df.lambda(_7);
as="_a.call(this, _z.r, _r, _z.a)";
_9["_a=_t.a"]=1;
}
var _b=df.keys(_8),_c=df.keys(_9),f=new Function([],"var _x=arguments,_y,_z,_r".concat(_b.length?","+_b.join(","):"",_c.length?",_t=_x.callee,"+_c.join(","):"",t?(_c.length?",_t=_t.t":"_t=_x.callee.t"):"",";while(!",cs,"){_r=",bs,";_y={p:_y,a:_r[1]};_z={p:_z,a:_x};_x=_r[0]}for(;;){do{_r=",ts,";if(!_z)return _r;while(\"r\" in _z){_r=",as,";if(!(_z=_z.p))return _r}_z.r=_r;_x=_y.a;_y=_y.p}while(",cs,");do{_r=",bs,";_y={p:_y,a:_r[1]};_z={p:_z,a:_x};_x=_r[0]}while(!",cs,")}"));
if(c){
f.c=c;
}
if(t){
f.t=t;
}
if(b){
f.b=b;
}
if(a){
f.a=a;
}
return f;
};
})();
}
