alias adb="~/Library/Android/sdk/platform-tools/adb"
IP_ADDRESS=$(adb shell netcfg | grep wlan0 | grep -oE "(?:[0-9]{1,3}\.){3}[0-9]{1,3}")
adb tcpip 5555
sleep 5
adb connect $IP_ADDRESS:5555
echo "Connected to \"$IP_ADDRESS:5555\". Run \"adb usb\", to disable wifi adb"