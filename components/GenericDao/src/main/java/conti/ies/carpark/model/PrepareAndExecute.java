package conti.ies.carpark.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.Work;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import conti.ies.comp.Cons.eExec;

@Repository
public class PrepareAndExecute implements Work,  IPrepareAndExecute {

	private static final Logger logger = LoggerFactory.getLogger(PrepareAndExecute.class);

    private String Sql;
    private String retValue = null;
    private int numRowsUpdated = 0;
    private String msg = null;
    private List<String> listColumns = null;
    private eExec  exec = eExec.Exec;


	@Autowired
	private SessionFactory sessionFactory;

	@Override
    @Transactional
    public String executeQuery(String Sql, String msg)
    {
		this.exec = eExec.Query;
		this.msg = msg;
        this.Sql = Sql;
		retValue = null;
        getSession().doWork(this);
        return this.getRetValue();
    }

	@Override
    @Transactional
    public int executeUpdate(String Sql, String msg)
    {

		this.exec = eExec.Exec;
		this.msg = msg;
        this.Sql = Sql;
        getSession().doWork(this);
        return numRowsUpdated;
    }


	@Override
    @Transactional
    public List<String> columns(String Sql, String msg)
    {
		this.exec = eExec.Columns;
		this.msg = msg;
        this.Sql = Sql;
        getSession().doWork(this);
        return this.listColumns;
    }

	@Override
	public void execute(Connection connection) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
        	logger.info("<======== SQL =======> \n" + msg );
        	logger.info(Sql);
			ps = connection.prepareStatement(Sql);
			if (this.exec == eExec.Exec )
			{
				numRowsUpdated = ps.executeUpdate();
			}
			else if (this.exec == eExec.Columns )
			{
				rs = ps.executeQuery();
			    ResultSetMetaData rsMetaData = rs.getMetaData();
			    listColumns = new ArrayList<String>();
			    for (int i = 1; i <= rsMetaData.getColumnCount(); i++)
			    {
			    	listColumns.add(rsMetaData.getColumnName(i));
				}

			}
			else if (this.exec == eExec.Query )
			{
				rs = ps.executeQuery();

				if (rs.next()) {
				    retValue = rs.getString(1);
				}
			}
		} finally {
			ps.close();
		}

    }

    public String getSql() {
        return Sql;
    }

    public void setSql(String sql) {
        Sql = sql;
    }


    public String getRetValue() {
        return retValue;
    }

    public Session getSession()
    {
        return sessionFactory.getCurrentSession();
    }

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}


	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}


	public List<String> getListColumns() {
		return listColumns;
	}

	public void setListColumns(List<String> listColumns) {
		this.listColumns = listColumns;
	}

	public eExec getExec() {
		return exec;
	}

	public void setExec(eExec exec) {
		this.exec = exec;
	}



}

