package conti.ies.carpark.dao;

import org.springframework.stereotype.Repository;

import conti.ies.carpark.model.Calendar;
import conti.ies.comp.GenericDao;

@Repository
public class CalendarDao extends GenericDao<Calendar,Integer> implements ICalendarDao{


}
