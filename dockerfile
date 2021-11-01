FROM openjdk:11
RUN mkdir /feed
COPY /build/libs/feed-0.1.jar /feed
CMD ["java","-jar","/feed/feed-0.1.jar"]