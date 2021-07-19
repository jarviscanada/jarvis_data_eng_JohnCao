#!/bin/bash

psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

if [[ "$#" -ne 5 ]] ; then
  echo 'Incorrect number of command line arguments'
  echo './host_usage.sh psql_host psql_port db_name psql_user psql_password'
  exit 1
fi

# get host usage information
hostname=$(hostname -f)
timestamp=$(date +"%Y-%m-%d %H:%M:%S")

meminfo_out=$(cat /proc/meminfo)
memory_free=$(echo "$meminfo_out" | egrep "^MemFree:" | awk '{print $2}' | xargs)

vmstat_out=$(vmstat)
cpu_idle=$(echo "$vmstat_out" | tail -1 | awk '{print $15}' | xargs)
cpu_kernel=$(echo "$vmstat_out" | tail -1 | awk '{print $14}' | xargs)
disk_io=$(vmstat -d | tail -1 | awk '{print $10}' | xargs)
disk_available=$(df -BM | egrep "^/dev/sda2" | awk '{print substr($4, 1, length($4)-1)}' | xargs)

# create sql insert statement
insert_stmt="INSERT INTO host_usage(timestamp, host_id, memory_free, cpu_idle, cpu_kernel, disk_io, disk_available)"
insert_stmt+=" VALUES('$timestamp', (SELECT id FROM host_info WHERE host_info.hostname = '$hostname'), "
insert_stmt+="$memory_free, $cpu_idle, $cpu_kernel, $disk_io, $disk_available);"

# insert data into table
export PGPASSWORD=$psql_password
psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"

exit $?
