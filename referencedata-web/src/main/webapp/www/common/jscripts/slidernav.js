/*
 *  SliderNav - A Simple Content Slider with a Navigation Bar
 *  Copyright 2015 Monji Dolon, http://mdolon.com/
 *  Released under the MIT, BSD, and GPL Licenses.
 *  More information: http://devgrow.com/slidernav
 */
ai_jquery.fn.sliderNav = function(options) {
	var defaults = { items: ["A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"], debug: false, height: null, arrows: true, event: 'mouseover'};
	var opts = ai_jquery.extend(defaults, options); var o = ai_jquery.meta ? ai_jquery.extend({}, opts, ai_jqueryai_jquery.data()) : opts; var slider = ai_jquery(this); ai_jquery(slider).addClass('slider');
	ai_jquery('.slider-content li:first', slider).addClass('selected');
	ai_jquery(slider).append('<div id="sliderNav" class="slider-nav"><ul></ul></div>');
	for(var i = 0; i < o.items.length; ++i) ai_jquery('.slider-nav ul', slider).append("<li><a alt='#"+o.items[i]+"'>"+o.items[i]+"</a></li>");
	var height = ai_jquery('.slider-nav', slider).height();
	if(o.height) height = o.height;
	ai_jquery('.slider-content, .slider-nav', slider).css('height',height);
	if(o.debug) ai_jquery(slider).append('<div id="debug">Scroll Offset: <span>0</span></div>');
	
	ai_jquery('.slider-nav a', slider).on(opts.event, function(event){
		var target = ai_jquery(this).attr('alt');
		var cOffset = ai_jquery('.slider-content', slider).offset().top;
		var tOffset = ai_jquery('.slider-content '+target, slider).offset().top;
		var height = ai_jquery('.slider-nav', slider).height(); if(o.height) height = o.height;
		var pScroll = (tOffset - cOffset) - height/8;
		ai_jquery('.slider-content li', slider).removeClass('selected');
		ai_jquery(target).addClass('selected');
		ai_jquery('.slider-content', slider).stop().animate({scrollTop: '+=' + pScroll + 'px'});
		if(o.debug) ai_jquery('#debug span', slider).html(tOffset);
	});
	if(o.arrows){
		ai_jquery('.slider-nav',slider).css('top','20px');
		ai_jquery(slider).prepend('<div class="slide-up end"><span class="arrow up"></span></div>');
		ai_jquery(slider).append('<div class="slide-down"><span class="arrow down"></span></div>');
		ai_jquery('.slide-down',slider).click(function(){
			ai_jquery('.slider-content',slider).animate({scrollTop : "+="+height+"px"}, 500);
		});
		ai_jquery('.slide-up',slider).click(function(){
			ai_jquery('.slider-content',slider).animate({scrollTop : "-="+height+"px"}, 500);
		});
	}
};
