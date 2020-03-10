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
        <title>View Reserves and Parkings</title>


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

		var btnClicked = e.text();
        $("#newStatus").val(btnClicked);

        if (btnClicked == "Search")
       	{
        	return btnSearch();
       	}


        var grid = $("#grid").data("kendoGrid");
        $("#skip").val(grid.dataSource.skip());
        $("#page").val(grid.dataSource.page());

        $("form").submit();

	}
});
</script>

    </head>
<body>




		<div id="mainDiv" align="center"  class="container-fluid carparkForm">
		<spring:url	 value="/"		var="root"	 />

				<form:form action= "${root}parking/search" modelAttribute="search"   method="post" >

                    <div class="row-fluid" align="left" style="font-weight: bold;">
                        <div class="span2" >From date </div>
                        <div class="span2" >To date </div>
                        <div class="span4" ></div>
                        <div class="span4" ></div>
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
                        <div class="span1" >
                            &nbsp;
                        </div>

                        <div class="span2" >
                            <button type="button" class="btn btn-block btn-Search" >Search</button>

                        </div>
                        <div class="span3" >
                            &nbsp;
                        </div>
                    </div>
                    <div class="row-fluid" > <div class="span12" > </div></div>
                    <div class="row-fluid" row>
                        <div class="span8">
                        </div>
                        <div class="span4">
                            <div class="row-fluid">
                                <div class="span2">
                                </div>

                                <div class="span2">
                                </div>


                                <div class="span2">
                                </div>
                                <div class="span2">
                                </div>
                                <div class="span2">
                                </div>
                                <div class="span2">
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
                            <form:hidden path="newStatus"/>
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

        var crudServiceBaseUrl = "${root}parking";
        var lookupUrl = "${root}lookup";


        idField = "parkingId";

        var dataSource = new kendo.data.DataSource({
        		transport: {

                    read:  {
                        url: crudServiceBaseUrl + "/read",
                        contentType: "application/json",
                        type: "POST",
                        dataType: "json",
                        complete:  function (res, textStatus) {
                        	//kendoConsole.log(res.responseText);
                        	writeException  (res, textStatus);
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
                                    $("#newStatus").val("");
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
                            kendoConsole.log(js);
                            return JSON.stringify(options.models);
                        }
                        else
                        {
                            if (!! options.filter) {
                                $("#newStatus").val() == "Filter"
                            }

                            var listFF = [];

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



                            if ($("#newStatus").val() == "Search")
                                options.btnSearch = true;
                            else
                                options.btnSearch = false;



                            options.fieldFilters = listFF;

                            // debugger;
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
                        id: "parkingId",
                        fields: {
                        	parkingId: {type: "number", editable: false, nullable: true },
                            slotUsedId : {type: "number"},

                            billId : {type: "number"},
                            finalBillId : {type: "number"},
                            parkingBillRow : {type: "string"},
                            status : {type: "string"},

                            slotId : {type: "number"},
                            parkingSlotRow : {type: "string"},

                            userId : {type: "number"},
                            userRow : {type: "string"},

                            vehicleId : {type: "number"},
                            vehicleRow : {type: "string"}
                        }
                    }
                }
            });



        var grid = $("#grid").kendoGrid({

            dataSource: dataSource,
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
            height: 300,

            columns: [

                { field: "billId", title: "Bill Id", width: 20},
                { field: "slotUsedId", title: "Slot Used Id", width: 30},
                { field: "finalBillId", title: "Final Bill Id", width: 25},

                { field: "billId", title: "Parking Bill", width: 90
                	, template: '<a href="${root}parkingBill/edit/#= finalBillId ? finalBillId : billId #">#=parkingBillRow#</a>'
            	},
            	{ field: "status", title: "Status", width: 30, attributes: {"class" : "div#= status #" }, filterable: false
           		},
                { field: "slotId", title: "Location", width: 40
           			, template: '<a href="${root}parkingSlot/edit/#=slotId#">#=parkingSlotRow#</a>'
            	},
                { field: "userId", title: "User", width: 50, template: '<a href="${root}allusers/view/#=userId#">#=userRow#</a>'
                	, filterable: {
                    	ui: ddlFilterUserId
		               	}
            	},
                { field: "vehicleId", title: "Vehicle", width: 30, template: '#= vehicleRow #'
            	},

                ],

        }).data("kendoGrid");


        function onFilterMenuInit(e) {
            e.container.on("click", "[type='reset']", function () {
            	$("#fromDate").val("");
            	$("#toDate").val("");
				//options.extraSearch = "";
				//options.extraTables = "";
            });
        }




        $("#formMsg").val("");
        $("#formErr").val("");



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

