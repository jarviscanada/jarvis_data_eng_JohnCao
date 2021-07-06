#!/bin/bash

# cmd can be create, start, or stop
cmd=$1

# start docker daemon if not started
systemctl status docker || system start docker

container_name='jrvs-psql'

# writes out true if docker container has already been created, writes out false if not
function was_container_created {
  if [[ $(docker container ls -a -f name="${container_name}" | wc -l) -eq 2 ]] ; then
    echo "true"
  else
    echo "false"
  fi
}

case $cmd in
  # create a psql container with username and password
  create  ) if [[ "$(was_container_created)" = "true" ]] ; then
              echo 'Docker container has already been created.'
              exit 1
            fi

            # db_username or db_password has not been passed as command-line arguments
            if [[ "$#" -ne 3 ]] ; then
              echo 'No username or password specified!'
              echo './psql_docker.sh create [db_username] [db_password]'
              exit 1
            fi
            db_username=$2
            db_password=$3

            docker volume create pgdata

            docker run --name "${container_name}" -e POSTGRES_PASSWORD="${db_password}" -e POSTGRES_USER="${db_username}" -d -v pgdata:/var/lib/postgresql/data -p 5432:5432 postgres
            exit $?
            ;;
  # start a psql container
  start   ) if [[ "$(was_container_created)" = "false" ]] ; then
              echo 'Docker container has not been created yet.'
              echo 'Please create the container by running ./psql_docker.sh create'
              exit 1
            fi

            docker container start "${container_name}"
            exit $?
            ;;
  # stop a psql container
  stop    ) if [[ "$(was_container_created)" = "false" ]] ; then
              echo 'Docker container has not been created yet.'
              echo 'Please create the container by running ./psql_docker.sh create'
              exit 1
            fi

            docker container stop "${container_name}"
            exit $?
            ;;
  *       ) echo 'Invalid command. Valid commands are: create/start/stop'
            exit 1
            ;;
esac




