# Data Helix Road Map

Q4 - 2019
- Goal 1 - Deliver a Version 1 product that is faster and  easier to use for a simple table data set than data masking/anonymising of production data
  - Improve the usability of data helix by making the profile json schema less verbose, better structured and more intuitive
  - Host a Sandbox Data Helix UI and AWS lambda backed backend so users can load/edit and data helix profiles and see the results they generate all on a single web page.
  - add ability to configure a weighted distribution for user defined types
  - add ability for fields to depend on data in other fields, starting with date columns.  Eg DATE 1 = DATE 2 + N DAYS
  - add ability to call at least one 3rd party generator tool's data generators within data helix
  - provide ability for users of dat ahelic to integrate their own custom generators and refernce them in the profile file.
  - add ability to define unique combinations of fields within data helix via a multi-column csv file.
- Goal 2 - Prove product with 6 key users
  - This will involve implementing new features that enable this which will get from feedback from new users

Q1 - 2020
- Goal 3 - Scale out users wise
  - This will involve implementing new features that enable this which will get from feedback from new users

Q2 - 2020
- Goal 4 - Enhance to satisfy Foreign Keys In Db Tables Use Case
- Goal 5 - Enhance to satisfy component streaming Use Case (Kafka, Kinesis, heading for the future)


