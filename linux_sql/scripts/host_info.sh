#!/bin/bash

psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

if [[ "$#" -ne 5 ]] ; then
  echo 'Missing command line arguments.'
  echo './host_info.sh psql_host psql_port db_name psql_user psql_password'
  exit 1
fi

# get host hardware information
hostname=$(hostname -f)

lscpu_out=$(lscpu)
cpu_number=$(echo "$lscpu_out" | egrep "^CPU\(s\):" | awk '{print $2}' | xargs)
cpu_architecture=$(echo "$lscpu_out" | egrep "^Architecture:" | awk '{print $2}' | xargs)
cpu_model=$(echo "$lscpu_out" | egrep "^Model[[:space:]]name:" | awk '{$1=$2=""; print $0}' | xargs)
cpu_mhz=$(echo "$lscpu_out" | egrep "^CPU[[:space:]]MHz:" | awk '{print $3}' | xargs)
l2_cache=$(echo "$lscpu_out" | egrep "^L2[[:space:]]cache:" | awk '{print substr($3, 1, length($3)-1)}' | xargs)

meminfo_out=$(cat /proc/meminfo)
total_mem=$(echo "$meminfo_out" | egrep "^MemTotal:" | awk '{print $2}' | xargs)

timestamp=$(date +"%Y-%m-%d %H:%M:%S")

# create sql insert statement
insert_stmt="INSERT INTO host_info(hostname, cpu_number, cpu_architecture, cpu_model, cpu_mhz, l2_cache, total_mem, timestamp)"
insert_stmt+=" VALUES ('$hostname', $cpu_number, '$cpu_architecture', '$cpu_model', $cpu_mhz, $l2_cache, $total_mem, '$timestamp')"

# insert data into table
export PGPASSWORD=$psql_password
psql -h "$psql_host" -p "$psql_port"-d "$db_name" -U "$psql_user" -c "$insert_stmt"

exit 0