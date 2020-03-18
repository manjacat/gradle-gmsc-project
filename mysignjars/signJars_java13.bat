@echo off
For %%f In (*.pack.gz) Do del %%f -F -Q
For %%f In (*.jar) Do "C:\Program Files\Java\jdk-13.0.2\bin\pack200" --repack --segment-limit=-1 %%f
For %%f In (*.jar) Do  "C:\Program Files\Java\jdk-13.0.2\bin\jarsigner" -keystore "GMSCDUDe" -storepass dude2014 %%f DUDe
For %%f In (*.jar) Do "C:\Program Files\Java\jdk-13.0.2\bin\pack200" --segment-limit=-1 %%f.pack.gz %%f
For %%f In (*.pack.gz) Do "C:\Program Files\Java\jdk-13.0.2\bin\unpack200.exe" %%f %%f.validate.jar 
For %%f In (*.validate.jar) Do  "C:\Program Files\Java\jdk-13.0.2\bin\jarsigner" -verify %%f
For %%f In (*.validate.jar) Do del %%f -F -Q
For %%f In (*.bak) Do del %%f -F -Q
pause