<link rel="stylesheet" href="/images/BusinessInnovation/tabsgradation.css">

<ul class="demo-btns" style="">
<li>
<a href="<@ofbizUrl>findFulfillments</@ofbizUrl>" id="SEARCH" class="btn btn-labeled btn-primary">
<span class="btn-label"><i class="fa fa-search"></i></span>${uiLabelMap.Search}</a>
</li>
<li>
<a href="<@ofbizUrl>HiringEmployee</@ofbizUrl>" id="HIRING" class="btn btn-labeled btn-primary">
<span class="btn-label"><i class="fa fa-pencil"></i></span>${uiLabelMap.Hiring}</a>
</li>
<li>
<a href="<@ofbizUrl>BonusEmployee</@ofbizUrl>" id="BONUS" class="btn btn-labeled btn-primary">
<span class="btn-label"><i class="fa fa-money"></i></span>${uiLabelMap.Bonus}</a>
</li>
<li>
<a href="<@ofbizUrl>PromotionEmployee</@ofbizUrl>" id="PROMOTION" class="btn btn-labeled btn-primary">
<span class="btn-label"><i class="fa fa-user-plus"></i></span>${uiLabelMap.Promotion}</a>
</li>
<li>
<a href="<@ofbizUrl>MoveEmployee</@ofbizUrl>" id="MOVING" class="btn btn-labeled btn-primary">
<span class="btn-label"><i class="fa fa-exchange"></i></span>${uiLabelMap.Move}</a>
</li>
<li>
<a href="<@ofbizUrl>TerminateEmployee</@ofbizUrl>" id="TERMINATE" class="btn btn-labeled btn-primary">
<span class="btn-label"><i class="fa fa-hand-paper-o"></i></span>${uiLabelMap.Terminate}</a>
</li>
<li>
<a href="<@ofbizUrl>HoldEmployee</@ofbizUrl>" id="HOLD" class="btn btn-labeled btn-primary">
<span class="btn-label"><i class="fa fa-pause"></i></span>${uiLabelMap.Hold}</a>
</li>
</ul>
<script>
if("${action}"=="SEARCH"){
 $("#${action}").addClass("selceted");}
else if("${action}"=="HIRING"){
 $("#${action}").addClass("selceted");}
else if("${action}"=="BONUS"){
 $("#${action}").addClass("selceted");}
else if("${action}"=="PROMOTION"){
 $("#${action}").addClass("selceted");}
else if("${action}"=="MOVING"){
 $("#${action}").addClass("selceted");}
else if("${action}"=="TERMINATE"){
 $("#${action}").addClass("selceted");}
else if("${action}"=="HOLD"){
 $("#${action}").addClass("selceted");}
</script>
<script>

;( function( window ) {
	
	'use strict';

	function extend( a, b ) {
		for( var key in b ) { 
			if( b.hasOwnProperty( key ) ) {
				a[key] = b[key];
			}
		}
		return a;
	}

	function CBPFWTabs( el, options ) {
		this.el = el;
		this.options = extend( {}, this.options );
  		extend( this.options, options );
  		this._init();
	}

	CBPFWTabs.prototype.options = {
		start : 0
	};

	CBPFWTabs.prototype._init = function() {
		// tabs elems
		this.tabs = [].slice.call( this.el.querySelectorAll( 'nav > ul > li' ) );
		// content items
		this.items = [].slice.call( this.el.querySelectorAll( '.content-wrap > section' ) );
		// current index
		this.current = -1;
		// show current content item
		this._show();
		// init events
		this._initEvents();
	};

	CBPFWTabs.prototype._initEvents = function() {
		var self = this;
		this.tabs.forEach( function( tab, idx ) {
			tab.addEventListener( 'click', function( ev ) {
				ev.preventDefault();
				self._show( idx );
			} );
		} );
	};

	CBPFWTabs.prototype._show = function( idx ) {
		if( this.current >= 0 ) {
			this.tabs[ this.current ].className = this.items[ this.current ].className = '';
		}
		// change current
		this.current = idx != undefined ? idx : this.options.start >= 0 && this.options.start < this.items.length ? this.options.start : 0;
		this.tabs[ this.current ].className = 'tab-current';
		this.items[ this.current ].className = 'content-current';
	};

	// add to global namespace
	window.CBPFWTabs = CBPFWTabs;

})( window );</script>
		<script>
			(function() {

				[].slice.call( document.querySelectorAll( '.tabs' ) ).forEach( function( el ) {
					new CBPFWTabs( el );
				});

			})();




		</script>
<script>

$("#ForFillTabs li").click(function() {
  localStorage.setItem("tab-current", $(this).index());

});
var currentTab = localStorage.getItem("tab-current");
$('ul#ForFillTabs li:eq('+currentTab+')').addClass("tab-current");
$('ul#ForFillTabs li:eq('+currentTab+')').siblings().removeClass("tab-current");


</script>
<script>
        <#include "component://humanres/widget/forms/postionGradation/js/PositionGradationMain.js"/>

</script>
