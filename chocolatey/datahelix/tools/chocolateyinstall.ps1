$ErrorActionPreference = 'Stop';
$toolsDir   = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
$version    = $env:ChocolateyPackageVersion
$url32      = "https://github.com/finos/datahelix/releases/download/v$($version)/datahelix.zip"
$url64      = $url32
$checksum32 = '<run ./update.ps1 to set this>'
$checksum64 = '<run ./update.ps1 to set this>'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = $toolsDir
  url           = $url32
  url64         = $url64

  softwareName  = 'datahelix*'
  checksum      = $checksum32
  checksumType  = 'sha256'
  checksum64    = $checksum64
  checksumType64= 'sha256'
}

Write-Host "Installing Data Helix: $($version)"
Install-ChocolateyZipPackage @packageArgs

Install-BinFile -name "datahelix" -path "$env:ChocolateyInstall\datahelix\bin\datahelix.bat"
