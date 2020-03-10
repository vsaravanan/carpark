package conti.ies.carpark.dao;


import org.springframework.stereotype.Repository;

import conti.ies.carpark.model.Vehicle;
import conti.ies.comp.GenericDao;


@Repository
public class VehicleDao extends GenericDao<Vehicle, Integer> implements IVehicleDao {


}