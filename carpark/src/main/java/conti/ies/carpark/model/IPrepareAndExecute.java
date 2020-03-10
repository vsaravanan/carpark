package conti.ies.carpark.model;

import java.util.List;

public interface IPrepareAndExecute {

    public String executeQuery(String Sql, String msg);
    public int executeUpdate(String Sql, String msg);
	List<String> columns(String Sql, String msg);

}
