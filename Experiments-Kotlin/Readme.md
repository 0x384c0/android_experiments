## Description
Android application for testing

## Folders
* `Application/` contains android projects
* `CoreNetwork/` android library with core functionality

## Requirements
* Android Studio 3.3
* ruby 2.3
* Bundler 1.16.1

## make commands for CI
* you must setup ANDROID_HOME env variable in your CI
* upload build to fabric : ```make beta FABRIC_API_TOKEN=<token> FABRIC_BUILD_SECRET=<secret>```
* create git flow release : ```make create_release```