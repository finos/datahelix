## Chocolatey

Chocolatey is a windows package manager for applications. The Data Helix generator has been published to this service to improve the usability of the tool.

### Getting Data Helix

1. [Install Chocolatey](https://chocolatey.org/install)
1. Execute `choco install datahelix` to install the latest version

### Updating the package

The update process for the package cannot - currently - be automated, as it requires a windows build agent (to run Powershell). The steps below detail how to update the package (manually) at present

On a Windows computer with Powershell installed (these are the only requirements, no need to be able to build the project)

#### Initial setup steps
1. Ensure [Chocolatey is installed](https://chocolatey.org/install).
1. Install the [auto-updater](https://github.com/majkinetor/au/blob/master/README.md) chocolatey module
   1. Launch Powershell in administrative mode
   1. Execute `Install-Module au`
1. Execute `choco apikey <apikey>` key is accessible from the [chocolatey account page](https://chocolatey.org/account).

#### Per update steps
1. Ensure the latest release has been published to GitHub releases
1. Open this directory in a powershell command prompt, doesn't need to have admin privileges)
   1. Execute `.\update.ps1`
      1. This will determine the latest release number and calculate the checksum of the .zip file
      1. These details will be inserted into the `.nuspec` and `chocolateyinstall.ps1` scripts
   1. Execute `choco pack`
   1. Execute `choco push datahelix-X.Y.Z.nupkg --source https://push.chocolatey.org/`
   1. Discard the changes to the `.nuspec` and `chocolateyinstall.ps1` files

The package will be pushed to the community feed for chocolatey, it will be 'private' until all of the following validation/moderation steps are complete:

1. Automatic validation
1. Automatic verification
1. Manual moderation

Once all steps are complete the new version will be published.