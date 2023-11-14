#!/bin/bash

today = `date +%m-%d-%Y`

git add . && git commit -m "Deploy to dokku in ~> ${today}" && git push -f dokku master && git push -f origin master
