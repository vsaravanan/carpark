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
        <title>Parking Bill List</title>


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

				<form:form action= "${root}parkingBill/search" modelAttribute="search"   method="post" >

                    <div class="row-fluid" align="left" style="font-weight: bold;">
                        <div class="span3" >From date </div>
                        <div class="span3" >To date </div>
                        <div class="span6" ></div>
                    </div>


                    <div class="row-fluid" align="left">
                        <div class="span3" >
                            <input id="fromDate0"   />
                            <form:hidden path="fromDate" />
                        </div>
                        <div class="span3" >
                            <input id="toDate0"    />
                            <form:hidden path="toDate"/>
                         </div>

                        <div class="span6" >
                            <div class="row-fluid">
		                        <div class="span6"></div>
                                <div class="span3">
                                    <button type="button" class="btn btn-block btn-primary" >Payment</button>
                                </div>

                                <div class="span3">
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



        var crudServiceBaseUrl = "${root}parkingBill";

        idField = "billId";

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
                            kendoConsole.log(js);
                            return JSON.stringify(options.models);
                        }
                        else
                        {


                         	var i = 0;
                        	var lstTables = {};
                         	var lstWhere = [];

                         	if (!! $("#fromDate").val())
                       		{
                         		//lstTables[" Calendar c "] = 0;
                     			lstWhere.push(" s.entryTimeInReserve >= '" +  $("#fromDate").val() + "'");
                       		}


                           	if (!! $("#toDate").val())
                       		{
                         		//lstTables[" Calendar c "] = 0;
                     			lstWhere.push(" s.exitTimeInReserve < '" +  $("#toDate").val() + "'");
                       		}


                           	var tables = [];
                           	for(o in lstTables) tables.push(o);

                           	var extraTables = "";
                           	if (tables.length > 0)
                           		extraTables = " , " + tables.join(", ");
							options.extraTables = extraTables;

							var extraSearch = "";
                           	if (lstWhere.length > 0)
                           		extraSearch = lstWhere.join(" and ");
							options.extraSearch = extraSearch;

                        	var js = JSON.stringify(options);
                            //console.log(js);
                            //kendoConsole.log(js);

                            return js;

                        }
                    }


                },
                requestEnd : onRequestEnd,
                error : error,
                batch: true,
                pageSize: 8,
                serverPaging: true,
                serverFiltering: true,
                serverSorting: true,
                schema: {
                	total: "total",
                	data : "data",
                	errors: "Errors",

                    model: {
                        id: "billId",
                        fields: {
                        	billId: { editable: false, nullable: true },
                        	entryTimeInReserve : { nullable: true, type: "datetime"},
                        	exitTimeInReserve : { nullable: true, type: "datetime"},
                        	status : {type: "string"},
                        	entryTime : { nullable: true,  type: "datetime"},
                        	exitTime : { nullable: true, type: "datetime"},
                        	numMinsParked : {type: "number"},
                        	charges : {type: "number"}
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
            pageable: {pageSizes : [8, 10, 25, 50,"all"] },
            navigatable: true,
            scrollable: true,
            sortable: {
                mode: "multiple",
                allowUnsort: true
            },
            resizable: true,
            height: 400,

            columns: [

                { field: "billId", title: "Bill Id", width: 40},
                { field: "entryTimeInReserve", title: "Reserved Entry Time", width: 100,  template: "#=   (entryTimeInReserve == null) ? '' : kendo.toString(new Date(entryTimeInReserve), ddMMMyyyyHHmm)   #"  },
                { field: "exitTimeInReserve", title: "Reserved Exit Time", width: 100,  template: "#=   (exitTimeInReserve == null) ? '' : kendo.toString(new Date(exitTimeInReserve), ddMMMyyyyHHmm)   #"  },
                { field: "status", title: "Status", width: 60, attributes: {"class" : "div#= status #" }
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
                { field: "entryTime", title: "Parking Entry Time", width: 100, template: "#=   (entryTime == null) ? '' : kendo.toString(new Date(entryTime), ddMMMyyyyHHmm)   #" },
                { field: "exitTime", title: "Parking Exit Time", width: 100 ,  template: "#=   (exitTime == null) ? '' : kendo.toString(new Date(exitTime), ddMMMyyyyHHmm)   #"  },
                { field: "numMinsParked", title: "No of mins parked", width: 80},
                { field: "charges", title: "Parking Charges", width: 80}

				//{ command: [{name: "myDelete", text: "Delete", click: deleteItem}], title: "&nbsp;", width: 80 }
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




});





    </script>

</body>
</html>

