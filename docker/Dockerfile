FROM    postgres:13.1-alpine

		# ADD Timezone
RUN     apk add --no-cache tzdata \
        && echo "America/Sao_Paulo" > /etc/timezone

		# RUN create.sql on init
ADD     ./initial-database.sql /docker-entrypoint-initdb.d