## Apt-get

Apt-get (Advanced Package Tool get) is a package manager that is a part of Debian linux and Debian-based linux distributions, such as Ubuntu.
The datahelix tool is published to a private repository which can be added as a source to apt-get, allowing install and automatic update.

### Building the .deb package

A `.deb` package can be built using the `createDeb` gradle task, runnable with command `gradle createDeb`.
This task will package the datahelix jar and a shell script so that when installed the datahelix will be runnable from the terminal globally. It will also package a compressed documentation file or 'manual page', so that it can be viewed with command `man datahelix` (so long as a man page viewer is installed). 
This [documentation file](../../../orchestrator\src\main\resources\datahelix.1) may occasionally need to be updated if command line options change --  after editing it should be compressed using [gzip](https://www.gnu.org/software/gzip/) with the command `gzip -k -9 $path` (maximum compression, keep original file).

### Publishing a new version of the package

The package is hosted on [gemfury](https://gemfury.com/), a hosted package repository.
Uploading a new version of the package can be done in a couple of ways.
To upload through the site, simply login with user details located in the shared folder, click the button on the 'Upload' tab, and select the `.deb` file.
Alternatively it is possible to upload via a push request (e.g. by using cURL on the command line).
 [This is documented on the gemfury site](https://gemfury.com/help/upload-packages).