-- Group hosts by hardware info
SELECT cpu_number, id, total_mem
FROM (
         SELECT cpu_number, id, total_mem,
                RANK() OVER (
                    PARTITION BY cpu_number
                    ORDER BY total_mem desc
                    ) as rank
         FROM host_info
         ORDER BY cpu_number, rank
     ) as sorted_total_mem;

-- Average Memory Usage
WITH mem_usage_to_nearest_interval (host_id, nearest_interval, mem_used) AS (
    SELECT host_id, 
	   (date_trunc('hour', host_usage.timestamp) + date_part('minute', host_usage.timestamp):: int / 5 * interval '5 min') as nearest_interval,
           (total_mem - memory_free) as mem_used
    FROM host_usage, host_info
    WHERE host_usage.host_id = host_info.id
), avg_used_memory (host_id, nearest_interval, avg_used_memory) AS (
    SELECT host_id, nearest_interval, 
	   AVG(mem_used) as avg_used_memory
    FROM host_info as h, mem_usage_to_nearest_interval as m
    WHERE h.id = m.host_id
    GROUP BY host_id, nearest_interval
)
SELECT host_id, hostname, 
       nearest_interval as "timestamp", 
       ROUND(avg_used_memory::DECIMAL / total_mem * 100) as avg_used_mem_percentage
FROM avg_used_memory as u, host_info as h
WHERE u.host_id = h.id
ORDER BY host_id, "timestamp";

-- Detect Host Failure
SELECT host_id, entries.timestamp, COUNT(*) as num_data_points
FROM (
         SELECT host_id, 
		(date_trunc('hour', timestamp) + date_part('minute', timestamp):: int / 5 * interval '5 min') as "timestamp"
         FROM host_usage
    ) as entries
GROUP BY host_id, entries.timestamp
ORDER BY host_id, entries.timestamp;
