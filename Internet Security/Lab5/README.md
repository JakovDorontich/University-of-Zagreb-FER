# Lab 5
# Reverse Engineering Android Application

Fetch the latest version of the assignment from the repository:

```
$ git pull
```

In this assignment, a malicious Android "Wallpaper" application will be analyzed.

To get acquainted with the basics of the Android applications, it is recommended to look at:
```
https://developer.android.com/guide/components/fundamentals.html#Manifest
```

It is also recommended to analyze Android application permissions:
```
https://developer.android.com/reference/android/Manifest.permission.html
```

An example of a methodology for analyzing a suspicious application:

- Check Resources and Functionalities: Analyzing app sources (e.g., the unofficial Google Play Store website) and what exactly does the app identify as. This step is the basis for further analysis, but it will not be done in this assignment because the application is already known to be malicious.

- Check the permissions requested by the application: Examine the permissions that the application is requiring and compare them with the expected permissions for the features that the application provides to the users. If the application is requiring more permissions then necessary, then it is a candidate for further analysis.

- Data: based on the required permissions, we can build the matrix of data that the application can access.

- Network connections: The last step is to analyze the source code. It is necessary to check whether the application is opening some network connections, what are the IP addresses it uses, what is transferred, are there any advertising libraries used, etc.

# Analyse lab5.apk

### 1. Permissions
Compare the requested permissions with the expected permissions for Wallpaper application.
Permissions can be found in AndroidManifest.xml, extracted using apktool:

```
$ apktool d lab5.apk
$ cd lab5
$ less AndroidManifest.xml
```

### 2. Source code preparation 

The Android operating system has its own virtual machine, called the Dalvik Virtual Machine (DVM). 
The file containing the byte code is called DEX (Dalvik Executable). The source code is compiled to .dex and then packed to file .apk.
By reverse engineering the .dex file, we can get the source code.
That can be done using dex2jar, unzip and procyon tools:

Unzip lab5.apk to new  directory:
```
$ unzip lab5.apk -d lab5_extracted/
```

Convert a .dex file (classes.dex) to .jar and decompile it using the procyon tool installed under the name decompiler.jar:
```
$ cd lab5_extracted
$ /usr/local/bin/dex2jar-2.0/d2j-dex2jar.sh classes.dex
$ java8 -jar /usr/local/bin/decompiler.jar -jar <path_to_jar_file> -o <source_code_destination_directory>
```

### 3. Source code analysis

Analyze the source code to determine which parts of the code are malicious and what are they doing.
The recommended analysis sequence:

CutePuppiesWallpaper -> BotService -> BotClient -> BotWorker -> BotLocationHandler -> BotSMSHandler.

### 4. Applications Used

More information about the tools:

- apktool: https://ibotpeaches.github.io/Apktool/
- dex2jar: https://sourceforge.net/projects/dex2jar/
- decompiler / procyon: https://bitbucket.org/mstrobel/procyon/wiki/Java%20Decompiler
