<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kendo" uri="http://www.kendoui.com/jsp/tags"%>
<html>
    <head>
		<%@include file="csrf.jsp" %>
        <title>Generate Slots / Reserve / Park </title>


<script>
$(function() {
	document.body.style.zoom="90%";

    $("button").click(function(){
        setBtnName($(this));
    });


    function btnSearch() {

       	var grid = $('#grid').data('kendoGrid');
       	grid.dataSource.read();

    }

	function setBtnName (e) {
		mapSelect.clean();
        var btnClicked = e.text();
        $("#newStatus").val(btnClicked);

        if (btnClicked == "Search")
       	{
        	return btnSearch();
       	}


        var InvalidMoves = '{' +
                '"Inactive": ["Inactive", "Parked","Reserved"],    ' +
                '"Open": ["Open", "Parked"],    ' +
                '"Cancel": ["Open", "Parked"],    ' +
                '"Reserve": ["Inactive", "Reserved", "Parked"],    ' +
                '"Park": ["Inactive", "Parked"]    ' +
                '}';
        var im = JSON.parse(InvalidMoves);
        var errorFound = 0;
        im[btnClicked].forEach(function(val)
        {
            if (getObjects(mapSelect.values(),"status",val) != "")
            {
                errorMsg("One of the item <br>Changing status from " + val + " to " + btnClicked + " is INVALID. Action aborted");
                errorFound = -1;
                return;
            }
        });

        if (! errorFound)
        {
            var strSelected = mapSelect.keys().join();
            //kendoConsole.log(strSelected);
            $("#selectedRows").val(strSelected);

            var grid = $("#grid").data("kendoGrid");
            $("#skip").val(grid.dataSource.skip());
            $("#page").val(grid.dataSource.page());
            //kendoConsole.log("current skip : " + grid.dataSource.skip() + "  page " + grid.dataSource.page());

            $("form").submit();
        }


	}
});
</script>

    </head>
<body>



		<div id="mainDiv" align="center"  class="container-fluid carparkForm">
		<spring:url	 value="/"		var="root"	 />

				<form:form action= "${root}slotUsed/search" modelAttribute="search"   method="post" >

                    <div class="row-fluid" align="left" style="font-weight: bold;">
                        <div class="span2" >From date </div>
                        <div class="span2" >To date </div>
                        <div class="span4" >Slots</div>
                        <div class="span4" >User</div>
                    </div>


                    <div class="row-fluid" align="left">
                        <div class="span2" >
                            <input id="fromDate0"   />
                            <form:hidden path="fromDate" />
                        </div>
                        <div class="span2" >
                            <input id="toDate0"    />
                            <form:hidden path="toDate"/>
                         </div>

                        <div class="span4" >
                            <input id="slotId0"  />
                            <form:hidden path="slotId"/>
                        </div>
                        <div class="span4" >
                            <input id="userId0" class="input-block-level"  style="display:inline-block; width: 100%;" />
                            <form:hidden path="userId"/>
                            <form:hidden path="userName"/>
                        </div>
                    </div>
                    <div class="row-fluid" > <div class="span12" > </div></div>
                    <div class="row-fluid" row>
                        <div class="span6" ></div>
                        <div class="span6">
                            <div class="row-fluid">
                                <div class="span2">
                                    <button type="button" class="btn btn-block btn-primary" >Open</button>
                                </div>
                                <div class="span2">
                                    <button type="button" class="btn btn-block btn-info" >Reserve</button>
                                </div>
                                <div class="span2">
                                    <button type="button" class="btn btn-block btn-success" >Park</button>
                                </div>
                                <div class="span2">
                                    <button type="button" class="btn btn-block btn-warning" >Cancel</button>
                                </div>
                                <div class="span2">
                                    <button type="button" class="btn btn-block btn-danger" >Inactive</button>
                                </div>
                                <div class="span2">
                                    <button type="button" class="btn btn-block btn-Search" >Search</button>
                                </div>
                            </div>

                        </div>
                    </div>

                    <div class="row-fluid" > <div class="span12" > </div></div>
                    <div class="row-fluid" >
                        <div class="span12" >


                            <div id="grid"></div>
                            <div class="console" > </div>
                            <span id="notification"></span>
                            <div id="del_window"></div>

                            <form:hidden path="selectedRows"/>
                            <form:hidden path="newStatus" />
                            <form:hidden path="skip"/>
                            <form:hidden path="page"/>
                        </div>
                    </div>
                </form:form>
        </div>
<script>

$(function() {


	var start = $("#fromDate0").kendoDateTimePicker ({
		format : ddMMMyyyyHHmm,
	    change: startChange
	}).data("kendoDateTimePicker");

	var end = $("#toDate0").kendoDateTimePicker({
		format : ddMMMyyyyHHmm,
	    change: endChange
	}).data("kendoDateTimePicker");

	end.min(start.value());

	window.start = start;
	window.end = end;



        var crudServiceBaseUrl = "${root}slotUsed";
        var lookupUrl = "${root}lookup";


        var lstSlots = {
          	  animation: {
       		   close: {
       		     effects: "fadeOut zoom:out",
       		     duration: 300
       		   },
       		   open: {
       		     effects: "fadeIn zoom:in",
       		     duration: 300
       		   }
       		  },
           template: '#= location # - #= level # - #= slotNo #',
           filter: "contains",
           placeholder: "Select slot id ...",
           dataTextField: "slotId",
           dataValueField: "slotId",
           height: 1000,
           dataSource: {
               transport: {

                   read:  {
                       url: lookupUrl + "/listParkingSlot",
                       contentType: "application/json",
                       dataType: "json"
                   }
               }
           },
           change: function(e) {
               $('#slotId').val(this.value());

           }
       };


    	var lstSlotsFull = Object.create(lstSlots);
    	lstSlotsFull.template = '<span class="slotId">#= slotId #</span> #= location # &ensp;&ensp;|&ensp;&ensp; Level : #= level #   &ensp;&ensp;|&ensp;&ensp;   Park : #= slotNo #';
        $("#slotId0").kendoMultiSelect(lstSlotsFull);

        function ddlFilterSlot(element) {
            element.kendoDropDownList(lstSlots) ;
        }


	var lstUsers = {
            animation: {
                close: {
                    effects: "fadeOut zoom:out",
                    duration: 300
                },
                open: {
                    effects: "fadeIn zoom:in",
                    duration: 300
                }
            },
            template: '#= userName #  ',
            filter: "contains",
            dataTextField: "userName",
            dataValueField: "userId",

            height: 1000,
            dataSource: {
                transport: {

                    read:  {
                        url: lookupUrl + "/listUser",
                        contentType: "application/json",
                        dataType: "json"
                    }
                }
            },
            change: function(e) {
                $('#userId').val(this.value());
                $('#userName').val(this.text());

            }
        };

    function ddlFilterUserId(element) {
        element.kendoDropDownList(lstUsers);
    }

	var lstUsersFull = Object.create(lstUsers);
	lstUsersFull.optionLabel = "Select user id ..",
	lstUsersFull.template = '#= userName.padEnd(20,"~").replace(/~/gi,"&nbsp;") # &nbsp; #= address # '
    $("#userId0").kendoDropDownList(lstUsersFull);




        idField = "slotUsedId";

        var dataSource = new kendo.data.DataSource({
        		transport: {

                    read:  {
                        url: crudServiceBaseUrl + "/read",
                        contentType: "application/json",
                        type: "POST",
                        dataType: "json",
                        complete:  function (res, textStatus) {
                        	writeException  (res, textStatus);
                        	$("#newStatus").val("");
                            kendoConsole.log(res.responseJSON["message"]);
                        }
                    },
                    create : {
                        url: crudServiceBaseUrl + "/update",
                        contentType: "application/json",
                        type: "POST",
                        dataType: "json",
                            complete : function (res, textStatus) {

                                if (textStatus != "success") {
                                	writeException  (res, textStatus);
                                }
                                else
                               	{
		                           	var grid = $('#grid').data('kendoGrid');
		                           	grid.dataSource.read();
		                            grid.refresh();
                               	}
                            }

                    	},
                    update: {
                        url: crudServiceBaseUrl + "/update",
                        contentType: "application/json",
                        type: "POST",
                        dataType: "json",
                            complete: function (res, textStatus) {
                            	writeException  (res, textStatus);
                            }
                    },
                    destroy: {
                        url: crudServiceBaseUrl + "/destroy",
                        contentType: "application/json",
                        type: "POST",
                        dataType: "json",
                        complete: function (res, textStatus) {
                        	writeException  (res, textStatus);
                        }

                    },
                    parameterMap: function(options, operation) {
                        if (operation !== "read" ) {
                            var js = JSON.stringify(options.models);
                            //kendoConsole.log(js);
                            return JSON.stringify(options.models);
                        }
                        else
                        {
                        	if (!! options.filter)
	                        	$("#newStatus").val() == "Filter"

							var listFF = []

   							function FieldFilter(){
   								this.field = "";
   								this.where = "";
   							}

                         	if (!! $("#fromDate").val())
                       		{

    							var ff = new FieldFilter();
    							ff.field = "entryTime";
    							ff.where = "c.entryTime >= '" +  $("#fromDate").val() + "'";
    							listFF.push(ff);

                       		}


                           	if (!! $("#toDate").val())
                       		{

    							var ff = new FieldFilter();
    							ff.field = "exitTime";
    							ff.where = "c.exitTime < '" +  $("#toDate").val() + "'";
    							listFF.push(ff);

                       		}


                            if (!! $("#slotId").val())
                         	{

	   							var ff = new FieldFilter();
	   							ff.field = "slotId";
	   							ff.where = " s.slotId in (" +  $("#slotId").val() + " ) ";
	   							listFF.push(ff);

                         	}

                           	if (!! $("#userId").val())
                       		{

	   							var ff = new FieldFilter();
	   							ff.field = "userId";
	   							ff.where = " s.userId in (" +  $("#userId").val() + " ) ";
	   							listFF.push(ff);
                         	}

                           	if ($("#newStatus").val() == "Search")
                           		options.btnSearch = true;
                           	else
                           		options.btnSearch = false;



							options.fieldFilters = listFF;

                        	var js = JSON.stringify(options);
                            kendoConsole.log(js);

                            return js;

                        }
                    }


                },
                requestEnd : onRequestEnd,
                error : error,
                batch: true,
                pageSize: 5,
                serverPaging: true,
                serverFiltering: true,
                serverSorting: true,
                schema: {
                	total: "total",
                	data : "data",
                	errors: "Errors",

                    model: {
                        id: "slotUsedId",
                        fields: {
                            slotUsedId: { type: "number",  editable: false, nullable: true},
                            calendarId : {type: "number"},
                            calendarRow : {type: "string"},
                            slotId : {type: "number"},
                            parkingSlotRow : {type: "string"},
                            status : {type: "string"},
                            userId : {type: "number"},
                            userRow : {type: "string"},
                            parkingId : {type: "number"}
                        }
                    }
                }
            });



        var grid = $("#grid").kendoGrid({

            dataSource: dataSource,
            dataBound: onDataBound,
            change: onChange,
            filterMenuInit : onFilterMenuInit,
            //editable: true,

            filterable: {
                extra: false,
                //mode : "row"
                 operators: {
                    number: {
                        eq: "Is equal to",
                        neq: "Is not equal to"
                        //startswith: "Starts with",
                        //contains: "contains"
                    }
                }
            },
            pageable: true,
            selectable: "multiple row",
        	allowCopy: true,
            pageable: {pageSizes : [5, 10, 25, 50,"all"] },
            navigatable: true,
            scrollable: true,
            sortable: {
                mode: "multiple",
                allowUnsort: true
            },
            resizable: true,
            height: 400,

            columns: [

                { field: "slotUsedId", title: "Slot used id", width: 40 ,
                    filterable: {
                        ui: function (e) {
                            e.kendoNumericTextBox({
                                format: "#",
                                decimals: 0
                            });
                        }
                    }
                },
                { field: "calendarId", title: "Calendar", width: 80,  template: '#= calendarRow #', filterable: false },
                { field: "slotId", title: "Location", width: 80, template: '#= parkingSlotRow #',
                		filterable: {
                			ui : ddlFilterSlot
		                },
                },
                { field: "status", title: "Status", width: 50, attributes: {"class" : "div#= status #" }
                     , filterable: {
                        multi:true,
                    	dataSource: [
                                     { status: "Open" },
                                     { status: "Reserved" },
                                     { status: "Parked" },
                                     { status: "Inactive" }
                                 ]
                    }

                },
                { field: "parkingId", title: "Parking id", width: 40 ,
                    filterable: {
                        ui: function (e) {
                            e.kendoNumericTextBox({
                                format: "#",
                                decimals: 0
                            });
                        }
                    }
 				},
                { field: "userId", title: "User", width: 80, template: '<a href="${root}allusers/view/#=userId#">#= (userId == null) ? " " :  userRow#</a>'
                    , filterable: {
                        ui: ddlFilterUserId
                    }

                },
				{ command: [{name: "myDelete", text: "Delete", click: deleteItem}], title: "&nbsp;", width: 80 }],

        }).data("kendoGrid");


        function onFilterMenuInit(e) {
            e.container.on("click", "[type='reset']", function () {
            	$("#fromDate").val("");
            	$("#toDate").val("");
            	$("#slotId").val("");
            	$("#userId").val("");
				//options.extraSearch = "";
				//options.extraTables = "";
            });
        }

		function onChange(e, args) {

	     var grid = e.sender;
	     var items = grid.items();
	     items.each(function (idx, row) {

	         var idValue = grid.dataItem(row).get(idField);

	         if (row.className.indexOf("k-state-selected") >= 0) {
				mapSelect.put(idValue, JSON.parse(JSON.stringify(grid.dataItem(row))));
				selectedRows[idValue] = true;
	         } else if (selectedRows[idValue]) {
				mapSelect.put(idValue,"");
				delete selectedRows[idValue];
	         }
	     });
	 }


        var page = $("#page").val();

        if (!!strFormMsg || !!strFormErr)
       	{
        	if (!!page)
       		{
        		var dataSource = grid.dataSource;
        		dataSource.fetch(function()
       				{
        				dataSource.page(page);
       				}
        		);
       		}


       	}
        $("#newStatus").val("Filter");


});





    </script>

</body>
</html>

