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
        <title>Carparks</title>


    </head>
<body>



		<div id="mainDiv" align="center" class="hero-unit">
		<spring:url	 value="/"		var="root"	 />
		<table>
				<form:form action= "${root}carpark/search" modelAttribute="search"   method="post" >
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
					<form:hidden path="userName"/>
					<form:hidden path="userId"/>

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

        var crudServiceBaseUrl = "${root}carpark";
        //var userName;
        var dataSource = new kendo.data.DataSource({
        		transport: {
	                    read:  {
                        url: crudServiceBaseUrl + "/read",
                        contentType: "application/json",
                        type: "POST",
                        dataType: "json",
                        complete:  function (res, textStatus) {
                        	//userName = res.responseJSON.search.userName;

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
                                	promptMsg(res);
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
                                else
                                	promptMsg(res);
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
                            else
                            	promptMsg(res);


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
                pageSize: 8,
                allowCopy: true,
                serverPaging: true,
                serverFiltering: true,
                serverSorting: true,
                schema: {
                	total: "total",
                	data : "data",
                	errors: "Errors",
                    model: {
                        id: "carparkId",
                        fields: {
                        	carparkId	: {type: "number", editable: false, nullable: true },
                        	carpark 	: {type: "string"},
                        	latitude 	: {type: "number"},
                        	longitude 	: {type: "number"},
                        	userId 		: {type: "number", editable: false, defaultValue: $("#userId").val()},
                        	userName 	: {type: "string", editable: false, defaultValue: $("#userName").val()},
                        	whenUploaded : {type: "datetime", editable: false, defaultValue: new Date()}
                        }

                    }
                }
            });


        $("#grid").kendoGrid({
            dataSource: dataSource,
            editable : true,
            dataBound : onDataBound,
            edit: onEdit,
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


            height: 500,
            toolbar   : ["create", "save", "cancel"],
            columns: [
                { field: "carparkId", title: "carpark id", width: 60,
                    filterable: {
                        ui: function (element) {
                            element.kendoNumericTextBox({
                                format: "#",
                                decimals: 0
                            });
                        }
                    }
                },
                { field: "carpark", title: "Carpark", width: 150},
                { field: "latitude", title: "latitude", width: 80, editor: numberEditor, format: '{0:n5}',
                    filterable: {
                        ui: function (element) {
                            element.kendoNumericTextBox({
                                format: "n5",
                                decimals: 5
                            });
                        }
                    }
                },
                { field: "longitude", title: "longitude", width: 90, editor: numberEditor, format: '{0:n5}' ,
                    filterable: {
                        ui: function (element) {
                            element.kendoNumericTextBox({
                                format: "n5",
                                decimals: 5
                            });
                        }
                    }
                },
                { field: "userName", title: "User Name", width: 70},
                { field: "whenUploaded", title: "Last updated", width: 90,  template: "#=   (whenUploaded == null) ? '' : kendo.toString(new Date(whenUploaded), ddMMMyyyyHHmm)   #"  },
				{ command: ["edit", {name: "myDelete", text: "Delete", click: deleteItem}], title: "&nbsp;", width: 80 }]
        });

        function numberEditor(container, options) {
            $('<input name="' + options.field + '"/>')
                    .appendTo(container)
                    .kendoNumericTextBox({
                        format  : "{0:n5}",
                        decimals: 5,
                        step    : 0.00001
                    });
        }

        function onEdit(e) {
        	if (e.model.userName != $("#userName").val() )
    		{
    			this.closeCell();
    		}
        	//$('#grid').data("kendoGrid").cancelRow();

        }


        function onDataBound(e) {

      	  $("#grid tbody tr .k-grid-myDelete, #grid tbody tr .k-grid-edit").each(function () {

            	var currentDataItem = $("#grid").data("kendoGrid").dataItem($(this).closest("tr"));
            	if (currentDataItem.userName != $("#userName").val()) {
            	    $(this).remove();
            	}
            	//currentDataItem.cancelRow();
      	  });
      }

//       	$("#formMsg").val("");


    });

</script>



</body>
</html>


