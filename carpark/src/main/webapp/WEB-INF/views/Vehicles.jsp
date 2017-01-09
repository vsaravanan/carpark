<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
<%@taglib prefix="kendo" uri="http://www.kendoui.com/jsp/tags"%>

<html>
    <head>
		<%@include file="csrf.jsp" %>
        <title>Vehicles List</title>

		<spring:url	 value="/"		var="root"	 />

    </head>
<body>

		<button id="btnPopup" type="button" >
				<span class="k-sprite"></span>Popup
		</button>
		<input type="checkbox" id="chkPopup"   disabled="disabled" />

                            <div id="grid"></div>
                            <div class="console" > </div>
                            <span id="notification"></span>
                            <div id="del_window"></div>




<script>


	$(document).ready(function () {


        var crudServiceBaseUrl = "${root}vehicle";
        var dataSource = new kendo.data.DataSource({
                transport: {
                    read:  {
                        url: crudServiceBaseUrl + "/read",
                        contentType: "application/json",
                        type: "GET",
                        dataType: "json"
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
                    }


                },
                requestEnd : onRequestEnd,
                error : error,
                batch: true,
                pageSize: 10,
                schema: {
                	errors: "Errors",

                    model: {
                        id: "id",
                        fields: {
                            id: { type: "number", editable: false, nullable: true },
                            vehicleNo: {validation: { required: true }},
                            iuNo : {},
                            hasUser : {type: "boolean" }

                        }
                    }
                }
            });


        $("#grid").kendoGrid({
            toolbar   : ["create", "save", "cancel","excel"],
            excel: {
                fileName: "Vehicle.xlsx",
                proxyURL: crudServiceBaseUrl + "/export",
                filterable: true
            },
            dataSource: dataSource,
            dataBound : onDataBound,
            editable: true,
        	selectable: "multiple cell",
            filterable : true,
        	//change: onChange,
        	allowCopy: true,
            pageable: {pageSizes : [5, 10, 25, 50,"all"] },

            navigatable: true,
            scrollable: true,
            sortable: true,
            //columnMenu: true,
            resizable: true,

            height: 300,
            columns: [
                { field: "id", title: "Vehicle id", width: 80, filterable: { multi: true } },
                { field: "vehicleNo", title: "Vehicle No", width: 80, filterable: { multi: true } },
                { field: "iuNo", title: "IU No", width: 80, filterable: { multi: true } },
				{ command: ["edit", {name: "myDelete", text: "Delete", click: deleteItem}], title: "&nbsp;", width: 80 }]
        });


        function onDataBound(e) {

        	  $("#grid tbody tr .k-grid-myDelete").each(function () {

              	var currentDataItem = $("#grid").data("kendoGrid").dataItem($(this).closest("tr"));
              	if (currentDataItem.hasUser) {
              	    $(this).remove();
              	}
        	  });
        }



        function deleteItem(e) {
        	e.preventDefault();

			var dataItem = this.dataItem($(e.currentTarget).closest("tr"));
      	    if (dataItem.hasUser)
      	    {
      	    	errorMsg("cannot delete", "It contains parent record.");
      	    	$("#grid").data("kendoGrid").cancelChanges();
      	    }
      	    else
      	    {
      	    	wnd.content(vehicleDeleteWindow(dataItem));
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
        }


    });

</script>

</body>
</html>



