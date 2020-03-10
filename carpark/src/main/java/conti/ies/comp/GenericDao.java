package conti.ies.comp;


import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

//import org.hibernate.*;



@Repository
public abstract class GenericDao<T, PK extends Serializable> implements IGenericDao<T, PK>  {

	private static final Logger logger = LoggerFactory.getLogger(GenericDao.class);


	@Autowired
	SessionFactory sessionFactory;

	@PersistenceContext
	private EntityManager entityManager;

	private Class<T> entityClass;

	private  String className;

    public Session getSession()
    {
    	return sessionFactory.getCurrentSession();
    }



	@SuppressWarnings({ "unchecked", "rawtypes" })
	public GenericDao() {
		//this.sessionFactory = sessionFactory;
		Type t = getClass().getGenericSuperclass();
		ParameterizedType pt = (ParameterizedType) t;
		entityClass = (Class) pt.getActualTypeArguments()[0];
		className = entityClass.getSimpleName().toLowerCase();
	}



	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<T> list() {
		return getSession().createQuery( "from " + entityClass.getName() ).list();
	}


	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public <kls> List<? extends kls> list(String sql, Class<?> kls) {
		Query qrySql = getSession().createSQLQuery( sql ).addEntity(kls);
		return qrySql.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<T> list(int pageSize, int skip) {
		Query query = getSession().createQuery("from " + entityClass.getName());
		query.setFirstResult(skip);
		query.setMaxResults(pageSize);
		return query.list();

	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<T> adhocQry(String Sql)
	{
		Query query = getSession().createSQLQuery(Sql).addEntity(entityClass) ;
		return query.list();
	}


//	@Override
//	public KendoRead qryPaging(Object kendoFilter, Class<?> kls)
//	{
//		return qryPaging(kendoFilter);
//	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public KendoRead qryPaging(Object kendoFilter) {

		PagingContainer pc = UtilPaging.getPaging(kendoFilter, entityClass);
		FilterPtrRoot fpr = pc.getFpr();
		Map<String, String> mapClassProps = pc.getEp().getClassProps();

		String Sql = mapClassProps.get("sql");
		String drivingTable = mapClassProps.get("drivingTable");

		Set<String> joinTables = fpr.getJoinTables();
/*		if (! fpr.getExtraTables().equals(""))
		{
			for (String str : fpr.getExtraTables().split(", "))
			{
				joinTables.add(str.trim());
			}
		}
*/

		String extraTables = "";
       	if (joinTables.size() > 0)
       		extraTables = " , " + StringUtils.join(joinTables," , ");

		Set<String> joinWheres = fpr.getJoinWheres();
/*		if (! fpr.getExtraSearch().equals(""))
		{
			joinWheres.add(fpr.getExtraSearch());
		}*/

		String extraWheres = "";
       	if (joinWheres.size() > 0)
       		extraWheres = " ( " + StringUtils.join(joinWheres," and ") + " ) ";

		List<String> listWheres = Arrays.asList(extraWheres, fpr.getWhere());
		listWheres = Utils.removeBlanks(listWheres);
		String where = StringUtils.join(listWheres, " and ") ;

/*		if (StringUtils.isNotEmpty(fpr.getExtraSearch()))
			where = fpr.getExtraSearch();
		else if (StringUtils.isNotEmpty(fpr.getWhere()))
			where = fpr.getWhere();
*/


		if (! where.equals(""))
			where = " where " + where;


		Integer counts;

		String SqlCount = "SELECT count(*) from <drivingTable> s <extraTables> <where>";
		SqlCount = SqlCount.replaceAll("<drivingTable>", drivingTable);
		SqlCount = SqlCount.replaceAll("<extraTables>", extraTables);
		SqlCount = SqlCount.replaceAll("<where>", where);
		Query qryCount = getSession().createSQLQuery(SqlCount);
		List list = qryCount.list();
		counts = ((BigInteger) list.get(0)).intValue();
		logger.info(" counts " + counts);

		if (counts == 0)
			Sql = "SELECT * FROM " + drivingTable + " limit 0 ";
		else {

			Sql = Sql.replaceAll("<keyId>", mapClassProps.get("keyId") );

			// check if too many records use sort by index key
			// to avoid performance degrade

			if (counts > 10000 || StringUtils.isBlank(fpr.getOrderByRow()))
			{
				Sql = Sql.replaceAll("<orderByNum>", " ORDER BY s.<sortKey> " );
				Sql = Sql.replaceAll("<sortKey>", mapClassProps.get("sortKey") );
			}
			else
			{
				Sql = Sql.replaceAll("<orderByNum>",fpr.getOrderByRow() );
			}


			Sql = Sql.replaceAll("<drivingTable>", drivingTable);
			Sql = Sql.replaceAll("<extraTables>", extraTables);
			Sql = Sql.replaceAll("<where>", where);
			Sql = Sql.replaceAll("<orderBy>", fpr.getOrderBy());
			Sql = Sql.replaceAll("<skip>", String.valueOf(fpr.getSkip()));
			Sql = Sql.replaceAll("<pageSize>", String.valueOf(fpr.getPageSize()));
		}

		//logger.info("GenericDao.qryPaging : " + tableName + "  " + drivingTable + "  " + Sql);

		Query qrySql = getSession().createSQLQuery(Sql).addEntity(entityClass); // MVS kls

		final List lstData = qrySql.list();

		KendoRead kr = new KendoRead(lstData, counts, pc.getMessage());

		return kr;

	}


	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public T get(PK id) {
		return (T) getSession().get(entityClass, id);
	}

	@Override
	@Transactional
	public void saveOrUpdate(T t) {
		getSession().saveOrUpdate(t);
	}

	@Override
	@Transactional
	public void saveOrUpdate(T... t) {
		for (T i : t)
			getSession().saveOrUpdate(i);
	}

	@Override
	@Transactional
	public void saveOrUpdate(List<T> t) {
		for (T i : t)
			getSession().saveOrUpdate(i);
	}


	@Override
	@Transactional

	public void delete(PK id)  {
		Object entity = getSession().get(entityClass, id);
		if (entity != null)
			getSession().delete(entity);

	}


	@Override
	//@Transactional
	public void delete(T t) {
			getSession().delete(t);
	}

	@Override
	//@Transactional
	public void delete(T... t) {
		for (T i : t)
			getSession().delete(i);
	}

	@Override
	@Transactional
	public void delete(List<T> t) {
		for (T i : t)
			getSession().delete(i);
	}

	@Override
	@Transactional
	public T findByUserName(String userName) {
		CriteriaBuilder builder = getSession().getCriteriaBuilder();
		CriteriaQuery<T> cq = builder.createQuery(entityClass);
		Root<T> root = cq.from(entityClass);
		cq.select(root).where(builder.equal(root.get("userName"), userName));
		Query<T> q= getSession().createQuery(cq);
		T t=q.getSingleResult();
		return t;

//		Criteria crit = getSession().createCriteria(entityClass);
//		crit.add(Restrictions.eq("userName", userName));
//		return (T) crit.uniqueResult();
	}


	public String getClassName() {
		return className;
	}



	public void setClassName(String className) {
		this.className = className;
	}

}

//�       @Component � Indicates a auto scan component.
//�       @Repository � Indicates DAO component in the persistence layer.
//�       @Service � Indicates a Service component in the business layer.
//�       @Controller � Indicates a controller component in the presentation layer.
//| Annotation | Meaning                                             |
//+------------+-----------------------------------------------------+
//| @Component | generic stereotype for any Spring-managed component |
//| @Repository| stereotype for persistence layer                    |
//| @Service   | stereotype for service layer                        |
//| @Controller| stereotype for presentation layer (spring-mvc)



//	String hql = "from " + type.getName() +  " where id=" + id;
//	Query query = getSession().createQuery(hql);
//
//	@SuppressWarnings("unchecked")
//	List<T> listT = (List<T>) query.list();
//
//	if (listT != null && !listT.isEmpty()) {
//		return listT.get(0);
//	}
//
//	return null;

//	@Override
//	@Transactional
//	public void persist(T t) {
//		getSession().persist(t);
//	}
//
//	@Override
//	@Transactional
//	public void persist(T... t) {
//		for (T i : t)
//			getSession().persist(i);
//	}
//
//	@Override
//	@Transactional
//	public void save(T t) {
//		getSession().save(t);
//		// flush(); flush is required if you don't use transaction
//	}
//
//	@Override
//	@Transactional
//	public void save(T... t) {
//		for (T i : t)
//			getSession().save(i);
//	}
//
//	@Override
//	@Transactional
//	public void merge(T t) {
//		getSession().merge(t);
//	}
//
//	@Override
//	@Transactional
//	public void merge(T... t) {
//		for (T i : t)
//			getSession().merge(i);
//	}
//
//	public void flush() {
//		getSession().flush();
//	}
//
//	public void clear() {
//		getSession().clear();
//	}
//	public void refresh(T t) {
//		getSession().refresh(t);
//	}




