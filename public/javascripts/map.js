console.log("map.js loaded");


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

function load2(selectedvalue) {
	console.log(selectedvalue);
	

}

function load(selectedvalue,username) {
	console.log(selectedvalue);
	fetch("http://localhost:9000/cs", { 
		  method: 'POST',
		  headers: {'Content-Type': 'application/json'},
		  body: JSON.stringify({"selectedvalue": selectedvalue,"username":username})
	  });
  }



