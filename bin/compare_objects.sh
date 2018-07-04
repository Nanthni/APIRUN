curl -gH "Authorization: Token token=$tokenid" "https://$hostname/harvester/api/jobs/$jobid/upload" --form "id=$jobid" --form "connection=$connectorname" -F "file=@$absolutefilepath"
java -classpath "$Classpath" -jar ./lib/OrHarvesterUtil-0.0.1-jar-with-dependencies.jar -runthisjob $hostname $tokenid $jobid
cd ..
Root="$(pwd)"
Classpath=$Root/lib/*
java -classpath "$Classpath" com.orion.regression.ObjectRegression