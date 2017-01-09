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
        <title>Parking Slots</title>


    </head>
<body>



		<div id="mainDiv" align="center" class="hero-unit">
		<spring:url	 value="/"		var="root"	 />
		<table>
				<form:form action= "${root}parkingSlot/search" modelAttribute="search"   method="post" >
		<thead>

			<tr>
				<th class="col-sm-2 control-label" align="left">&nbsp;</th>
				<th class="col-sm-2 control-label" align="left">&nbsp;</th>
				<th class="col-sm-2 control-label" align="left">&nbsp;</th>
				<th class="col-sm-6 control-label" align="left">&nbsp;</th>
			</tr>
		</thead>
			<tr>


				<td>
					&nbsp;
				</td>
				<td>
					&nbsp;
				</td>

				<td>
					<button type="submit" class="btn btn-primary ">Search</button>
				</td>
				<td>
					&nbsp;
				</td>

			</tr>


			<tr>
				<td colspan="4" class="col-sm-12">
					<button id="btnPopup" type="button" >
							<span class="k-sprite"></span>Popup
					</button>
					<input type="checkbox" id="chkPopup"   disabled="disabled" />

                            <div id="grid"></div>
                            <div class="console" > </div>
                            <span id="notification"></span>
                            <div id="del_window"></div>


				</td>
			</tr>

		</table>



		</form:form>
		</div>


<script>

$(function() {

        var crudServiceBaseUrl = "${root}parkingSlot";
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
                pageSize: 5,
                allowCopy: true,
                serverPaging: true,
                serverFiltering: true,
                serverSorting: true,
                schema: {
                	total: "total",
                	data : "data",
                	errors: "Errors",
                    model: {
                        id: "slotId",
                        fields: {
                        	slotId: {type: "number", editable: false, nullable: true },
                        	location : {type: "string"},
                        	level : {type: "number"},
                        	slotNo : {type: "number"},
                        	tagId : {type: "string"},
                        	status : {type: "string"}
                        }

                    }
                }
            });


        $("#grid").kendoGrid({
            dataSource: dataSource,
            editable : true,

            filterable: {
                extra: false,

                 operators: {
                    number: {
                        eq: "Is equal to",
                        neq: "Is not equal to"

                    }
                }
            },
            selectable: "multiple cell",

        	allowCopy: true,
            pageable: {pageSizes : [5, 10, 25, 50,"all"] },
            navigatable: true,
            scrollable: true,
            sortable: true,
            //columnMenu: true,
            resizable: true,


            height: 300,
            toolbar   : ["create", "save", "cancel"],
            columns: [
                { field: "slotId", title: "Slot id", width: 80},
                { field: "location", title: "Location", width: 150, template: '<a href="${root}parkingSlot/edit/#=slotId#">#=location#</a>' },
                { field: "level", title: "Level", width: 80 },
                { field: "slotNo", title: "Slot No", width: 80 },
                { field: "tagId", title: "Tag Id", width: 80},
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

				{ command: ["edit", {name: "myDelete", text: "Delete", click: deleteItem}], title: "&nbsp;", width: 200 }]
        });



    });

</script>



</body>
</html>


