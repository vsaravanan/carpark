package conti.ies.carpark.dao;

import org.springframework.stereotype.Repository;

import conti.ies.carpark.model.VwParking;
import conti.ies.comp.GenericDao;

@Repository
public class VwParkingDao extends GenericDao<VwParking, Integer> implements IVwParkingDao{

}
