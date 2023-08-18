$('.pin').on("click", function() {
	if ( $(this).hasClass('is-opened') ) {
		$(this).toggleClass('is-opened');
		return;
	}
	
	$('.pin').each(function() {
		$(this).removeClass('is-opened');
	});
	$(this).toggleClass('is-opened');
})