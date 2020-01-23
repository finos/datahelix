Import-Module au

function global:au_GetLatest() {
     $releases = "https://github.com/finos/datahelix/releases/latest"

     $download_page = Invoke-WebRequest -Uri $releases -UseBasicParsing #1
     $regex   = '.zip$'
     $url     = $download_page.links | ? href -match $regex | select -First 1 -expand href #2
     $version = [System.Text.RegularExpressions.Regex]::Match($url, "v(\d+\.\d+\.\d+)").Groups[1].Value

     return @{ Version = $version; URL32 = "https://github.com" + $url }
}

function global:au_SearchReplace() {
    @{
        "tools\chocolateyInstall.ps1" = @{
            "(^[$]checksum32\s*=\s*)('.*')" = "`$1'$($Latest.Checksum32)'"
            "(^[$]checksum64\s*=\s*)('.*')" = "`$1'$($Latest.Checksum32)'"
        }
    }
}

function global:au_BeforeUpdate() {
    $Latest.Checksum32 = Get-RemoteChecksum $Latest.Url32
}

##copied from: https://www.powershellgallery.com/packages/AU/2017.3.29/Content/Public%5CGet-RemoteChecksum.ps1
function global:Get-RemoteChecksum($Url, $Algorithm='sha256') {
    $fn = [System.IO.Path]::GetTempFileName()
    Invoke-WebRequest $Url -OutFile $fn -UseBasicParsing
    $res = Get-FileHash $fn -Algorithm $Algorithm | % Hash
    rm $fn -ea ignore
    return $res.ToLower()
}

update -ChecksumFor none -NoReadme
