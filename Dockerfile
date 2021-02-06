FROM    openjdk:8u282-jre-slim

ENV     LANG pt_BR.UTF-8  
ENV     LANGUAGE pt_BR:pt_BR  
ENV     LC_ALL pt_BR.UTF-8

        # Set timezone
RUN     apt-get update \ 
        && apt-get install tzdata locales -y \
        && sed -i -e 's/# pt_BR.UTF-8 UTF-8/pt_BR.UTF-8 UTF-8/' /etc/locale.gen && locale-gen \
        && cp /usr/share/zoneinfo/America/Sao_Paulo /etc/localtime

RUN     getent group 1000 || groupadd spring -g 1000 \
        && getent passwd 1000 || adduser --uid 1000 --gid 1000 --disabled-password --gecos "" spring 

USER    spring:spring
ADD     ./target/craft-beer-1.0.jar /app/

WORKDIR /app