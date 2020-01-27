$ErrorActionPreference = 'Stop';
$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  softwareName  = 'datahelix*'
}
$shimPath   = "$($env:ChocolateyInstall)\lib\datahelix\bin\datahelix.bat"

Uninstall-BinFile -name "datahelix" -path $shimPath