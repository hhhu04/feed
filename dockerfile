# FROM hhhu04/ubuntu
# RUN mkdir /feed
# COPY /build/libs/feed-0.1.jar /feed
# COPY start.sh ./start.sh
# COPY feed.sql /feed
# EXPOSE 8080
# ENTRYPOINT ["./start.sh"]

FROM openjdk:oraclelinux8
RUN mkdir /feed
COPY /build/libs/feed-0.1.jar /feed
CMD ["java","-jar","/feed/feed-0.1.jar"]
