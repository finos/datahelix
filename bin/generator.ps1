
write-host "DataHelix Generator Executor Script"

$DATAHELIX_GENERATOR_HOME="c:\datahelix\generator"  # Change this to be the path that the `generator.jar` file is in

$DATAHELIX_GENERATOR_JAR="generator.jar"

$DATAHELIX_GENERATOR_ARGS = [System.Collections.ArrayList]@()
$null=$DATAHELIX_GENERATOR_ARGS.add("-jar")  # $null is used here to supress output
$null=$DATAHELIX_GENERATOR_ARGS.add("$DATAHELIX_GENERATOR_HOME\$DATAHELIX_GENERATOR_JAR")

# add the arguments that were passed in on the command line
foreach($a in $args)
{
    $null=$DATAHELIX_GENERATOR_ARGS.add($a)
}

write-host "Executing the Datahelix Generator (java $DATAHELIX_GENERATOR_ARGS)"

Start-Process java -ArgumentList $DATAHELIX_GENERATOR_ARGS -RedirectStandardOutput '.\console.out' -RedirectStandardError '.\console.err' -NoNewWindow -wait -PassThru 

