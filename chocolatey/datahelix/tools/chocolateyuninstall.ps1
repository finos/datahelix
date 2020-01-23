$ErrorActionPreference = 'Stop';
$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  softwareName  = 'datahelix*'
}

Uninstall-BinFile -name "datahelix" -path "$env:ChocolateyInstall\datahelix\bin\datahelix.bat"