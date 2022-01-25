#!/bin/sh

sudo service mysql start
mysql -uroot -p12345678 < /feed/feed.sql
java -jar /feed/feed-0.1.jar