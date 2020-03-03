<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="kendo" uri="http://www.kendoui.com/jsp/tags"%>
<html>
    <head>
		<%@include file="csrf.jsp" %>
        <title>Calendars List</title>



    </head>
<body>



		<div id="mainDiv" align="center" class="hero-unit">
		<spring:url	 value="/"		var="root"	 />

		<form:form action= "${root}calendar/search" modelAttribute="search"   method="post" >
		<table>
		<thead>

			<tr>
				<th class="col-sm-2 control-label" align="left">&nbsp;</th>
				<th class="col-sm-2 control-label" align="left">From date </th>
				<th class="col-sm-1 control-label" align="left">&nbsp;</th>
				<th class="col-sm-3 control-label" align="left">To date </th>
				<th class="col-sm-1 control-label" align="left">&nbsp;</th>
				<th class="col-sm-3 control-label" align="left">&nbsp;</th>
			</tr>
		</thead>
			<tr>
				<td >&nbsp;</td>
				<td>
					<input id="fromDate0"   />
 					<form:hidden path="fromDate" />
				</td>
				<td >&nbsp;</td>
				<td>
					<input id="toDate0"    />
 					<form:hidden path="toDate"/>
				</td>
				<td >&nbsp;</td>
				<td>
					<button type="submit" class="btn btn-primary ">Generate</button>
				</td>

			</tr>


		</table>
		<table>
			<tr>
				<td class="col-sm-1">
					&nbsp;
				</td>
				<td class="col-sm-8">
					<button id="btnPopup" type="button" >
							<span class="k-sprite"></span>Popup
					</button>
					<input type="checkbox" id="chkPopup"   disabled="disabled" />

                            <div id="grid"></div>
                            <div class="console" > </div>
                            <span id="notification"></span>
                            <div id="del_window"></div>


				</td>
				<td class="col-sm-3">&nbsp;</td>
			</tr>

		</table>


<script>

$(function() {

	var start = $("#fromDate0").kendoDatePicker({
		format : ddMMMyyyy,
	    change: startChange
	}).data("kendoDatePicker");

	var end = $("#toDate0").kendoDatePicker({
		format : ddMMMyyyy,
	    change: endChange
	}).data("kendoDatePicker");

	end.min(start.value());

	window.start = start;
	window.end = end;


});


</script>

		</form:form>
		</div>


<script>

$(function() {

        var crudServiceBaseUrl = "${root}calendar";
        var dataSource = new kendo.data.DataSource({
        		transport: {
	                    read:  {
                        url: crudServiceBaseUrl + "/read",
                        contentType: "application/json",
                        type: "POST",
                        dataType: "json",
                        complete:  function (res, textStatus) {
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

                                if (textStatus != "success") {
                                	writeException  (res, textStatus);
                                }
                            }
                    },
                    destroy: {
                        url: crudServiceBaseUrl + "/destroy",
                        contentType: "application/json",
                        type: "POST",
                        dataType: "json",
                        complete: function (res, textStatus) {

                            if (textStatus != "success") {
                            	writeException  (res, textStatus);
                            }

                        }

                    },
                    parameterMap: function(options, operation) {
                        if (operation !== "read" ) {
                            var js = JSON.stringify(options.models);
                            return JSON.stringify(options.models);
                        }
                        else
                        {
                            return JSON.stringify(options);
                        }
                    }


                },
                requestEnd : onRequestEnd,
                error : error,
                batch: true,
                pageSize: 7,
                allowCopy: true,
                serverPaging: true,
                serverFiltering: true,
                serverSorting: true,
                schema: {
                	total: "total",
                	data : "data",
                	errors: "Errors",
                    model: {
                        id: "calendarId",
                        fields: {
                        	calendarId: { editable: false, nullable: true },
                            entryDate : {},
                            entryTime : {type: "datetime"},
                            exitTime : {type: "date"}

                        } ,
						entryDateDdMMMyyyy : function() {
							return kendo.toString(new Date(this.entryDate),'dd-MMM-yyyy');
						},
                        entryTimeHHmm2 : function() {
                            return kendo.toString(new Date ( this.entryTime + new Date(this.entryTime).getTimezoneOffset() * 60 * 1000 ), 'HH:mm')
                        }

                    }
                }
            });


        $("#grid").kendoGrid({
            dataSource: dataSource,
            //dataBound : onDataBound,
        	//change: onChange,
            editable: false,
        	filterable: true,
            selectable: "multiple cell",

        	allowCopy: true,
            pageable: {pageSizes : [5, 7, 10, 25, 50,"all"] },
            navigatable: true,
            scrollable: true,
            sortable: true,
            //columnMenu: true,
            resizable: true,


            height: 400,
            toolbar   : ["create", "save", "cancel"],
            columns: [
                { field: "calendarId", title: "Calendar id", width: 80  },
                { field: "entryDate", title: "Entry date" , template: "#= entryDateDdMMMyyyy() #", width: 130  },
                 { field: "entryTime", title: "Entry time", width: 170 , template: "#= entryTimeHHmm2() #" 

                },
                { field: "exitTime", title: "Exit time",  format : "{0:HH:mm}", width: 100, 
                    filterable: {
                        ui: "datetimepicker"
                    }
                },
				{ command: ["edit", {name: "myDelete", text: "Delete", click: deleteItem}], title: "&nbsp;", width: 200 }]
        });



    });

</script>



</body>
</html>


