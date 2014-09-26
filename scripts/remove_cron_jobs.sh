#!/bin/sh

crontab -l > delta_imports.crontab
crontab -r
