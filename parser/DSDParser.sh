#!/bin/bash
java -cp "./build:./lib/*" -Xmx512M org.deri.eurostat.dsdparser.DSDParser "$@"