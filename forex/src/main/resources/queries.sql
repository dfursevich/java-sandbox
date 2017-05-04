select e1.id, e1.low, e1.high, e2.id, e2.low, e2.high, e3.id, e3.low, e3.high from eurusd e1
  join eurusd e2 on e1.id < e2.id and (e2.id - e1.id) < 100
  join eurusd e3 on e3.id = e2.id + 1
  where (e1.low > e2.high or e1.high < e2.low) and ((e1.high < e3.high and e1.high > e3.low) or (e1.high > e3.high and e1.low < e3.high))
  group by e1.id
  order by e1.id, e2.id, e3.id
  limit 10;


select e1.id as start_id, min(e3.id) as end_id from eurusd e1
  join eurusd e2 on e1.id < e2.id and (e2.id - e1.id) < 100
  join eurusd e3 on e3.id = e2.id + 1
where (e1.low > e2.high or e1.high < e2.low) and ((e1.high < e3.high and e1.high > e3.low) or (e1.high > e3.high and e1.low < e3.high))
group by e1.id
order by e1.id
limit 10;


select
  matches.start_id,
  matches.end_id,
  (select min(e4.low) from eurusd e4 where e4.id >= matches.start_id and e4.id <= matches.end_id) as min,
  (select max(e5.high) from eurusd e5 where e5.id >= matches.start_id and e5.id <= matches.end_id) as max
from (select e1.id as start_id, min(e3.id) as end_id from eurusd e1
  join eurusd e2 on e1.id < e2.id and (e2.id - e1.id) < 100
  join eurusd e3 on e3.id = e2.id + 1
where (e1.low > e2.high or e1.high < e2.low) and ((e1.high < e3.high and e1.high > e3.low) or (e1.high > e3.high and e1.low < e3.high))
group by e1.id
order by e1.id
limit 10) as matches;