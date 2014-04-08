WaterLevel
==========
An embedded android project for the course "BTE5484: Embedded Android".


About
-----
This app shows the current rotation of the device (BFH cape) as a virtual water level on the display.

Hardware access
---------------
The project uses the JNI to access a I2C motion sensor via C.
The LED and button are implemented via sysfs.

Installation on the BBB-BFH-Cape
--------------------------------
By copying:

1. Copy the file WaterLevel.apk onto your microSD card. And start the device.
2. Make sure the it is possible to install apps from unknown sources:
   Settings > Security > Unknown sources
3. Navigate to the copied file and install by touching on WaterLevel.apk
4. The app should now be installed and available in the app list

Or via adb:

1. `adb install /mydir/to/WaterLevel.apk`

LEDs and button states
----------------------
If the rotation is within a acceptable limit, the LED L1 is ON, else the LED is OFF.
It's possible to set the current rotation as default rotation by pushing the button T1.
