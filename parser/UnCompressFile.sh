#!/bin/bash
java -cp "./build:./lib/*" -Xmx256M org.deri.eurostat.zip.UnCompressXML "$@"
mysql -u root -ptFKjvQoKrUFjeAbWFmGxn24TsozTdTkf3SXyEohy9JfMisF3xkfrLc