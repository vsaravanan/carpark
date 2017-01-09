package conti.ies.carpark.dao;

import org.springframework.stereotype.Repository;

import conti.ies.carpark.model.Carpark;
import conti.ies.comp.GenericDao;

@Repository
public class CarparkDao extends GenericDao<Carpark, Integer> implements ICarparkDao {

}
