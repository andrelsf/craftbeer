FROM    postgres:13.1-alpine

		# ADD Timezone
RUN     apk add --no-cache tzdata shadow \
        && echo "America/Sao_Paulo" > /etc/timezone

RUN     groupadd psql -g 1000 \
        && getent passwd 1000 || adduser -u 1000 -G psql --disabled-password -g "" psql \
		&& usermod -aG psql postgres

RUN		chown psql:psql /var/lib/postgresql -R

USER 	psql

		# RUN create.sql on init
ADD     ./initial-database.sql /docker-entrypoint-initdb.d