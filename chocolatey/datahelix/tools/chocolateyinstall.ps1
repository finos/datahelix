$ErrorActionPreference = 'Stop';
$toolsDir   = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)\..\..\"
$version    = $env:ChocolateyPackageVersion
$url        = "https://github.com/finos/datahelix/releases/download/v$($version)/datahelix.zip"
$checksum   = '<run ./update.ps1 to set this>'
$shimPath   = "$($env:ChocolateyInstall)\lib\datahelix\bin\datahelix.bat"

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = $toolsDir
  url           = $url

  softwareName  = 'datahelix*'
  checksum      = $checksum
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs

Install-BinFile -name "datahelix" -path $shimPath
