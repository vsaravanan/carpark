package conti.ies.carpark.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.Work;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;




@Repository
public class AdhocDao  {

	private static final Logger logger = LoggerFactory.getLogger(AdhocDao.class);

	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession()
    {
    	return sessionFactory.getCurrentSession();
    }


	@Transactional
	public void executeSql(final String Sql)
	{
	      Session session = null;


          session = getSession();
          //session.beginTransaction();

          session.doWork(
				  new Work() {


					@Override
					public void execute(Connection connection) throws SQLException {
						  PreparedStatement pStmt = null;
						  try
						  {
				    		  logger.info(Sql);
							  pStmt = connection.prepareStatement(Sql);
							  pStmt.executeQuery();
						  }
						  finally
						  {
							  pStmt.close();
						  }
					}
				  }

		  );
          //session.getTransaction().commit();
	}



	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}


	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}



	@SuppressWarnings("unchecked")
	@Transactional
	public Map<String, String>  mapLkp(String Sql) {


        SQLQuery adQry = getSession().createSQLQuery(Sql);

        Map<String, String> map = new HashMap<>();

        List<Object[]> rows = adQry.list();
        for(Object[] row : rows){
        	map.put(row[0].toString(), row[1].toString());
        }

		return map;
	}


	@SuppressWarnings("unchecked")
	@Transactional
	public List<String>  listLkp(String Sql) {


        SQLQuery adQry = getSession().createSQLQuery(Sql);

        List<String>  list = new ArrayList<>();

        List<Object[]> rows = adQry.list();
        for(Object[] row : rows){
        	list.add(row[0].toString());
        }

		return list;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public Map<Integer, String>  mapIntLkp(String Sql) {


        SQLQuery adQry = getSession().createSQLQuery(Sql);

        Map<Integer, String> map = new HashMap<>();

        List<Object[]> rows = adQry.list();
        for(Object[] row : rows){
        	map.put( (Integer) row[0], row[1].toString());
        }

		return map;
	}



}
