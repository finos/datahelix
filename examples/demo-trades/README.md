## Overview
 - Example schema for trade list that can use for a demo
 - The smaller profile is used to generate simpler data that can then be used to demonstrate the profiler

## Demo Script

can run data helix using following scripts that take profile file as an argument:
json_gen.sh:
```
#!/usr/bin/bash
java -jar generator.jar generate --output-format JSON --output-path output/output.json --replace --max-rows=25 --generation-type=RANDOM --profile-file=$1
```

csv_gen.sh:
```#!/usr/bin/bash
java -jar generator.jar generate --output-format CSV --output-path output/output.csv --replace --max-rows=25 --generation-type=RANDOM --profile-file=$1
```

can run profiler using following script (NB: the -D option is only needed if running on laptop not connected to wifi):
run.sh:
```#!/usr/bin/bash
java -Dspark.driver.host=localhost -jar profiler.jar $1 output/
```

directory format that need to populate for demo is:
datahelixdemo/
datahelixdemo/generator/
datahelixdemo/generator/json_gen.sh
datahelixdemo/generator/csv_gen.sh
datahelixdemo/generator/generator.jar (which is copied from./orchestrator/build/libs/generator.jar in data helix project after run 'gradle build')
datahelixdemo/generator/profile.json
datahelixdemo/generator/smaller_profile.json
datahelixdemo/generator/stockCodes.csv
datahelixdemo/output/

datahelixdemo/profiler/
datahelixdemo/profiler/run.sh
datahelixdemo/profiler/profiler.jar (which is copied from ./frontend-cli/build/libs/frontend-cli-0.0.1-SNAPSHOT.jar after done 'gradle build bootJar' in profiler project)


DEMO FORMAT:

0 - Before demo
 - edit path to stockCodes.csv in the 2 profile json files so its assumed its in current directory (ie get rid of any path to it)
 - can do following to generate a file that can be used by profiler:
```
cd generator
./csv_gen.sh smaller_profile.json 
cp output/output.csv ../profiler/sample_trade_data.csv
```

1 - show how can use data helix to generate (more readable than csv) json data from profile.json:
```
cd generator
./json_gen.sh smaller_profile.json 
vi output/output.json
```
    
2 - show how cna use profiler to generate a config from some sample data
```
cd ../profiler
./run.sh sample_trade_data.csv
vi output/sample_trade_data-profile.json
cp output/sample_trade_data-profile.json ../generator
```

3 - show how can use file profiler generated to generate data
```
cd ../generator
./json_gen.sh smaller_profile.json 
vi output/output.json
```
