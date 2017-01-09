
	var vd;
	var vd1;	
	var timeDrag = false;   /* Drag status */	
	var updatebar;
    var progress;

    function btnPlay() {

       	vd.play();

    }		

    function btnPause() {

       	vd.pause();

    }	

    function btnIncrease() {

       	if (vd.volume + 0.1 <= 1)
       		vd.volume+=0.1;

    }    

    function btnDecrease() {

       	if (vd.volume - 0.1 >= 0)
       		vd.volume-=0.1;

    }  
    
    function btnFullScreen() {

       	
       	vd.webkitEnterFullscreen();
       	vd.mozRequestFullScreen();

    }   
    
    $(function() {
    	vd = $('#video1')[0];    	
    	vd1 = $('#video1');    
    	
/*      //get HTML5 video time duration
	    vd1.on('loadedmetadata', function() {
	        $('.duration').text(vd.duration.toFixed(2));
	    }); */
	     
	    //update HTML5 video current play time
	    vd1.on('timeupdate', function() {
	        //$('.current').text(vd.currentTime.toFixed(2));
	        var currentPos = vd.currentTime; 
	        var maxduration = vd.duration; 
	        var percentage = 100 * currentPos / maxduration; 
	        //percentage = percentage.toFixed(2);
	        $('.timeBar').css('width', percentage+'%');	        
	    });   

	    $('.progressBar').mousedown(function(e) {
	        timeDrag = true;
	        updatebar(e.pageX);
	    });
	    $(document).mouseup(function(e) {
	        if(timeDrag) {
	            timeDrag = false;
	            updatebar(e.pageX);
	        }
	    });
	    $(document).mousemove(function(e) {
	        if(timeDrag) {
	            updatebar(e.pageX);
	        }
	    });

	  	//update Progress Bar control
	    updatebar = function(x) {
	        progress = $('.progressBar');
	        var maxduration = vd.duration; 
	        var position = x - progress.offset().left; 
	        var percentage = 100 * position / progress.width();
	     
	        //Check within range
	        if(percentage > 100) {
	            percentage = 100;
	        }
	        if(percentage < 0) {
	            percentage = 0;
	        }
	     
	        //Update progress bar and video currenttime
	        $('.timeBar').css('width', percentage+'%');
	        var i = maxduration * percentage / 100;
	        //console.log(vd.currentTime + " - "  + position + " - " + i);
	        vd.pause();
	        vd.currentTime = i;
	        vd.play();

        
	    };	    
	    
    });    

	