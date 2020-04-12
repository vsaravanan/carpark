function error(args) {

    if (args.errors) {

        var grid = $("#grid").data("kendoGrid");
        if (grid.editable)
        {

			grid.one("dataBinding", function (e) {
				e.preventDefault();   // cancel grid rebind if error occurs
				for (var error in args.errors) {
					showMessage(grid.editable.element, error, args.errors[error].errors);
				}
			}
			);
        }
    }
}


function showMessage(container, name, errors) {

    var validationMessageTmpl = kendo.template($("#message").html());

    var output = validationMessageTmpl({
      field: name,
      message: errors
    }).trim();

    container.find("[data-container-for='" + name + "']").append($(output));

  }

var idField;



$(function() {

	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$(document).ajaxSend(function(e, xhr, options) {
	    xhr.setRequestHeader(header, token);
	});

	window.ddMMMyyyy = "dd-MMM-yyyy";
	window.ddMMMyyyyHHmm = "dd-MMM-yyyy HH:mm";
	// requirements notification, popupNotification, staticNotification, appendto



    function getStringFromErrors(Errors) {
    	var myErrors = "";
		for (var error in Errors) {
			myErrors += "Error at " + error + " : " + Errors[error].errors + "\n";
		}
		return myErrors;
    }

    window.getStringFromErrors = getStringFromErrors;


    $("#btnPopup").kendoButton({
        click: function(e) {
            var checkedValue = $('#chkPopup').prop('checked');
            if (checkedValue)
            	$('#chkPopup').removeAttr('checked');
            else
            	$('#chkPopup').prop('checked','checked');
            checkedValue = $('#chkPopup').prop('checked');

            var grid = $("#grid").data("kendoGrid");

            grid.setOptions({
            	editable: checkedValue ? "popup" : "inline"
            });

        }
    });

    vehicleDeleteWindow = kendo.template($("#vehicleDeleteWindow").html());
    deleteWindow = kendo.template($("#deleteWindow").html());

    wnd = $("#del_window").kendoWindow({
        title: "row delete ?",
        modal: true,
        visible: false,
        width: "400px",
        height: "200px",
    }).data("kendoWindow");




    // row highlighted selection
    var selectedRows = [];

    window.selectedRows = selectedRows;
    window.idField = idField;


    function getObjects(obj, key, val) {
        var objects = [];
        for (var i in obj) {
            if (!obj.hasOwnProperty(i)) continue;
            if (typeof obj[i] === 'object') {
                objects = objects.concat(getObjects(obj[i], key, val));
            } else if (i === key && obj[key] === val) {
                objects.push(obj);
            }
        }
        return objects;
    }
    window.getObjects = getObjects;



    function onDataBound(e) {
        // row highlighted selection
        var grid = e.sender;
        var items = grid.items();
        var itemsToSelect = [];
        items.each(function (idx, row) {
          var dataItem = grid.dataItem(row);
          if (selectedRows[dataItem[idField]]) {
            itemsToSelect.push(row);
          }
        });
        e.sender.select(itemsToSelect);

    }

    window.onDataBound = onDataBound;

    notification = $("#notification").kendoNotification({
        position: {
            pinned: true,
            top: 30,
            right: 30
        },
        autoHideAfter: 3000,
        stacking: "down",
        templates: [{
            type: "error",
            template: $("#errorTemplate").html()
        }, {
            type: "upload-success",
            template: $("#successTemplate").html()
        }]

    }).data("kendoNotification");



    function errorMsg(message, title)
    {
    	if (typeof title === "undefined")
    		title = "Error";
        message = message.replace(/\n|\r\n|\r/g, '<br/>');
        notification.show({
            title: title,
            message: message
        }, "error");
        $("#formErr").val("");
    }
    window.errorMsg = errorMsg;

    function successMsg(message)
    {
        message = message.replace(/\n|\r\n|\r/g, '<br/>');
        notification.show({
            message: message
        }, "upload-success");
        $("#formMsg").val("");
    }
    window.successMsg = successMsg;


    function deleteItem(e) {
    	e.preventDefault();

		var dataItem = this.dataItem($(e.currentTarget).closest("tr"));

		wnd.content(deleteWindow(dataItem));
		wnd.open().center();

		$("#yesButton").click(function(){
		    var grid = $("#grid").data("kendoGrid");
		    grid.dataSource.remove(dataItem);
		    grid.dataSource.sync();
		    wnd.close();
		})

		$("#noButton").click(function(){
			wnd.close();
		})

    }

    window.deleteItem = deleteItem;

    var strFormMsg = $("#formMsg").val()

    if (!!strFormMsg)
        successMsg(strFormMsg);

    var strFormErr = $("#formErr").val()

    if (!!strFormErr)
        errorMsg(strFormErr);

    window.strFormMsg = strFormMsg;
    window.strFormErr = strFormErr;


	function startChange() {
	    var startDate = start.value();
	    $('#fromDate').val(kendo.toString(startDate,ddMMMyyyyHHmm));
	    if (startDate) {
	        startDate = new Date(startDate);
	        startDate.setDate(startDate.getDate());
	        end.min(startDate);
	        var endDate = end.value();
	        if (endDate && endDate <= startDate) {
	            endDate.setDate(startDate.getDate() + 1);
	            end.value(endDate);
	        }
	    }
	}

	function endChange() {
	    var endDate = end.value();
	    $('#toDate').val(kendo.toString(endDate,ddMMMyyyyHHmm));
	    if (endDate) {
	        endDate = new Date(endDate);
	        endDate.setDate(endDate.getDate());
	        start.max(endDate);
	    }
	}



    window.startChange = startChange;
    window.endChange = endChange;

    function writeException (res, textStatus) {

	     if (textStatus !== "success") {
	         var result = res.responseText;
	     	document.write(result);
	     	document.close();
	     }
	}

    window.writeException = writeException;

    function promptMsg (res) {

   		var msg = res.responseJSON.msg;
   		if (!! msg)
		{
			successMsg(msg);
			return;
		}
   		else
		{
   	   		var err = res.responseJSON.err;
   	   		if (!! err)
   	   			errorMsg(err);
   	   		return;
		}


	}

    window.promptMsg = promptMsg;

    var HashMap = (function(){
        // IE 8 and earlier has no Array.prototype.indexOf
        var keys = [];
        var values = [];

    	function indexOfPolyfill(val) {
          for (var i = 0, l = this.length; i < l; ++i) {
            if (this[i] === val) {
              return i;
            }
          }
          return -1;
        }


        function HashMap(){

          if (!keys.indexOf) {
            keys.indexOf = indexOfPolyfill;
          }
        };

        HashMap.prototype.has = function(key){
          return keys.indexOf(key) !== -1;
        };

        HashMap.prototype.get = function(key, defaultValue){
          var index = keys.indexOf(key);
          return index === -1 ? defaultValue : values[index];
        };

        HashMap.prototype.put = function(key, value){
          var index = keys.indexOf(key);
          if (index === -1) {
            keys.push(key);
            values.push(value);
          } else {
            var prevValue = values[index];
            values[index] = value;
            return prevValue;
          }
        };

        HashMap.prototype.remove = function(key){
          var index = keys.indexOf(key);
          if (index !== -1) {
            keys.splice(index, 1);
            return values.splice(index, 1)[0];
          }
        };

        HashMap.prototype.clear = function(){
          keys.splice(0, keys.length);
          values.splice(0, values.length);
        };

    	HashMap.prototype.keys = function(){
    		return keys;
    	};
    	HashMap.prototype.values = function(){
    		return values;
    	};

    	HashMap.prototype.hashmap = function() {

			var myMap = {};
			for (var i = 0, l = keys.length; i < l; ++i) {
				myMap[keys[i]] = values[i];
			}
			return myMap;
    		};

    	HashMap.prototype.clean = function() {

            var clnkeys = [];
            var clnvalues = [];

            for (var i = 0, l = values.length, j=0; i < l; ++i) {
            	if (values[i] !== "")
            	{
            		clnkeys[j] = keys[i];
            		clnvalues[j] = values[i];
            		j++;
            	}
            }
            keys = clnkeys;
            values = clnvalues;
            return this;

    	};

        return HashMap;
    })();
    window.HashMap = HashMap;

    var mapSelect = new HashMap();
    window.mapSelect =  mapSelect;

    function lpad(n, width, z) {
    	  z = z || ' ';
    	  n = n + '';
    	  return n.length >= width ? n : new Array(width - n.length + 1).join(z) + n;
    }
    window.lpad = lpad;

    function rpad(n, width, z) {
  	  z = z || ' ';
  	  n = n + '';
  	  return n.length >= width ? n : n + new Array(width - n.length + 1).join(z);
    }
    window.rpad = rpad;


    function isValidURL(url) {
        var encodedURL = encodeURIComponent(url);
        var isValid = false;

        $.ajax({
          url: "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20html%20where%20url%3D%22" + encodedURL + "%22&format=json",
          type: "get",
          async: false,
          dataType: "json",
          success: function(data) {
            isValid = data.query.results != null;
          },
          error: function(){
            isValid = false;
          }
        });

        return isValid;
      }
    
    window.isValidURL = isValidURL;    

//    $('[id^=describeMe]').click(function (e) {
//        //event.preventDefault();
//
//        var thisBtn = $(this);
//
//        var url =  thisBtn.data("alt");
//
//
//        //if (isValidURL(url) == false)
//        //	url =  thisBtn.data("alt");
//
//
//
//        //url =  url + "?&cc_load_policy=1&autoplay=1&rel=0";
//        var windowName = "_blank";
//        var windowSize = "location=yes, width=640, height=360, scrollbars=yes,status=yes";
//
//        window.open(url, windowName, windowSize);
//    });

    
    function onRequestEnd(e) {

    	if (e.type !== undefined && e.type !== "read")
    	{
			var returnMsg;
			if (e.response.Errors === undefined)
            	returnMsg = e.type + " is successful";
			else
				returnMsg = e.type + " is failed.\n" +
            				getStringFromErrors(e.response.Errors);
            kendoConsole.log(returnMsg);
            mapSelect.clear();
        }
    }

    window.onRequestEnd = onRequestEnd;    
    
 });

