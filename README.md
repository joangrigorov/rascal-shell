# How to run it?

	java -Xmx1G -Xss32m -jar rascal.jar

This will start a console. If you have a Rascal source module File.rsc with this function:

	public void main(list[str] args)

You can run it from the command line by typing:

	java -Xmx1G -Xss32m -jar rascal.jar File.rsc arg1 arg2 …

# Where to get it?

There are two variants of the fully contained jar file for Rascal Shell: 
- [rascal-shell-stable.jar](http://update.rascal-mpl.org/console/rascal-shell-stable.jar), the latest release
- [rascal-shell-unstable.jar](http://update.rascal-mpl.org/console/rascal-shell-unstable.jar), the [unstable CI build![Build Status](http://ci.usethesource.io/job/usethesource/job/rascal-shell/job/master/badge/icon)](http://ci.usethesource.io/job/usethesource/job/rascal-shell/job/master/)


# How to make it yourself?

- Check out the rascal-shell project
- Find the RascalShell class and select "Run As Java Program"
- Edit the run configuration to use more stack and heap space: -Xss32m -Xmx1000m
- Select the rascal-shell project and choose "Export ..." from the context menu
- Select Java->"Runnable JAR file", this starts a wizard
- Select your newly made run configuration from the top dropdown box
- Type an export destination, like ``/Users/jurgenv/Desktop/rascal-0.4.20.jar``
- Select "Extract required libraries into generated JAR"
- Press "Finish"
- An error dialog pops up; read the messages and ignore all "class file compiled with compiler warnings", but review the other messages
- When satisfied, press "Ok", which brings you back to the main wizard.
- Now press "Cancel", and the jar will still be where you exported it to
- Test it
- Deploy
