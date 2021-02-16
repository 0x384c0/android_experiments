alias adb="~/Library/Android/sdk/platform-tools/adb"
IP_ADDRESS=$(adb shell netcfg | grep wlan0 | grep -oE "(?:[0-9]{1,3}\.){3}[0-9]{1,3}")
adb tcpip 5555
sleep 5
echo "Connecting to \"$IP_ADDRESS:5555\""
adb connect $IP_ADDRESS:5555
echo "Run \"adb usb\", to disable wifi adb"