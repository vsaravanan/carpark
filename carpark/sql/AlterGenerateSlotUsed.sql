CREATE OR REPLACE FUNCTION carpark.generateslotused(fromdate timestamp without time zone,todate timestamp without time zone,slotids text,status text,userid integer,parkingid integer)
    RETURNS void
    LANGUAGE 'plpgsql'
    VOLATILE
    PARALLEL UNSAFE
    COST 100
AS $BODY$DECLARE
	insert_blank_slots text;
	update_slots text;
	execute_stat text;
	set_stat text := '';
	need_comma boolean := false;
BEGIN

	if slotIds != '' then
		slotIds := ' in ( ' ||  slotIds || ' ) ';
	else
		slotIds := ' = p.slotId ';
	end if;

	if status != '' then
		if need_comma then
			set_stat := set_stat || ' , ';
		end if;
		set_stat := set_stat || ' status =  ''' || status || ''' ';
		need_comma := true;
	end if;

	if userid is not null then
		if need_comma then
			set_stat := set_stat || ' , ';
		end if;
		set_stat := set_stat || ' userid =  ' || userid || ' ';
		need_comma := true;
	end if;

	if parkingid is not null then
		if need_comma then
			set_stat := set_stat || ' , ';
		end if;
		set_stat := set_stat || ' parkingid =  ' || parkingid || ' ';
		need_comma := true;
			
	end if;

	if status = 'Open'  then
	
		insert_blank_slots := '
			insert into slotUsed
		(slotId, calendarId)
		select 
			p.slotId, c.calendarId
		from calendar c 
		inner join parkingSlot p
		on p.slotId '  ||  slotIds  || '
		and c.entrytime >= ''' || fromDate || '''
		and c.exittime  < ''' || toDate || '''
		left join slotUsed s
		on c.calendarId = s.calendarId
		and p.slotId = s.slotId
		where s.calendarId is null
		order by 1, 2'
		;
		

		execute_stat := insert_blank_slots;

		raise info '%', execute_stat;

		execute execute_stat;
	
	end if;
			
	update_slots := '
		with r as 
		(
			select 
				s.slotUsedId
			from calendar c 
			inner join parkingSlot p
			on p.slotId '  ||  slotIds  || '
			and c.entrytime >= ''' || fromDate || '''
			and c.exittime  < ''' || toDate || '''
			inner join slotUsed s
			on c.calendarId = s.calendarId
			and p.slotId = s.slotId

		)
		update 	slotUsed
		set	' || set_stat ||
		' from r
		  where  slotUsed.slotUsedId = r.slotUsedId '
		;		

	execute_stat := update_slots;
	raise info '%', execute_stat;

	execute execute_stat;
	

	
END;
$BODY$;

truncate table slotused cascade;
select GenerateSlotUsed('20-Feb-2020 18:00',  '20-Feb-2020 19:00', '3', 'Open', null, null ) 