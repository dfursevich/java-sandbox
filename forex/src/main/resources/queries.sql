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

# price stream analysis queries
select hour, format(avg(sd), 6) from (select dayofyear(time) as day, hour(time) as hour, format(std(close), 6) as sd from eurusd group by dayofyear(time), hour(time)) as sds group by sds.hour;
# +------+--------------------+
# | hour | format(avg(sd), 6) |
# +------+--------------------+
# |    0 | 0.000208           |
# |    1 | 0.000261           |
# |    2 | 0.000427           |
# |    3 | 0.000540           |
# |    4 | 0.000461           |
# |    5 | 0.000394           |
# |    6 | 0.000392           |
# |    7 | 0.000434           |
# |    8 | 0.000711           |
# |    9 | 0.000619           |
# |   10 | 0.000650           |
# |   11 | 0.000493           |
# |   12 | 0.000411           |
# |   13 | 0.000373           |
# |   14 | 0.000343           |
# |   15 | 0.000269           |
# |   16 | 0.000218           |
# |   17 | 0.000191           |
# |   18 | 0.000208           |
# |   19 | 0.000259           |
# |   20 | 0.000317           |
# |   21 | 0.000309           |
# |   22 | 0.000272           |
# |   23 | 0.000222           |
# +------+--------------------+




